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
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Subject
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun SubjectsScreen(
    subjects: List<Subject>,
    lang: String,
    onBack: () -> Unit,
    onSelectSubject: (String) -> Unit,
    onAddSubject: () -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("subjects", lang),
                onBack = onBack,
                actions = {
                    IconButton(
                        onClick = onAddSubject,
                        modifier = Modifier.testTag("subjects_add_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Subject",
                            tint = StudyPilotPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(subjects) { subject ->
                    val subjectColor = try {
                        Color(android.graphics.Color.parseColor(subject.colorHex))
                    } catch (e: Exception) {
                        StudyPilotPrimary
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSubject(subject.id) }
                            .testTag("subject_card_${subject.id}"),
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
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Subject icon in colored rounded box
                                Surface(
                                    modifier = Modifier.size(46.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = subjectColor.copy(alpha = 0.15f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.Book,
                                            contentDescription = subject.name,
                                            tint = subjectColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = subject.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StudyPilotTextDark
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${subject.completedChapters}/${subject.totalChapters} ${AppStrings.get("chapters", lang)}",
                                        fontSize = 12.sp,
                                        color = StudyPilotTextMuted
                                    )
                                }

                                Text(
                                    text = "${subject.progressPercent}%",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudyPilotTextDark
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Subtle progress bar
                            LinearProgressIndicator(
                                progress = { (subject.progressPercent / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = subjectColor,
                                trackColor = Color(0xFFF1F5F9)
                            )
                        }
                    }
                }
            }

            // Bottom "+ Add Subject" Outlined Action Button
            OutlinedButton(
                onClick = onAddSubject,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 8.dp)
                    .testTag("subjects_add_subject_bottom_btn"),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = StudyPilotPrimary
                ),
                border = ButtonDefaults.outlinedButtonBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(StudyPilotPrimary)
                )
            ) {
                Text(
                    text = AppStrings.get("add_subject", lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
