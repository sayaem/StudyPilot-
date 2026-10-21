package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.StudyPilotTheme
import com.example.ui.viewmodel.ScreenDestination
import com.example.ui.viewmodel.StudyPilotViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: StudyPilotViewModel = viewModel()
            StudyPilotApp(viewModel = viewModel)
        }
    }
}

@Composable
fun StudyPilotApp(viewModel: StudyPilotViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val lang by viewModel.language.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val chaptersMap by viewModel.chapters.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val blockedApps by viewModel.blockedApps.collectAsState()
    val isBlockingEnabled by viewModel.appBlockingEnabled.collectAsState()
    val blockTiming by viewModel.blockTiming.collectAsState()
    val mockTests by viewModel.mockTests.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val focusSeconds by viewModel.focusRemainingSeconds.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val currentTestId by viewModel.currentTestId.collectAsState()
    val currentQIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedAnswers by viewModel.selectedAnswers.collectAsState()

    // Handle back button on secondary screens
    val isRootScreen = currentScreen is ScreenDestination.Home || currentScreen is ScreenDestination.Splash
    BackHandler(enabled = !isRootScreen) {
        viewModel.navigateBack()
    }

    StudyPilotTheme(darkTheme = currentScreen is ScreenDestination.FocusRunning) {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (val screen = currentScreen) {
                is ScreenDestination.Splash -> {
                    SplashScreen(
                        onFinished = {
                            if (viewModel.repository.onboardingCompleted.value) {
                                viewModel.navigateTo(ScreenDestination.Home, clearStack = true)
                            } else {
                                viewModel.navigateTo(ScreenDestination.Onboarding(1), clearStack = true)
                            }
                        }
                    )
                }

                is ScreenDestination.Onboarding -> {
                    OnboardingScreen(
                        page = screen.page,
                        lang = lang,
                        onNext = {
                            if (screen.page < 3) {
                                viewModel.navigateTo(ScreenDestination.Onboarding(screen.page + 1))
                            } else {
                                viewModel.completeOnboarding()
                            }
                        },
                        onSkip = {
                            viewModel.completeOnboarding()
                        }
                    )
                }

                is ScreenDestination.Home -> {
                    HomeScreen(
                        profile = profile,
                        lang = lang,
                        onNavigate = { route ->
                            when (route) {
                                "planner" -> viewModel.navigateTo(ScreenDestination.Planner)
                                "focus" -> viewModel.navigateTo(ScreenDestination.FocusIdle)
                                "calendar" -> viewModel.navigateTo(ScreenDestination.Calendar)
                                "more" -> viewModel.navigateTo(ScreenDestination.Profile)
                                "smart_planner" -> viewModel.navigateTo(ScreenDestination.SmartPlanner)
                                "mock_tests" -> viewModel.navigateTo(ScreenDestination.MockTests)
                                "notifications" -> viewModel.navigateTo(ScreenDestination.Notifications)
                                "profile" -> viewModel.navigateTo(ScreenDestination.Profile)
                                "subjects" -> viewModel.navigateTo(ScreenDestination.Subjects)
                                "notes" -> viewModel.navigateTo(ScreenDestination.NotesLibrary)
                            }
                        }
                    )
                }

                is ScreenDestination.Planner -> {
                    PlannerScreen(
                        sessions = sessions,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onToggleSession = { viewModel.repository.toggleSessionComplete(it) },
                        onNavigateCalendar = { viewModel.navigateTo(ScreenDestination.Calendar) }
                    )
                }

                is ScreenDestination.Subjects -> {
                    SubjectsScreen(
                        subjects = subjects,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onSelectSubject = { subId ->
                            viewModel.navigateTo(ScreenDestination.ChapterSyllabus(subId))
                        },
                        onAddSubject = {
                            viewModel.navigateTo(ScreenDestination.AddSubject)
                        }
                    )
                }

                is ScreenDestination.AddSubject -> {
                    AddSubjectScreen(
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onSave = { name, colorHex, examType ->
                            viewModel.repository.addSubject(name, colorHex, examType)
                            viewModel.navigateBack()
                        }
                    )
                }

                is ScreenDestination.ChapterSyllabus -> {
                    val subject = subjects.find { it.id == screen.subjectId } ?: subjects.first()
                    val chapters = chaptersMap[subject.id] ?: emptyList()
                    ChapterSyllabusScreen(
                        subject = subject,
                        chapters = chapters,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onToggleStatus = { chId ->
                            viewModel.repository.toggleChapterStatus(subject.id, chId)
                        },
                        onAddChapter = { title ->
                            viewModel.repository.addChapter(subject.id, title)
                        }
                    )
                }

                is ScreenDestination.SmartPlanner -> {
                    SmartPlannerScreen(
                        lang = lang,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is ScreenDestination.FocusIdle -> {
                    FocusModeIdleScreen(
                        lang = lang,
                        onStartFocus = { viewModel.startFocusTimer(25) },
                        onNavigateAppsBlocker = { viewModel.navigateTo(ScreenDestination.AppsBlocker) },
                        onNavigateSettings = { viewModel.navigateTo(ScreenDestination.Settings) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is ScreenDestination.FocusRunning -> {
                    FocusRunningScreen(
                        remainingSeconds = focusSeconds,
                        isRunning = isTimerRunning,
                        lang = lang,
                        onPauseResume = { viewModel.pauseResumeFocusTimer() },
                        onEndSession = { viewModel.endFocusSession() },
                        onNavigateAppsBlocker = { viewModel.navigateTo(ScreenDestination.AppsBlocker) }
                    )
                }

                is ScreenDestination.AppsBlocker -> {
                    AppsBlockerScreen(
                        blockedApps = blockedApps,
                        isBlockingEnabled = isBlockingEnabled,
                        blockTiming = blockTiming,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onToggleEnable = { viewModel.repository.setAppBlockingEnabled(it) },
                        onToggleApp = { viewModel.repository.toggleAppBlocked(it) },
                        onSelectTiming = { viewModel.repository.setBlockTiming(it) }
                    )
                }

                is ScreenDestination.Calendar -> {
                    CalendarScreen(
                        sessions = sessions,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onNavigate = { route ->
                            when (route) {
                                "home" -> viewModel.navigateTo(ScreenDestination.Home)
                                "planner" -> viewModel.navigateTo(ScreenDestination.Planner)
                                "focus" -> viewModel.navigateTo(ScreenDestination.FocusIdle)
                                "more" -> viewModel.navigateTo(ScreenDestination.Profile)
                            }
                        }
                    )
                }

                is ScreenDestination.MockTests -> {
                    MockTestsScreen(
                        tests = mockTests,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onSelectTest = { testId -> viewModel.startMockTest(testId) },
                        onNavigate = { route ->
                            when (route) {
                                "home" -> viewModel.navigateTo(ScreenDestination.Home)
                                "planner" -> viewModel.navigateTo(ScreenDestination.Planner)
                                "focus" -> viewModel.navigateTo(ScreenDestination.FocusIdle)
                                "calendar" -> viewModel.navigateTo(ScreenDestination.Calendar)
                                "more" -> viewModel.navigateTo(ScreenDestination.Profile)
                            }
                        }
                    )
                }

                is ScreenDestination.TestTaking -> {
                    val test = mockTests.find { it.id == screen.testId } ?: mockTests.first()
                    TestTakingScreen(
                        test = test,
                        questionIndex = currentQIndex,
                        selectedAnswers = selectedAnswers,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onSelectOption = { qIdx, optIdx ->
                            viewModel.selectTestAnswer(qIdx, optIdx)
                        },
                        onClearOption = { qIdx ->
                            viewModel.clearCurrentAnswer(qIdx)
                        },
                        onNext = {
                            viewModel.nextQuestion(test.questions.size)
                        }
                    )
                }

                is ScreenDestination.TestResult -> {
                    TestResultScreen(
                        score = screen.score,
                        correct = screen.correct,
                        incorrect = screen.incorrect,
                        timeTakenSeconds = screen.timeTakenSeconds,
                        lang = lang,
                        onBack = { viewModel.navigateTo(ScreenDestination.MockTests, clearStack = true) },
                        onReviewAnswers = {
                            viewModel.navigateTo(ScreenDestination.TestTaking(currentTestId))
                        },
                        onTryAgain = {
                            viewModel.startMockTest(currentTestId)
                        }
                    )
                }

                is ScreenDestination.NotesLibrary -> {
                    NotesLibraryScreen(
                        notes = notes,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onToggleBookmark = { viewModel.repository.toggleBookmarkNote(it) },
                        onAddNote = { t, s, c -> viewModel.repository.addNote(t, s, c) },
                        onNavigate = { route ->
                            when (route) {
                                "home" -> viewModel.navigateTo(ScreenDestination.Home)
                                "planner" -> viewModel.navigateTo(ScreenDestination.Planner)
                                "focus" -> viewModel.navigateTo(ScreenDestination.FocusIdle)
                                "calendar" -> viewModel.navigateTo(ScreenDestination.Calendar)
                                "more" -> viewModel.navigateTo(ScreenDestination.Profile)
                            }
                        }
                    )
                }

                is ScreenDestination.Notifications -> {
                    NotificationsScreen(
                        notifications = notifications,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onMarkRead = { viewModel.repository.markNotificationRead(it) }
                    )
                }

                is ScreenDestination.Profile -> {
                    ProfileScreen(
                        profile = profile,
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onNavigateLanguage = { viewModel.navigateTo(ScreenDestination.Language) },
                        onNavigateAboutData = { viewModel.navigateTo(ScreenDestination.AboutData) },
                        onNavigateSettings = { viewModel.navigateTo(ScreenDestination.Settings) },
                        onNavigate = { route ->
                            when (route) {
                                "home" -> viewModel.navigateTo(ScreenDestination.Home)
                                "planner" -> viewModel.navigateTo(ScreenDestination.Planner)
                                "focus" -> viewModel.navigateTo(ScreenDestination.FocusIdle)
                                "calendar" -> viewModel.navigateTo(ScreenDestination.Calendar)
                                "notes" -> viewModel.navigateTo(ScreenDestination.NotesLibrary)
                                "subjects" -> viewModel.navigateTo(ScreenDestination.Subjects)
                            }
                        }
                    )
                }

                is ScreenDestination.Settings -> {
                    SettingsScreen(
                        lang = lang,
                        onBack = { viewModel.navigateBack() },
                        onNavigateLanguage = { viewModel.navigateTo(ScreenDestination.Language) },
                        onNavigateAboutData = { viewModel.navigateTo(ScreenDestination.AboutData) },
                        onNavigatePrivacy = { viewModel.navigateTo(ScreenDestination.PrivacyPolicy) },
                        onNavigateTerms = { viewModel.navigateTo(ScreenDestination.TermsConditions) }
                    )
                }

                is ScreenDestination.Language -> {
                    LanguageSelectionScreen(
                        currentLang = lang,
                        onBack = { viewModel.navigateBack() },
                        onSelectLanguage = { newLang ->
                            viewModel.setLanguage(newLang)
                        }
                    )
                }

                is ScreenDestination.AboutData -> {
                    AboutDataScreen(
                        lang = lang,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is ScreenDestination.PrivacyPolicy -> {
                    val privacySections = listOf(
                        "1. Introduction" to "Welcome to StudyPilot. We respect your privacy and are committed to protecting your personal data.",
                        "2. Information We Collect" to "We store course syllabus progress, scheduled study sessions, and focus mode statistics locally on your device.",
                        "3. How We Use Your Information" to "To provide personalized timetable recommendations and track your exam readiness.",
                        "4. Data Security" to "Your study notes and exam questions are stored safely using modern Android sandboxed storage.",
                        "5. Third Party Services" to "StudyPilot operates fully offline-first for student productivity and does not sell personal records.",
                        "6. Your Rights" to "You have full control to reset, export, or erase all your saved subjects and notes at any time in Settings."
                    )
                    PolicyExpandableScreen(
                        title = "Privacy Policy",
                        sections = privacySections,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is ScreenDestination.TermsConditions -> {
                    val termsSections = listOf(
                        "1. Acceptance of Terms" to "By using StudyPilot, you agree to these terms designed to support your academic success.",
                        "2. Use of Service" to "StudyPilot is built for personal, non-commercial educational preparation.",
                        "3. User Responsibilities" to "You are responsible for managing your focus sessions and maintaining your study goals.",
                        "4. Intellectual Property" to "All trademarks, illustrations, and StudyPilot designs are protected intellectual property.",
                        "5. Limitation of Liability" to "StudyPilot assists your study planning, but actual test scores depend on personal dedication.",
                        "6. Changes to Terms" to "We may update these terms periodically to enhance privacy and add student features."
                    )
                    PolicyExpandableScreen(
                        title = "Terms & Conditions",
                        sections = termsSections,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is ScreenDestination.MotivationalEnd -> {
                    MotivationalEndScreen(
                        lang = lang,
                        onBackToHome = { viewModel.navigateTo(ScreenDestination.Home, clearStack = true) }
                    )
                }
            }
        }
    }
}
