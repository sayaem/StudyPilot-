package com.example.data.model

data class Subject(
    val id: String,
    val name: String,
    val colorHex: String,
    val examType: String,
    val completedChapters: Int,
    val totalChapters: Int
) {
    val progressPercent: Int
        get() = if (totalChapters > 0) ((completedChapters.toFloat() / totalChapters) * 100).toInt() else 0
}

enum class ChapterStatus {
    COMPLETED,
    IN_PROGRESS,
    NOT_STARTED
}

data class Chapter(
    val id: String,
    val subjectId: String,
    val number: Int,
    val title: String,
    val status: ChapterStatus
)

data class StudySession(
    val id: String,
    val subjectName: String,
    val colorHex: String,
    val chapterTitle: String,
    val timeRange: String,
    val date: String, // e.g. "2025-10-20"
    val isCompleted: Boolean
)

data class ExamGoal(
    val title: String,
    val dateFormatted: String,
    val daysLeft: Int,
    val syllabusProgress: Int
)

data class DailyPlanItem(
    val subjectName: String,
    val colorHex: String,
    val dailyAllocation: String
)

data class BlockedApp(
    val id: String,
    val name: String,
    val iconColor: Long,
    val isBlocked: Boolean
)

data class TestQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)

data class MockTest(
    val id: String,
    val title: String,
    val totalQuestions: Int,
    val durationMinutes: Int,
    val rating: Double,
    val category: String, // "Admission", "University", "Subject"
    val questions: List<TestQuestion>
)

data class NoteItem(
    val id: String,
    val title: String,
    val subject: String,
    val fileType: String = "PDF",
    val fileSize: String,
    val content: String = "",
    val isBookmarked: Boolean = false
)

data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val category: String, // "Study", "Reminder", "System"
    val isRead: Boolean = false
)

data class UserProfile(
    val name: String = "Rafi",
    val fullName: String = "Rafi Uddin",
    val email: String = "rafiuddin@gmail.com",
    val roleBadge: String = "Admission Candidate",
    val streakDays: Int = 12,
    val totalStudyHours: Int = 48,
    val syllabusProgress: Int = 78,
    val todayCompletedHours: Float = 3.0f,
    val todayTargetHours: Float = 6.0f
)
