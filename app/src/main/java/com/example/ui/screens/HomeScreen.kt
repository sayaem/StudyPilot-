package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    profile: UserProfile,
    lang: String,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg,
        bottomBar = {
            StudyPilotBottomBar(
                currentRoute = "home",
                lang = lang,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 16.dp)
        ) {
            // Top Header: Greeting & Profile
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${AppStrings.get("good_morning", lang)} ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotTextDark
                            )
                            Icon(
                                imageVector = Icons.Filled.WbSunny,
                                contentDescription = "Morning",
                                tint = StudyPilotYellow,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = profile.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StudyPilotTextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppStrings.get("home_subtitle", lang),
                            fontSize = 12.sp,
                            color = StudyPilotTextMuted
                        )
                    }

                    // Notification Bell and Avatar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { onNavigate("notifications") },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("home_notifications_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = StudyPilotTextDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .clickable { onNavigate("profile") }
                                .testTag("home_avatar_button")
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_rafi),
                                contentDescription = "Profile",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Online indicator dot
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(StudyPilotGreen)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                    }
                }
            }

            // Today's Study Progress Card
            item {
                CircularProgressCard(
                    currentHours = profile.todayCompletedHours,
                    targetHours = profile.todayTargetHours,
                    lang = lang
                )
            }

            // Quick Actions Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = AppStrings.get("quick_actions", lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudyPilotTextDark
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionButton(
                            label = AppStrings.get("action_plan", lang),
                            icon = Icons.Outlined.Assignment,
                            iconColor = StudyPilotPrimary,
                            bgColor = StudyPilotPrimaryLight,
                            onClick = { onNavigate("smart_planner") }
                        )

                        QuickActionButton(
                            label = AppStrings.get("action_focus", lang),
                            icon = Icons.Outlined.Timer,
                            iconColor = StudyPilotCyan,
                            bgColor = StudyPilotCyanBg,
                            onClick = { onNavigate("focus") }
                        )

                        QuickActionButton(
                            label = AppStrings.get("action_calendar", lang),
                            icon = Icons.Outlined.CalendarMonth,
                            iconColor = StudyPilotPurple,
                            bgColor = StudyPilotPurpleBg,
                            onClick = { onNavigate("calendar") }
                        )

                        QuickActionButton(
                            label = AppStrings.get("action_mock", lang),
                            icon = Icons.Outlined.Quiz,
                            iconColor = StudyPilotOrange,
                            bgColor = StudyPilotOrangeBg,
                            onClick = { onNavigate("mock_tests") }
                        )
                    }
                }
            }

            // Upcoming Exam Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = AppStrings.get("upcoming", lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudyPilotTextDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigate("smart_planner") }
                            .testTag("upcoming_exam_card"),
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
                                color = StudyPilotRedBg
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.Description,
                                        contentDescription = "Exam",
                                        tint = StudyPilotRed,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "RU Admission Test",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudyPilotTextDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Nov 10, 2025",
                                    fontSize = 12.sp,
                                    color = StudyPilotTextMuted
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = StudyPilotRedBg
                            ) {
                                Text(
                                    text = "12 ${AppStrings.get("days_left", lang)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudyPilotRed,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
