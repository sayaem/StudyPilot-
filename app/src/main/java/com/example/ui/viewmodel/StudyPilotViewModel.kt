package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.StudyPilotRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Splash : ScreenDestination()
    data class Onboarding(val page: Int) : ScreenDestination()
    object Home : ScreenDestination()
    object Planner : ScreenDestination()
    object Subjects : ScreenDestination()
    object AddSubject : ScreenDestination()
    data class ChapterSyllabus(val subjectId: String) : ScreenDestination()
    object SmartPlanner : ScreenDestination()
    object FocusIdle : ScreenDestination()
    object FocusRunning : ScreenDestination()
    object AppsBlocker : ScreenDestination()
    object Calendar : ScreenDestination()
    object MockTests : ScreenDestination()
    data class TestTaking(val testId: String) : ScreenDestination()
    data class TestResult(val score: Int, val correct: Int, val incorrect: Int, val timeTakenSeconds: Int) : ScreenDestination()
    object NotesLibrary : ScreenDestination()
    object Notifications : ScreenDestination()
    object Profile : ScreenDestination()
    object Settings : ScreenDestination()
    object Language : ScreenDestination()
    object AboutData : ScreenDestination()
    object PrivacyPolicy : ScreenDestination()
    object TermsConditions : ScreenDestination()
    object MotivationalEnd : ScreenDestination()
}

class StudyPilotViewModel(application: Application) : AndroidViewModel(application) {
    val repository = StudyPilotRepository(application)

    val subjects: StateFlow<List<Subject>> = repository.subjects
    val chapters: StateFlow<Map<String, List<Chapter>>> = repository.chapters
    val sessions: StateFlow<List<StudySession>> = repository.sessions
    val blockedApps: StateFlow<List<BlockedApp>> = repository.blockedApps
    val mockTests: StateFlow<List<MockTest>> = repository.mockTests
    val notes: StateFlow<List<NoteItem>> = repository.notes
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val language: StateFlow<String> = repository.language
    val appBlockingEnabled: StateFlow<Boolean> = repository.appBlockingEnabled
    val blockTiming: StateFlow<String> = repository.blockTiming

    // Navigation stack
    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Splash)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val navStack = mutableListOf<ScreenDestination>()

    // Focus Timer State
    private val _focusTotalSeconds = MutableStateFlow(25 * 60) // 25 min default
    val focusTotalSeconds: StateFlow<Int> = _focusTotalSeconds.asStateFlow()

    private val _focusRemainingSeconds = MutableStateFlow(25 * 60)
    val focusRemainingSeconds: StateFlow<Int> = _focusRemainingSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null

    // Test Taking State
    private val _currentTestId = MutableStateFlow("mt_1")
    val currentTestId: StateFlow<String> = _currentTestId.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val selectedAnswers: StateFlow<Map<Int, Int>> = _selectedAnswers.asStateFlow()

    private val _testSecondsElapsed = MutableStateFlow(0)
    val testSecondsElapsed: StateFlow<Int> = _testSecondsElapsed.asStateFlow()

    init {
        // Splash screen auto advance
        viewModelScope.launch {
            delay(1800)
            if (repository.onboardingCompleted.value) {
                navigateTo(ScreenDestination.Home, clearStack = true)
            } else {
                navigateTo(ScreenDestination.Onboarding(1), clearStack = true)
            }
        }
    }

    fun navigateTo(destination: ScreenDestination, clearStack: Boolean = false) {
        if (clearStack) {
            navStack.clear()
        } else {
            navStack.add(_currentScreen.value)
        }
        _currentScreen.value = destination
    }

    fun navigateBack(): Boolean {
        if (navStack.isNotEmpty()) {
            val prev = navStack.removeAt(navStack.size - 1)
            _currentScreen.value = prev
            return true
        }
        return false
    }

    fun completeOnboarding() {
        repository.setOnboardingCompleted()
        navigateTo(ScreenDestination.Home, clearStack = true)
    }

    fun setLanguage(lang: String) {
        repository.setLanguage(lang)
    }

    fun startFocusTimer(durationMinutes: Int = 25) {
        _focusTotalSeconds.value = durationMinutes * 60
        _focusRemainingSeconds.value = durationMinutes * 60
        _isTimerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_focusRemainingSeconds.value > 0 && _isTimerRunning.value) {
                delay(1000)
                if (_isTimerRunning.value) {
                    _focusRemainingSeconds.value -= 1
                }
            }
            if (_focusRemainingSeconds.value <= 0) {
                _isTimerRunning.value = false
                repository.logFocusSession(_focusTotalSeconds.value)
                navigateTo(ScreenDestination.MotivationalEnd)
            }
        }
        navigateTo(ScreenDestination.FocusRunning)
    }

    fun pauseResumeFocusTimer() {
        _isTimerRunning.value = !_isTimerRunning.value
    }

    fun endFocusSession() {
        val elapsed = _focusTotalSeconds.value - _focusRemainingSeconds.value
        if (elapsed > 30) {
            repository.logFocusSession(elapsed)
        }
        timerJob?.cancel()
        _isTimerRunning.value = false
        navigateTo(ScreenDestination.MotivationalEnd)
    }

    fun startMockTest(testId: String) {
        _currentTestId.value = testId
        _currentQuestionIndex.value = 0
        _selectedAnswers.value = emptyMap()
        _testSecondsElapsed.value = 88 // 01:28
        navigateTo(ScreenDestination.TestTaking(testId))
    }

    fun selectTestAnswer(questionIndex: Int, optionIndex: Int) {
        val current = _selectedAnswers.value.toMutableMap()
        current[questionIndex] = optionIndex
        _selectedAnswers.value = current
    }

    fun clearCurrentAnswer(questionIndex: Int) {
        val current = _selectedAnswers.value.toMutableMap()
        current.remove(questionIndex)
        _selectedAnswers.value = current
    }

    fun nextQuestion(totalQuestions: Int) {
        if (_currentQuestionIndex.value < totalQuestions - 1) {
            _currentQuestionIndex.value += 1
        } else {
            submitMockTest()
        }
    }

    fun submitMockTest() {
        val test = mockTests.value.find { it.id == _currentTestId.value }
            ?: mockTests.value.firstOrNull()
        val questions = test?.questions ?: emptyList()
        var correct = 0
        questions.forEachIndexed { index, q ->
            val selected = _selectedAnswers.value[index]
            if (selected == q.correctIndex) {
                correct++
            }
        }
        val incorrect = questions.size - correct
        val score = if (questions.isNotEmpty()) ((correct.toFloat() / questions.size) * 100).toInt() else 68

        navigateTo(
            ScreenDestination.TestResult(
                score = score,
                correct = correct,
                incorrect = incorrect,
                timeTakenSeconds = 105 * 60 + 32 // matching 01:45:32
            )
        )
    }
}
