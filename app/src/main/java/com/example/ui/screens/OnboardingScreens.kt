package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AppStrings
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    page: Int,
    lang: String,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    val illustrationRes = when (page) {
        1 -> R.drawable.ill_onboard_1
        2 -> R.drawable.ill_onboard_2
        else -> R.drawable.ill_onboard_3
    }

    val title = when (page) {
        1 -> AppStrings.get("onboard_title_1", lang)
        2 -> AppStrings.get("onboard_title_2", lang)
        else -> AppStrings.get("onboard_title_3", lang)
    }

    val desc = when (page) {
        1 -> AppStrings.get("onboard_desc_1", lang)
        2 -> AppStrings.get("onboard_desc_2", lang)
        else -> AppStrings.get("onboard_desc_3", lang)
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with Skip Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (page < 3) {
                    Text(
                        text = AppStrings.get("skip", lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = StudyPilotTextSubtle,
                        modifier = Modifier
                            .clickable { onSkip() }
                            .padding(8.dp)
                            .testTag("onboarding_skip_button")
                    )
                } else {
                    Spacer(modifier = Modifier.height(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Centered Illustrated Artwork
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = illustrationRes),
                    contentDescription = title,
                    modifier = Modifier.size(260.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title & Description
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = StudyPilotTextDark,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium,
                color = StudyPilotTextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Pagination Dots & Next Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pagination Dots (4 dots as in reference)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..4) {
                        val isActive = (i == page)
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (isActive) 24.dp else 6.dp)
                                .clip(CircleShape)
                                .background(if (isActive) StudyPilotPrimary else Color(0xFFDCE6F2))
                        )
                    }
                }

                // Forward Action
                if (page < 3) {
                    IconButton(
                        onClick = onNext,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(StudyPilotPrimary)
                            .testTag("onboarding_next_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = onNext,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("onboarding_get_started_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary)
                    ) {
                        Text(
                            text = AppStrings.get("get_started", lang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
