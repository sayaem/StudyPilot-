package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    lang: String,
    onBack: () -> Unit,
    onNavigateLanguage: () -> Unit,
    onNavigateAboutData: () -> Unit,
    onNavigatePrivacy: () -> Unit,
    onNavigateTerms: () -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("settings", lang),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Column {
                        ProfileMenuItem(
                            icon = Icons.Outlined.Person,
                            title = "Account & Security",
                            onClick = {}
                        )
                        HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Brightness4,
                            title = AppStrings.get("appearance", lang),
                            trailingText = "Light",
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
                            icon = Icons.Outlined.Notifications,
                            title = AppStrings.get("notifications", lang),
                            onClick = {}
                        )
                        HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Storage,
                            title = AppStrings.get("data_storage", lang),
                            onClick = onNavigateAboutData
                        )
                        HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Policy,
                            title = AppStrings.get("privacy_policy", lang),
                            onClick = onNavigatePrivacy
                        )
                        HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Gavel,
                            title = AppStrings.get("terms_conditions", lang),
                            onClick = onNavigateTerms
                        )
                        HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

                        ProfileMenuItem(
                            icon = Icons.Outlined.Info,
                            title = AppStrings.get("about_studypilot", lang),
                            onClick = onNavigateAboutData
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageSelectionScreen(
    currentLang: String,
    onBack: () -> Unit,
    onSelectLanguage: (String) -> Unit
) {
    var selected by remember { mutableStateOf(currentLang) }

    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = if (selected == "bn") "ভাষা" else "Language",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // English Option Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selected = "en" }
                    .testTag("language_option_en"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (selected == "en") StudyPilotPrimary else StudyPilotBorder
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = StudyPilotPrimaryLight
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Language, contentDescription = "English", tint = StudyPilotPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "English", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudyPilotTextDark)
                        Text(text = "Default", fontSize = 12.sp, color = StudyPilotTextMuted)
                    }

                    if (selected == "en") {
                        Surface(
                            modifier = Modifier.size(24.dp),
                            shape = CircleShape,
                            color = StudyPilotPrimary
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Check, contentDescription = "Selected", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // Bangla Option Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selected = "bn" }
                    .testTag("language_option_bn"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (selected == "bn") StudyPilotPrimary else StudyPilotBorder
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = StudyPilotGreenBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Translate, contentDescription = "Bangla", tint = StudyPilotGreen)
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "বাংলা", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudyPilotTextDark)
                        Text(text = "সহজ এবং সাবলীল বাংলা ইন্টারফেস", fontSize = 12.sp, color = StudyPilotTextMuted)
                    }

                    if (selected == "bn") {
                        Surface(
                            modifier = Modifier.size(24.dp),
                            shape = CircleShape,
                            color = StudyPilotPrimary
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Check, contentDescription = "Selected", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onSelectLanguage(selected)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_language_button"),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary)
            ) {
                Text(
                    text = if (selected == "bn") "সংরক্ষণ করুন" else "Save",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun AboutDataScreen(
    lang: String,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("about_data", lang),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Text(text = "What we collect", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudyPilotTextDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "We only collect necessary study progress data, selected focus schedules, and syllabus completion states stored directly on your device.",
                                fontSize = 13.sp,
                                color = StudyPilotTextMuted,
                                lineHeight = 18.sp
                            )
                        }

                        HorizontalDivider(color = Color(0xFFF1F5F9))

                        Column {
                            Text(text = "How we use it", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudyPilotTextDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your data helps us personalize your daily study allocations, monitor exam countdowns, and build productive study streaks.",
                                fontSize = 13.sp,
                                color = StudyPilotTextMuted,
                                lineHeight = 18.sp
                            )
                        }

                        HorizontalDivider(color = Color(0xFFF1F5F9))

                        Column {
                            Text(text = "Your privacy matters", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudyPilotTextDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "We never share your personal information or study logs with third parties. All mock test records and syllabus checklists stay strictly private.",
                                fontSize = 13.sp,
                                color = StudyPilotTextMuted,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PolicyExpandableScreen(
    title: String,
    sections: List<Pair<String, String>>,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = title,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Last updated: 20 Oct 2025",
                    fontSize = 12.sp,
                    color = StudyPilotTextSubtle,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            items(sections.size) { index ->
                val (sectionTitle, sectionContent) = sections[index]
                var expanded by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sectionTitle,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotTextDark,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (expanded) Icons.Filled.ExpandMore else Icons.Filled.ChevronRight,
                                contentDescription = "Expand",
                                tint = StudyPilotTextSubtle
                            )
                        }

                        AnimatedVisibility(visible = expanded) {
                            Column {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = sectionContent,
                                    fontSize = 13.sp,
                                    color = StudyPilotTextBody,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
