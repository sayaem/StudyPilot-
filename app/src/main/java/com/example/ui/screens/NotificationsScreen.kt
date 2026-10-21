package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationItem
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    lang: String,
    onBack: () -> Unit,
    onMarkRead: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("notifications", lang),
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
            // Filter Pills: All, Study, Reminder, System
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Study", "Reminder", "System").forEach { cat ->
                    val isSelected = selectedCategory == cat
                    val localized = when (cat) {
                        "All" -> AppStrings.get("all", lang)
                        "Study" -> AppStrings.get("study", lang)
                        "Reminder" -> AppStrings.get("reminder", lang)
                        else -> AppStrings.get("system", lang)
                    }
                    Surface(
                        modifier = Modifier
                            .clickable { selectedCategory = cat }
                            .testTag("notification_filter_$cat"),
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

            // Notification Cards
            val filtered = if (selectedCategory == "All") notifications else notifications.filter { it.category == selectedCategory }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { item ->
                    val (icon, iconColor, iconBg) = when (item.title) {
                        "Exam Countdown" -> Triple(Icons.Outlined.Warning, StudyPilotRed, StudyPilotRedBg)
                        "Focus Completed" -> Triple(Icons.Outlined.CheckCircle, StudyPilotGreen, StudyPilotGreenBg)
                        "New Mock Test" -> Triple(Icons.Outlined.Quiz, StudyPilotPrimary, StudyPilotPrimaryLight)
                        "Tips for You" -> Triple(Icons.Outlined.Lightbulb, StudyPilotOrange, StudyPilotOrangeBg)
                        else -> Triple(Icons.Outlined.Notifications, StudyPilotPrimary, StudyPilotPrimaryLight)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMarkRead(item.id) }
                            .testTag("notification_item_${item.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                modifier = Modifier.size(38.dp),
                                shape = CircleShape,
                                color = iconBg
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = item.title,
                                        tint = iconColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudyPilotTextDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.description,
                                    fontSize = 12.sp,
                                    color = StudyPilotTextMuted,
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = item.timeAgo,
                                fontSize = 11.sp,
                                color = StudyPilotTextSubtle
                            )
                        }
                    }
                }
            }
        }
    }
}
