package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BlockedApp
import com.example.ui.components.AppStrings
import com.example.ui.components.StudyPilotTopBar
import com.example.ui.theme.*

@Composable
fun FocusModeIdleScreen(
    lang: String,
    onStartFocus: () -> Unit,
    onNavigateAppsBlocker: () -> Unit,
    onNavigateSettings: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = "Back", tint = StudyPilotTextDark)
                }
            }

            Text(
                text = AppStrings.get("focus_mode", lang),
                fontSize = 22.sp,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Bold,
                color = StudyPilotTextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = AppStrings.get("focus_subtitle", lang),
                fontSize = 13.sp,
                fontFamily = PlusJakartaSansFamily,
                color = StudyPilotTextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Illustrated Mountain Landscape with Circular Timer overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // High-Resolution Mountain, Golden Sunrise & Pine Forest Landscape
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .align(Alignment.TopCenter)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_focus_landscape),
                        contentDescription = "Scenic Focus Mountains",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Atmospheric top sky blend
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(StudyPilotBg, StudyPilotBg.copy(alpha = 0.5f), Color.Transparent)
                                )
                            )
                    )

                    // Soft bottom feathered blend to naturally transition forest & water
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, StudyPilotBg.copy(alpha = 0.6f), StudyPilotBg)
                                )
                            )
                    )
                }

                // Refined Large Circular Timer Card overlapping the landscape
                Surface(
                    modifier = Modifier
                        .size(215.dp)
                        .align(Alignment.Center)
                        .shadow(16.dp, CircleShape, spotColor = Color(0x181E3A8A)),
                    shape = CircleShape,
                    color = Color.White,
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                            .border(
                                6.dp,
                                Brush.sweepGradient(listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD), Color(0xFFE0F2FE))),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "25:00",
                                fontSize = 36.sp,
                                fontFamily = PlusJakartaSansFamily,
                                fontWeight = FontWeight.ExtraBold,
                                color = StudyPilotTextDark,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = AppStrings.get("focus_session", lang),
                                fontSize = 13.sp,
                                fontFamily = PlusJakartaSansFamily,
                                fontWeight = FontWeight.Medium,
                                color = StudyPilotTextMuted
                            )
                        }
                    }
                }
            }

            // Start Focus Button matching reference
            Button(
                onClick = onStartFocus,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp)
                    .testTag("start_focus_button"),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Start Focus",
                    fontSize = 15.sp,
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Quick Options: Custom, Apps Blocker, Settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onStartFocus() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = "Custom",
                        tint = StudyPilotTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AppStrings.get("custom", lang),
                        fontSize = 11.sp,
                        fontFamily = PlusJakartaSansFamily,
                        color = StudyPilotTextMuted
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onNavigateAppsBlocker() }
                        .testTag("focus_apps_blocker_shortcut")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Security,
                        contentDescription = "Apps Blocker",
                        tint = StudyPilotTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AppStrings.get("apps_blocker", lang),
                        fontSize = 11.sp,
                        fontFamily = PlusJakartaSansFamily,
                        color = StudyPilotTextMuted
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigateSettings() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = StudyPilotTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AppStrings.get("settings", lang),
                        fontSize = 11.sp,
                        fontFamily = PlusJakartaSansFamily,
                        color = StudyPilotTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun FocusRunningScreen(
    remainingSeconds: Int,
    isRunning: Boolean,
    lang: String,
    onPauseResume: () -> Unit,
    onEndSession: () -> Unit,
    onNavigateAppsBlocker: () -> Unit
) {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        containerColor = FocusDarkBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEndSession) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Text(
                text = AppStrings.get("focus_time", lang),
                fontSize = 18.sp,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = AppStrings.get("keep_going", lang),
                fontSize = 13.sp,
                fontFamily = PlusJakartaSansFamily,
                color = StudyPilotCyan,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // Large Glowing Dark Circular Timer
            Surface(
                modifier = Modifier
                    .size(240.dp)
                    .shadow(24.dp, CircleShape, spotColor = StudyPilotCyan.copy(alpha = 0.4f)),
                shape = CircleShape,
                color = FocusDarkSurface,
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FocusDarkBorder))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .border(
                            4.dp,
                            Brush.sweepGradient(listOf(StudyPilotCyan, StudyPilotPrimary, FocusDarkSurface)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = formattedTime,
                            fontSize = 38.sp,
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        IconButton(
                            onClick = onPauseResume,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .testTag("focus_pause_resume_button")
                        ) {
                            Icon(
                                imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = if (isRunning) "Pause" else "Resume",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.7f))

            // Session Stats Row: This Session / Today
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = FocusDarkCard,
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FocusDarkBorder))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppStrings.get("this_session", lang),
                            fontSize = 11.sp,
                            fontFamily = PlusJakartaSansFamily,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "0h 27m",
                            fontSize = 16.sp,
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = FocusDarkCard,
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FocusDarkBorder))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppStrings.get("today", lang),
                            fontSize = 11.sp,
                            fontFamily = PlusJakartaSansFamily,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "2h 15m",
                            fontSize = 16.sp,
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Blocked Apps Pill Card at Bottom
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateAppsBlocker() }
                    .testTag("focus_blocked_apps_pill"),
                shape = RoundedCornerShape(16.dp),
                color = FocusDarkCard,
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FocusDarkBorder))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = StudyPilotPrimary.copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Lock",
                                tint = StudyPilotCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.get("blocked_apps", lang),
                            fontSize = 11.sp,
                            fontFamily = PlusJakartaSansFamily,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "12 ${AppStrings.get("apps_blocked", lang)}",
                            fontSize = 13.sp,
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Details",
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun AppsBlockerScreen(
    blockedApps: List<BlockedApp>,
    isBlockingEnabled: Boolean,
    blockTiming: String,
    lang: String,
    onBack: () -> Unit,
    onToggleEnable: (Boolean) -> Unit,
    onToggleApp: (String) -> Unit,
    onSelectTiming: (String) -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg,
        topBar = {
            StudyPilotTopBar(
                title = AppStrings.get("apps_blocker", lang),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = AppStrings.get("enable_app_blocking", lang),
                            fontSize = 15.sp,
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = StudyPilotTextDark
                        )
                        Switch(
                            checked = isBlockingEnabled,
                            onCheckedChange = onToggleEnable,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = StudyPilotPrimary
                            ),
                            modifier = Modifier.testTag("enable_app_blocking_switch")
                        )
                    }
                }
            }

            item {
                Text(
                    text = AppStrings.get("blocked_apps", lang),
                    fontSize = 13.sp,
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                    color = StudyPilotTextMuted
                )
            }

            items(blockedApps) { app ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(app.iconColor).copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.Apps,
                                    contentDescription = app.name,
                                    tint = Color(app.iconColor),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = app.name,
                            fontSize = 14.sp,
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = StudyPilotTextDark,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = app.isBlocked && isBlockingEnabled,
                            onCheckedChange = { onToggleApp(app.id) },
                            enabled = isBlockingEnabled,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = StudyPilotPrimary
                            )
                        )
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = AppStrings.get("block_during", lang),
                        fontSize = 13.sp,
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = StudyPilotTextMuted
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        listOf("Focus Session", "Custom Time").forEach { timing ->
                            val isSelected = blockTiming == timing
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { onSelectTiming(timing) }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onSelectTiming(timing) },
                                    colors = RadioButtonDefaults.colors(selectedColor = StudyPilotPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (timing == "Focus Session") AppStrings.get("focus_session", lang) else AppStrings.get("custom", lang),
                                    fontSize = 13.sp,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = StudyPilotTextDark
                                )
                            }
                        }
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = StudyPilotPrimaryLight.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = StudyPilotPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Apps Blocker works during active Focus sessions to prevent distracting apps from interrupting your study routine.",
                            fontSize = 12.sp,
                            fontFamily = PlusJakartaSansFamily,
                            color = StudyPilotTextBody,
                            lineHeight = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
