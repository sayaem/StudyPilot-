package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun SmartPlannerScreen(
    lang: String,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Plan") }
    var planGeneratedText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("smart_planner", lang),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Plan / Remaining Pills
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Plan", "Remaining").forEach { tab ->
                        val isSelected = selectedTab == tab
                        val label = if (tab == "Plan") AppStrings.get("plan", lang) else AppStrings.get("remaining", lang)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clickable { selectedTab = tab }
                                .testTag("smart_planner_tab_$tab"),
                            shape = RoundedCornerShape(19.dp),
                            color = if (isSelected) StudyPilotPrimary else Color.White,
                            border = if (isSelected) null else CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else StudyPilotTextMuted
                                )
                            }
                        }
                    }
                }
            }

            // Your Exam Card
            item {
                Text(
                    text = AppStrings.get("your_exam", lang),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotTextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = StudyPilotPrimaryLight
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Description,
                                    contentDescription = "Exam",
                                    tint = StudyPilotPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "RU Admission Test",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotTextDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Nov 10, 2025",
                                    fontSize = 12.sp,
                                    color = StudyPilotTextMuted
                                )
                                Text(
                                    text = "  |  ",
                                    fontSize = 12.sp,
                                    color = StudyPilotTextSubtle
                                )
                                Text(
                                    text = "12 ${AppStrings.get("days_left", lang)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudyPilotRed
                                )
                            }
                        }
                    }
                }
            }

            // Syllabus Progress Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.get("syllabus_progress", lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotTextDark
                            )
                            Text(
                                text = "42%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { 0.42f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = StudyPilotCyan,
                            trackColor = Color(0xFFF1F5F9)
                        )
                    }
                }
            }

            // Recommended Plan Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.get("recommended_plan", lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotTextDark
                            )
                            Text(
                                text = AppStrings.get("next_7_days", lang),
                                fontSize = 12.sp,
                                color = StudyPilotPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val planItems = listOf(
                            Triple("Physics", "#06B6D4", "4h 30m/day"),
                            Triple("Mathematics", "#3B82F6", "4h 0m/day"),
                            Triple("Chemistry", "#F97316", "3h 30m/day")
                        )

                        planItems.forEachIndexed { index, (name, hex, time) ->
                            val color = Color(android.graphics.Color.parseColor(hex))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(34.dp),
                                    shape = CircleShape,
                                    color = color.copy(alpha = 0.15f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.Book,
                                            contentDescription = name,
                                            tint = color,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = StudyPilotTextDark,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = time,
                                    fontSize = 13.sp,
                                    color = StudyPilotTextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            if (index < planItems.size - 1) {
                                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                            }
                        }
                    }
                }
            }

            if (planGeneratedText.isNotEmpty()) {
                item {
                    Text(
                        text = planGeneratedText,
                        fontSize = 12.sp,
                        color = StudyPilotGreen,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            // Generate New Plan Button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        planGeneratedText = "Plan refreshed! Allocations optimized for upcoming RU exam."
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("generate_new_plan_button"),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary)
                ) {
                    Text(
                        text = AppStrings.get("generate_new_plan", lang),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
