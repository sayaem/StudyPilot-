package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun StudyPilotTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("top_bar_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = StudyPilotTextDark
                )
            }
        } else {
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = StudyPilotTextDark,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        actions()
    }
}

@Composable
fun StudyPilotBottomBar(
    currentRoute: String,
    lang: String,
    onNavigate: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, spotColor = Color(0x1A000000)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                Triple("home", AppStrings.get("nav_home", lang), Icons.Filled.Home),
                Triple("planner", AppStrings.get("nav_planner", lang), Icons.Outlined.CalendarToday),
                Triple("focus", AppStrings.get("nav_focus", lang), Icons.Outlined.Timer),
                Triple("calendar", AppStrings.get("nav_calendar", lang), Icons.Outlined.DateRange),
                Triple("more", AppStrings.get("nav_more", lang), Icons.Outlined.Menu)
            )

            items.forEach { (route, label, icon) ->
                val isSelected = currentRoute == route
                val tint = if (isSelected) StudyPilotPrimary else StudyPilotTextSubtle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onNavigate(route) }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .testTag("bottom_nav_$route")
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = tint
                    )
                }
            }
        }
    }
}

@Composable
fun CircularProgressCard(
    currentHours: Float,
    targetHours: Float,
    lang: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(StudyPilotBorder, StudyPilotBorder)))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = AppStrings.get("today_progress", lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = StudyPilotTextDark
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Ring Indicator
                Box(
                    modifier = Modifier.size(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val progress = (currentHours / targetHours).coerceIn(0f, 1f)
                    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

                    Canvas(modifier = Modifier.size(95.dp)) {
                        val strokeWidth = 9.dp.toPx()
                        // Background track
                        drawCircle(
                            color = Color(0xFFF1F5F9),
                            style = Stroke(strokeWidth)
                        )
                        // Progress arc
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(StudyPilotCyan, StudyPilotGreen, StudyPilotPrimary)
                            ),
                            startAngle = -90f,
                            sweepAngle = 360f * animatedProgress,
                            useCenter = false,
                            style = Stroke(strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${currentHours.toInt()}/${targetHours.toInt()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudyPilotTextDark
                        )
                        Text(
                            text = AppStrings.get("hours", lang),
                            fontSize = 11.sp,
                            color = StudyPilotTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.width(24.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(StudyPilotGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = AppStrings.get("completed", lang),
                                fontSize = 12.sp,
                                color = StudyPilotTextMuted
                            )
                            Text(
                                text = "${currentHours.toInt()}h 0m",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotTextDark
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(StudyPilotCyan)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = AppStrings.get("target", lang),
                                fontSize = 12.sp,
                                color = StudyPilotTextMuted
                            )
                            Text(
                                text = "${targetHours.toInt()}h 0m",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotTextDark
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    label: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag("quick_action_$label")
    ) {
        Surface(
            modifier = Modifier.size(62.dp),
            shape = RoundedCornerShape(16.dp),
            color = bgColor,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(StudyPilotBorder, StudyPilotBorder)))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = StudyPilotTextDark,
            textAlign = TextAlign.Center
        )
    }
}
