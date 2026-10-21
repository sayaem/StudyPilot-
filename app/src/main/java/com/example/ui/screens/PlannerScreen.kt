package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudySession
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun PlannerScreen(
    sessions: List<StudySession>,
    lang: String,
    onBack: () -> Unit,
    onToggleSession: (String) -> Unit,
    onNavigateCalendar: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Today") }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("study_planner", lang),
                onBack = onBack,
                actions = {
                    IconButton(
                        onClick = onNavigateCalendar,
                        modifier = Modifier.testTag("planner_calendar_icon")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = "Calendar",
                            tint = StudyPilotPrimary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = StudyPilotPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("planner_add_session_fab")
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Session")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // View Filters (Today, Week, Month)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Today", "Week", "Month").forEach { tab ->
                        val isSelected = selectedTab == tab
                        val localizedTab = when (tab) {
                            "Today" -> AppStrings.get("today", lang)
                            "Week" -> AppStrings.get("week", lang)
                            else -> AppStrings.get("month", lang)
                        }
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clickable { selectedTab = tab }
                                .testTag("planner_tab_$tab"),
                            shape = RoundedCornerShape(19.dp),
                            color = if (isSelected) StudyPilotPrimary else Color.White,
                            border = if (isSelected) null else CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = localizedTab,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else StudyPilotTextMuted
                                )
                            }
                        }
                    }
                }
            }

            // Date Navigation Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mon, 20 Oct 2025",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudyPilotTextDark
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.ChevronLeft, contentDescription = "Previous Date", tint = StudyPilotTextMuted)
                        }
                        IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.ChevronRight, contentDescription = "Next Date", tint = StudyPilotTextMuted)
                        }
                    }
                }
            }

            // Session Cards
            items(sessions) { session ->
                val cardColor = Color.White
                val subjectColor = try {
                    Color(android.graphics.Color.parseColor(session.colorHex))
                } catch (e: Exception) {
                    StudyPilotPrimary
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleSession(session.id) }
                        .testTag("session_item_${session.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Colored Circle Icon
                        Surface(
                            modifier = Modifier.size(42.dp),
                            shape = CircleShape,
                            color = subjectColor.copy(alpha = 0.12f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Book,
                                    contentDescription = session.subjectName,
                                    tint = subjectColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = session.timeRange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = StudyPilotTextSubtle
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = session.subjectName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotTextDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = session.chapterTitle,
                                fontSize = 12.sp,
                                color = StudyPilotTextMuted
                            )
                        }

                        // Completion indicator
                        IconButton(
                            onClick = { onToggleSession(session.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            if (session.isCompleted) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(StudyPilotPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Completed",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .clip(CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        modifier = Modifier.size(24.dp),
                                        shape = CircleShape,
                                        color = Color.Transparent,
                                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFCBD5E1)))
                                    ) {}
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
