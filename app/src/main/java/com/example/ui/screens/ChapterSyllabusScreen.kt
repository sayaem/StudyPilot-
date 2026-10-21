package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import com.example.data.model.Chapter
import com.example.data.model.ChapterStatus
import com.example.data.model.Subject
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun ChapterSyllabusScreen(
    subject: Subject,
    chapters: List<Chapter>,
    lang: String,
    onBack: () -> Unit,
    onToggleStatus: (String) -> Unit,
    onAddChapter: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("Chapters") }
    var showAddDialog by remember { mutableStateOf(false) }
    var newChapterTitle by remember { mutableStateOf("") }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(AppStrings.get("add_chapter", lang), fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newChapterTitle,
                    onValueChange = { newChapterTitle = it },
                    placeholder = { Text(AppStrings.get("chapter_name_hint", lang)) },
                    singleLine = true,
                    modifier = Modifier.testTag("add_chapter_input")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newChapterTitle.isNotBlank()) {
                            onAddChapter(newChapterTitle)
                            newChapterTitle = ""
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary)
                ) {
                    Text(AppStrings.get("save", lang))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(AppStrings.get("clear", lang))
                }
            }
        )
    }

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = subject.name,
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
            // Tabs: Chapters, Syllabus, Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf("Chapters", "Syllabus", "Stats").forEach { tab ->
                    val isSelected = selectedTab == tab
                    val label = when (tab) {
                        "Chapters" -> AppStrings.get("chapters", lang)
                        "Syllabus" -> AppStrings.get("syllabus", lang)
                        else -> AppStrings.get("stats", lang)
                    }
                    Column(
                        modifier = Modifier
                            .clickable { selectedTab = tab }
                            .padding(bottom = 6.dp)
                            .testTag("chapter_tab_$tab")
                    ) {
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) StudyPilotPrimary else StudyPilotTextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .width(28.dp)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(1.5.dp))
                                    .background(StudyPilotPrimary)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chapter list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(chapters) { chapter ->
                    val (statusColor, statusBg, statusText) = when (chapter.status) {
                        ChapterStatus.COMPLETED -> Triple(StudyPilotGreen, StudyPilotGreenBg, AppStrings.get("completed", lang))
                        ChapterStatus.IN_PROGRESS -> Triple(StudyPilotPrimary, StudyPilotPrimaryLight, AppStrings.get("in_progress", lang))
                        ChapterStatus.NOT_STARTED -> Triple(StudyPilotTextSubtle, Color(0xFFF1F5F9), AppStrings.get("not_started", lang))
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleStatus(chapter.id) }
                            .testTag("chapter_item_${chapter.id}"),
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
                            // Circular Chapter Icon / Index
                            Surface(
                                modifier = Modifier.size(38.dp),
                                shape = CircleShape,
                                color = statusBg
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${chapter.number}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = statusColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Chapter ${chapter.number}: ${chapter.title}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudyPilotTextDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = statusText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = statusColor
                                )
                            }

                            // Status Indicator Checkmark / Circle
                            if (chapter.status == ChapterStatus.COMPLETED) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(StudyPilotGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Completed",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            } else {
                                Surface(
                                    modifier = Modifier.size(22.dp),
                                    shape = CircleShape,
                                    color = Color.Transparent,
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(
                                            if (chapter.status == ChapterStatus.IN_PROGRESS) StudyPilotPrimary else Color(0xFFCBD5E1)
                                        )
                                    )
                                ) {}
                            }
                        }
                    }
                }
            }

            // Bottom "+ Add Chapter" Action
            OutlinedButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(bottom = 8.dp)
                    .testTag("add_chapter_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = StudyPilotPrimary
                ),
                border = ButtonDefaults.outlinedButtonBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(StudyPilotPrimary)
                )
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = AppStrings.get("add_chapter", lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
