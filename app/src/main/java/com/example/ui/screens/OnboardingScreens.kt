package com.example.ui.screens

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
import androidx.compose.ui.graphics.Brush
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
        1 -> R.drawable.img_onboard_1_clean
        2 -> R.drawable.img_onboard_2_clean
        else -> R.drawable.img_onboard_3_clean
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF1F6FE),
                        Color(0xFFF8FAFD),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with Skip Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 44.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (page < 3) {
                    Text(
                        text = AppStrings.get("skip", lang),
                        fontSize = 14.sp,
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B),
                        modifier = Modifier
                            .clickable { onSkip() }
                            .padding(8.dp)
                            .testTag("onboarding_skip_button")
                    )
                } else {
                    Spacer(modifier = Modifier.height(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Centered High-Resolution Illustrated Artwork with transparent softly feathered edges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = illustrationRes),
                    contentDescription = title,
                    modifier = Modifier.size(280.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Title & Description
            Text(
                text = title,
                fontSize = 24.sp,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Bold,
                color = StudyPilotTextDark,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = desc,
                fontSize = 14.sp,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Normal,
                color = StudyPilotTextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Pagination Dots & Next Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 44.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pagination Dots (4 dots matching reference: active pill, 3 grey dots)
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
                                .background(if (isActive) StudyPilotPrimary else Color(0xFFCBD5E1))
                        )
                    }
                }

                // Forward Action Button
                if (page < 3) {
                    IconButton(
                        onClick = onNext,
                        modifier = Modifier
                            .size(54.dp)
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
                            .padding(horizontal = 4.dp)
                            .testTag("onboarding_get_started_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary)
                    ) {
                        Text(
                            text = AppStrings.get("get_started", lang),
                            fontSize = 15.sp,
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
