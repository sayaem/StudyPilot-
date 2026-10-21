package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudySession
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotBottomBar
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun CalendarScreen(
    sessions: List<StudySession>,
    lang: String,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var selectedDay by remember { mutableStateOf(20) }

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("nav_calendar", lang),
                onBack = onBack
            )
        },
        bottomBar = {
            StudyPilotBottomBar(
                currentRoute = "calendar",
                lang = lang,
                onNavigate = onNavigate
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
            // Month Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "October 2025",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudyPilotTextDark
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.ChevronLeft, contentDescription = "Prev", tint = StudyPilotTextMuted)
                        }
                        IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.ChevronRight, contentDescription = "Next", tint = StudyPilotTextMuted)
                        }
                    }
                }
            }

            // Calendar Grid Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Weekday headers: Sun Mon Tue Wed Thu Fri Sat
                        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                        Row(modifier = Modifier.fillMaxWidth()) {
                            daysOfWeek.forEach { dayName ->
                                Text(
                                    text = dayName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = StudyPilotTextSubtle,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // October 2025 starts on Wednesday (offset 3)
                        val totalDays = 31
                        val startOffset = 3 // Sun=0, Mon=1, Tue=2, Wed=3
                        val totalCells = totalDays + startOffset
                        val numRows = (totalCells + 6) / 7

                        for (row in 0 until numRows) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (col in 0..6) {
                                    val cellIndex = row * 7 + col
                                    val dayNum = cellIndex - startOffset + 1
                                    if (dayNum in 1..totalDays) {
                                        val isSelected = dayNum == selectedDay
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(if (isSelected) StudyPilotPrimary else Color.Transparent)
                                                .clickable { selectedDay = dayNum }
                                                .testTag("calendar_day_$dayNum"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "$dayNum",
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color.White else StudyPilotTextDark
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.size(34.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Schedule for Selected Day
            item {
                Text(
                    text = "${AppStrings.get("today", lang)}, $selectedDay Oct",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotTextDark
                )
            }

            items(sessions.take(3)) { session ->
                val subjectColor = try {
                    Color(android.graphics.Color.parseColor(session.colorHex))
                } catch (e: Exception) {
                    StudyPilotPrimary
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = subjectColor.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Book,
                                    contentDescription = session.subjectName,
                                    tint = subjectColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = session.subjectName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudyPilotTextDark,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = session.timeRange,
                            fontSize = 12.sp,
                            color = StudyPilotTextMuted
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
