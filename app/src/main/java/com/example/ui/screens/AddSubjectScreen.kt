package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Book
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
fun AddSubjectScreen(
    lang: String,
    onBack: () -> Unit,
    onSave: (name: String, colorHex: String, examType: String) -> Unit
) {
    var subjectName by remember { mutableStateOf("") }
    val colorOptions = listOf(
        "#06B6D4", // Cyan
        "#10B981", // Green
        "#EF4444", // Red/Coral
        "#F59E0B", // Yellow
        "#8B5CF6", // Purple
        "#3B82F6"  // Blue
    )
    var selectedColor by remember { mutableStateOf(colorOptions[1]) }
    val examTypes = listOf("Admission", "HSC", "SSC", "Other")
    var selectedExamType by remember { mutableStateOf("Admission") }

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("add_subject", lang).replace("+ ", ""),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Icon Selector Card
            Surface(
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.Start),
                shape = RoundedCornerShape(16.dp),
                color = Color(android.graphics.Color.parseColor(selectedColor)).copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Book,
                        contentDescription = "Subject Icon",
                        tint = Color(android.graphics.Color.parseColor(selectedColor)),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Subject Name Input
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = AppStrings.get("subject_name", lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotTextDark
                )
                OutlinedTextField(
                    value = subjectName,
                    onValueChange = { subjectName = it },
                    placeholder = { Text(AppStrings.get("subject_name_hint", lang), color = StudyPilotTextSubtle) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_subject_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = StudyPilotPrimary,
                        unfocusedBorderColor = StudyPilotBorder
                    ),
                    singleLine = true
                )
            }

            // Color Selector
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = AppStrings.get("color", lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotTextDark
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colorOptions.forEach { hex ->
                        val isSelected = selectedColor == hex
                        val c = Color(android.graphics.Color.parseColor(hex))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(c)
                                .clickable { selectedColor = hex }
                                .then(
                                    if (isSelected) Modifier.border(2.dp, Color.White, CircleShape)
                                    else Modifier
                                )
                                .testTag("color_option_$hex"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Exam Type Pills
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = AppStrings.get("exam_type", lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotTextDark
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    examTypes.forEach { type ->
                        val isSelected = selectedExamType == type
                        Surface(
                            modifier = Modifier
                                .clickable { selectedExamType = type }
                                .testTag("exam_type_$type"),
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) StudyPilotPrimaryLight else Color.White,
                            border = if (isSelected) {
                                CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotPrimary))
                            } else {
                                CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                            }
                        ) {
                            Text(
                                text = type,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) StudyPilotPrimary else StudyPilotTextMuted,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = {
                    if (subjectName.isNotBlank()) {
                        onSave(subjectName, selectedColor, selectedExamType)
                    }
                },
                enabled = subjectName.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_subject_button"),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary)
            ) {
                Text(
                    text = AppStrings.get("save", lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
