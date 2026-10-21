package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class StudyPilotRepository(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("studypilot_prefs", Context.MODE_PRIVATE)

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    private val _chapters = MutableStateFlow<Map<String, List<Chapter>>>(emptyMap())
    val chapters: StateFlow<Map<String, List<Chapter>>> = _chapters.asStateFlow()

    private val _sessions = MutableStateFlow<List<StudySession>>(emptyList())
    val sessions: StateFlow<List<StudySession>> = _sessions.asStateFlow()

    private val _blockedApps = MutableStateFlow<List<BlockedApp>>(emptyList())
    val blockedApps: StateFlow<List<BlockedApp>> = _blockedApps.asStateFlow()

    private val _mockTests = MutableStateFlow<List<MockTest>>(emptyList())
    val mockTests: StateFlow<List<MockTest>> = _mockTests.asStateFlow()

    private val _notes = MutableStateFlow<List<NoteItem>>(emptyList())
    val notes: StateFlow<List<NoteItem>> = _notes.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _language = MutableStateFlow(prefs.getString("app_language", "en") ?: "en")
    val language: StateFlow<String> = _language.asStateFlow()

    private val _onboardingCompleted = MutableStateFlow(prefs.getBoolean("onboarding_done", false))
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    private val _appBlockingEnabled = MutableStateFlow(prefs.getBoolean("app_blocking_enabled", true))
    val appBlockingEnabled: StateFlow<Boolean> = _appBlockingEnabled.asStateFlow()

    private val _blockTiming = MutableStateFlow(prefs.getString("block_timing", "Focus Session") ?: "Focus Session")
    val blockTiming: StateFlow<String> = _blockTiming.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        // Initial defaults matching reference design
        val initialSubjects = listOf(
            Subject("sub_1", "Physics", "#06B6D4", "Admission", 8, 12),
            Subject("sub_2", "Mathematics", "#3B82F6", "Admission", 6, 10),
            Subject("sub_3", "Chemistry", "#F97316", "Admission", 5, 10),
            Subject("sub_4", "Biology", "#10B981", "HSC", 3, 6),
            Subject("sub_5", "English", "#8B5CF6", "HSC", 2, 6)
        )
        _subjects.value = initialSubjects

        val initialChapters = mapOf(
            "sub_1" to listOf(
                Chapter("ch_1", "sub_1", 1, "Motion", ChapterStatus.COMPLETED),
                Chapter("ch_2", "sub_1", 2, "Force", ChapterStatus.IN_PROGRESS),
                Chapter("ch_3", "sub_1", 3, "Work, Energy", ChapterStatus.NOT_STARTED),
                Chapter("ch_4", "sub_1", 4, "Gravitation", ChapterStatus.NOT_STARTED),
                Chapter("ch_5", "sub_1", 5, "Current Electricity", ChapterStatus.NOT_STARTED),
                Chapter("ch_6", "sub_1", 6, "Modern Physics", ChapterStatus.NOT_STARTED)
            ),
            "sub_2" to listOf(
                Chapter("ch_m1", "sub_2", 1, "Matrices & Determinants", ChapterStatus.COMPLETED),
                Chapter("ch_m2", "sub_2", 2, "Trigonometry", ChapterStatus.COMPLETED),
                Chapter("ch_m3", "sub_2", 3, "Differentiation", ChapterStatus.COMPLETED),
                Chapter("ch_m4", "sub_2", 4, "Integration", ChapterStatus.COMPLETED),
                Chapter("ch_m5", "sub_2", 5, "Straight Lines", ChapterStatus.COMPLETED),
                Chapter("ch_m6", "sub_2", 6, "Coordinate Geometry", ChapterStatus.IN_PROGRESS)
            )
        )
        _chapters.value = initialChapters

        val initialSessions = listOf(
            StudySession("s1", "Physics", "#06B6D4", "Chapter 5: Current Electricity", "08:00 - 10:00", "2025-10-20", true),
            StudySession("s2", "Mathematics", "#3B82F6", "Chapter 6: Coordinate Geometry", "10:30 - 12:00", "2025-10-20", false),
            StudySession("s3", "Chemistry", "#F97316", "Chapter 3: Chemical Bonding", "14:00 - 16:00", "2025-10-20", false),
            StudySession("s4", "English", "#8B5CF6", "Vocabulary + Reading", "18:00 - 20:00", "2025-10-20", false)
        )
        _sessions.value = initialSessions

        val initialBlocked = listOf(
            BlockedApp("app_yt", "YouTube", 0xFFFF0000, true),
            BlockedApp("app_ig", "Instagram", 0xFFE1306C, true),
            BlockedApp("app_tt", "TikTok", 0xFF000000, true),
            BlockedApp("app_fb", "Facebook", 0xFF1877F2, true),
            BlockedApp("app_wa", "WhatsApp", 0xFF25D366, true),
            BlockedApp("app_ch", "Chrome", 0xFF4285F4, true)
        )
        _blockedApps.value = initialBlocked

        val sampleQuestions = listOf(
            TestQuestion(1, "Which of the following is the unit of electric current?", listOf("Volt", "Ampere", "Ohm", "Watt"), 1),
            TestQuestion(2, "What is the dimensional formula of gravitational constant (G)?", listOf("[M⁻¹ L³ T⁻²]", "[M L² T⁻²]", "[M L⁻¹ T⁻²]", "[M⁻² L² T⁻¹]"), 0),
            TestQuestion(3, "According to Ohm's law, current is directly proportional to:", listOf("Resistance", "Potential difference", "Temperature", "Inductance"), 1),
            TestQuestion(4, "The momentum of a photon with wavelength λ is given by:", listOf("hλ", "h / λ", "hc / λ", "λ / h"), 1),
            TestQuestion(5, "In an elastic collision, which quantity is conserved?", listOf("Only kinetic energy", "Only linear momentum", "Both kinetic energy and momentum", "Neither"), 2)
        )

        val initialMockTests = listOf(
            MockTest("mt_1", "RU Admission Test", 100, 120, 4.8, "Admission", sampleQuestions),
            MockTest("mt_2", "CU Admission Test", 100, 120, 4.6, "Admission", sampleQuestions),
            MockTest("mt_3", "SUST Admission Test", 100, 120, 4.5, "University", sampleQuestions),
            MockTest("mt_4", "KUET Admission Test", 100, 120, 4.4, "University", sampleQuestions)
        )
        _mockTests.value = initialMockTests

        val initialNotes = listOf(
            NoteItem("n1", "Physics Formula Sheet", "Physics", "PDF", "2.4 MB", "Comprehensive formula compilation for HSC & Admission physics.", true),
            NoteItem("n2", "Chemistry Organic Notes", "Chemistry", "PDF", "1.8 MB", "All reaction mechanisms and conversions summarized.", false),
            NoteItem("n3", "Math Short Notes", "Mathematics", "PDF", "1.2 MB", "Quick shortcut techniques for calculus and coordinate geometry.", false),
            NoteItem("n4", "Important Questions", "Physics", "PDF", "3.6 MB", "Top recurring question bank with verified explanations.", true)
        )
        _notes.value = initialNotes

        val initialNotifications = listOf(
            NotificationItem("notif_1", "Study Reminder", "Your study plan for today is ready!", "2h ago", "Study", false),
            NotificationItem("notif_2", "Exam Countdown", "RU Admission Test is in 12 days!", "5h ago", "Reminder", false),
            NotificationItem("notif_3", "Focus Completed", "You completed 25 min focus session.", "8h ago", "Study", false),
            NotificationItem("notif_4", "New Mock Test", "CU Admission Test is now available.", "1d ago", "System", true),
            NotificationItem("notif_5", "Tips for You", "Best time to study is in the morning.", "1d ago", "Reminder", true)
        )
        _notifications.value = initialNotifications
    }

    fun setLanguage(lang: String) {
        prefs.edit().putString("app_language", lang).apply()
        _language.value = lang
    }

    fun setOnboardingCompleted() {
        prefs.edit().putBoolean("onboarding_done", true).apply()
        _onboardingCompleted.value = true
    }

    fun resetOnboarding() {
        prefs.edit().putBoolean("onboarding_done", false).apply()
        _onboardingCompleted.value = false
    }

    fun setAppBlockingEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("app_blocking_enabled", enabled).apply()
        _appBlockingEnabled.value = enabled
    }

    fun setBlockTiming(timing: String) {
        prefs.edit().putString("block_timing", timing).apply()
        _blockTiming.value = timing
    }

    fun toggleAppBlocked(id: String) {
        _blockedApps.value = _blockedApps.value.map {
            if (it.id == id) it.copy(isBlocked = !it.isBlocked) else it
        }
    }

    fun toggleSessionComplete(id: String) {
        _sessions.value = _sessions.value.map {
            if (it.id == id) {
                val newStatus = !it.isCompleted
                // Also update user profile study hours
                val delta = if (newStatus) 1.5f else -1.5f
                val newCompleted = (_userProfile.value.todayCompletedHours + delta).coerceAtLeast(0f)
                _userProfile.value = _userProfile.value.copy(todayCompletedHours = newCompleted)
                it.copy(isCompleted = newStatus)
            } else it
        }
    }

    fun addSubject(name: String, colorHex: String, examType: String) {
        val newSub = Subject(
            id = "sub_${System.currentTimeMillis()}",
            name = name,
            colorHex = colorHex,
            examType = examType,
            completedChapters = 0,
            totalChapters = 5
        )
        _subjects.value = _subjects.value + newSub
    }

    fun addChapter(subjectId: String, title: String) {
        val currentList = _chapters.value[subjectId] ?: emptyList()
        val nextNumber = currentList.size + 1
        val newChapter = Chapter(
            id = "ch_${System.currentTimeMillis()}",
            subjectId = subjectId,
            number = nextNumber,
            title = title,
            status = ChapterStatus.NOT_STARTED
        )
        val updatedMap = _chapters.value.toMutableMap()
        updatedMap[subjectId] = currentList + newChapter
        _chapters.value = updatedMap

        // Update subject total count
        _subjects.value = _subjects.value.map {
            if (it.id == subjectId) it.copy(totalChapters = it.totalChapters + 1) else it
        }
    }

    fun toggleChapterStatus(subjectId: String, chapterId: String) {
        val currentList = _chapters.value[subjectId] ?: return
        val updated = currentList.map {
            if (it.id == chapterId) {
                val nextStatus = when (it.status) {
                    ChapterStatus.NOT_STARTED -> ChapterStatus.IN_PROGRESS
                    ChapterStatus.IN_PROGRESS -> ChapterStatus.COMPLETED
                    ChapterStatus.COMPLETED -> ChapterStatus.NOT_STARTED
                }
                it.copy(status = nextStatus)
            } else it
        }
        val updatedMap = _chapters.value.toMutableMap()
        updatedMap[subjectId] = updated
        _chapters.value = updatedMap

        val completedCount = updated.count { it.status == ChapterStatus.COMPLETED }
        _subjects.value = _subjects.value.map {
            if (it.id == subjectId) it.copy(completedChapters = completedCount, totalChapters = updated.size) else it
        }
    }

    fun addNote(title: String, subject: String, content: String) {
        val newNote = NoteItem(
            id = "note_${System.currentTimeMillis()}",
            title = title,
            subject = subject,
            fileType = "PDF",
            fileSize = "1.5 MB",
            content = content
        )
        _notes.value = listOf(newNote) + _notes.value
    }

    fun toggleBookmarkNote(id: String) {
        _notes.value = _notes.value.map {
            if (it.id == id) it.copy(isBookmarked = !it.isBookmarked) else it
        }
    }

    fun markNotificationRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun logFocusSession(secondsCompleted: Int) {
        val hours = secondsCompleted / 3600f
        val newCompleted = (_userProfile.value.todayCompletedHours + hours).coerceAtMost(24f)
        _userProfile.value = _userProfile.value.copy(
            todayCompletedHours = (Math.round(newCompleted * 10) / 10f),
            totalStudyHours = _userProfile.value.totalStudyHours + (secondsCompleted / 3600)
        )
    }
}
