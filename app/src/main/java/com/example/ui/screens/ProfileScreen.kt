package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotBottomBar
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    profile: UserProfile,
    lang: String,
    onBack: () -> Unit,
    onNavigateLanguage: () -> Unit,
    onNavigateAboutData: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("profile", lang),
                onBack = onBack,
                actions = {
                    IconButton(onClick = onNavigateSettings) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = StudyPilotTextDark)
                    }
                }
            )
        },
        bottomBar = {
            StudyPilotBottomBar(
                currentRoute = "profile",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Header
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_rafi),
                            contentDescription = "Profile Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = profile.fullName,
                        fontSize = 18.sp,
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = StudyPilotTextDark
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = profile.email,
                        fontSize = 12.sp,
                        fontFamily = PlusJakartaSansFamily,
                        color = StudyPilotTextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = StudyPilotPrimaryLight
                    ) {
                        Text(
                            text = profile.roleBadge,
                            fontSize = 11.sp,
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = StudyPilotPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // 3 Stat Metric Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val metrics = listOf(
                        Triple("${profile.streakDays}", AppStrings.get("days_streak", lang), StudyPilotPrimary),
                        Triple("${profile.totalStudyHours}h", AppStrings.get("total_study", lang), StudyPilotCyan),
                        Triple("${profile.syllabusProgress}%", AppStrings.get("syllabus", lang), StudyPilotGreen)
                    )

                    metrics.forEach { (value, label, color) ->
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = value,
                                    fontSize = 18.sp,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = color
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontFamily = PlusJakartaSansFamily,
                                    color = StudyPilotTextMuted
                                )
                            }
                        }
                    }
                }
            }

            // Menu List
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Column {
                        ProfileMenuItem(
                            icon = Icons.Outlined.Flag,
                            title = AppStrings.get("my_goals", lang),
                            onClick = {}
                        )
                        HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.TrendingUp,
                            title = AppStrings.get("study_stats", lang),
                            onClick = {}
                        )
                        HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Language,
                            title = AppStrings.get("language", lang),
                            trailingText = if (lang == "bn") "বাংলা" else "English",
                            onClick = onNavigateLanguage
                        )
                        HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Info,
                            title = AppStrings.get("about_data", lang),
                            onClick = onNavigateAboutData
                        )
                        HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.HelpOutline,
                            title = AppStrings.get("help_support", lang),
                            onClick = {}
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

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    trailingText: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = StudyPilotTextMuted,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontFamily = PlusJakartaSansFamily,
            fontWeight = FontWeight.Medium,
            color = StudyPilotTextDark,
            modifier = Modifier.weight(1f)
        )
        if (trailingText != null) {
            Text(
                text = trailingText,
                fontSize = 12.sp,
                fontFamily = PlusJakartaSansFamily,
                color = StudyPilotTextMuted
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "Open",
            tint = StudyPilotTextSubtle,
            modifier = Modifier.size(18.dp)
        )
    }
}
