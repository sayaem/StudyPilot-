package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MockTest
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotBottomBar
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun MockTestsScreen(
    tests: List<MockTest>,
    lang: String,
    onBack: () -> Unit,
    onSelectTest: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("mock_tests", lang),
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search", tint = StudyPilotTextDark)
                    }
                }
            )
        },
        bottomBar = {
            StudyPilotBottomBar(
                currentRoute = "mock_tests",
                lang = lang,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // Filter Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Admission", "University", "Subject").forEach { cat ->
                    val isSelected = selectedCategory == cat
                    val localized = when (cat) {
                        "All" -> AppStrings.get("all", lang)
                        "Admission" -> AppStrings.get("admission", lang)
                        "University" -> AppStrings.get("university", lang)
                        else -> AppStrings.get("subject", lang)
                    }
                    Surface(
                        modifier = Modifier
                            .clickable { selectedCategory = cat }
                            .testTag("test_filter_$cat"),
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) StudyPilotPrimary else Color.White,
                        border = if (isSelected) null else CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                    ) {
                        Text(
                            text = localized,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else StudyPilotTextMuted,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            // Test Cards List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val filtered = if (selectedCategory == "All") tests else tests.filter { it.category == selectedCategory }
                items(filtered) { test ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectTest(test.id) }
                            .testTag("mock_test_${test.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = StudyPilotPrimaryLight
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.Assignment,
                                        contentDescription = test.title,
                                        tint = StudyPilotPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = test.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudyPilotTextDark
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "${test.totalQuestions} MCQ  |  ${test.durationMinutes / 60}h  |  ★ ${test.rating}",
                                    fontSize = 12.sp,
                                    color = StudyPilotTextMuted
                                )
                            }

                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = "Start Test",
                                tint = StudyPilotTextSubtle
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TestTakingScreen(
    test: MockTest,
    questionIndex: Int,
    selectedAnswers: Map<Int, Int>,
    lang: String,
    onBack: () -> Unit,
    onSelectOption: (Int, Int) -> Unit,
    onClearOption: (Int) -> Unit,
    onNext: () -> Unit
) {
    val questions = test.questions
    val currentQ = questions.getOrNull(questionIndex) ?: questions.first()
    val selectedOption = selectedAnswers[questionIndex]

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = test.title,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // Header: Question Counter & Timer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Q ${questionIndex + 1} / ${questions.size}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotTextDark
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = "Timer",
                        tint = StudyPilotGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "01:28:32",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudyPilotGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = currentQ.question,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StudyPilotTextDark,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Options List
            val optionPrefixes = listOf("A", "B", "C", "D")
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                currentQ.options.forEachIndexed { optIndex, optionText ->
                    val isChosen = selectedOption == optIndex
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectOption(questionIndex, optIndex) }
                            .testTag("test_option_$optIndex"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isChosen) StudyPilotGreenBg else Color.White
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(
                                if (isChosen) StudyPilotGreen else StudyPilotBorder
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Option prefix circle
                            Surface(
                                modifier = Modifier.size(28.dp),
                                shape = CircleShape,
                                color = if (isChosen) StudyPilotGreen else Color(0xFFF1F5F9)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (isChosen) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    } else {
                                        Text(
                                            text = optionPrefixes[optIndex],
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StudyPilotTextMuted
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "${optionPrefixes[optIndex]}. $optionText",
                                fontSize = 14.sp,
                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                color = if (isChosen) StudyPilotTextDark else StudyPilotTextBody
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Actions: Clear & Next
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onClearOption(questionIndex) },
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder)),
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(
                        text = AppStrings.get("clear", lang),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StudyPilotTextMuted
                    )
                }

                Button(
                    onClick = onNext,
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary),
                    modifier = Modifier.width(120.dp).testTag("test_next_button")
                ) {
                    Text(
                        text = if (questionIndex == questions.size - 1) AppStrings.get("save", lang) else AppStrings.get("next", lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun TestResultScreen(
    score: Int,
    correct: Int,
    incorrect: Int,
    timeTakenSeconds: Int,
    lang: String,
    onBack: () -> Unit,
    onReviewAnswers: () -> Unit,
    onTryAgain: () -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("test_result", lang),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Circular Score Card (68% Correct Answers)
            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.size(150.dp)) {
                    val stroke = 12.dp.toPx()
                    drawCircle(color = Color(0xFFF1F5F9), style = Stroke(stroke))
                    drawArc(
                        color = StudyPilotGreen,
                        startAngle = -90f,
                        sweepAngle = 360f * (score / 100f),
                        useCenter = false,
                        style = Stroke(stroke, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$score%",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StudyPilotGreen
                    )
                    Text(
                        text = AppStrings.get("correct_answers", lang),
                        fontSize = 11.sp,
                        color = StudyPilotTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Stats Row: 68/100 Score, 42 Correct, 18 Incorrect
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$score / 100", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StudyPilotTextDark)
                    Text(text = AppStrings.get("score", lang), fontSize = 11.sp, color = StudyPilotTextMuted)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$correct", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StudyPilotCyan)
                    Text(text = AppStrings.get("correct", lang), fontSize = 11.sp, color = StudyPilotTextMuted)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$incorrect", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StudyPilotRed)
                    Text(text = AppStrings.get("incorrect", lang), fontSize = 11.sp, color = StudyPilotTextMuted)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Time Taken
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = "Time",
                    tint = StudyPilotCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${AppStrings.get("time_taken", lang)}: 01:45:32",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = StudyPilotTextDark
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Review Answers & Try Again Buttons
            Button(
                onClick = onReviewAnswers,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("review_answers_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary)
            ) {
                Text(
                    text = AppStrings.get("review_answers", lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onTryAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("try_again_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
            ) {
                Text(
                    text = AppStrings.get("try_again", lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotTextDark
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
