package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun MotivationalEndScreen(
    lang: String,
    onBackToHome: () -> Unit
) {
    Scaffold(
        containerColor = StudyPilotBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main Motivation Card replicating End Screen
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("motivational_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Sunset Illustration
                    Image(
                        painter = painterResource(id = R.drawable.ill_motivation_sunset),
                        contentDescription = "Motivational Sunset",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = AppStrings.get("you_are_doing_great", lang),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudyPilotTextDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = AppStrings.get("every_step_counts", lang),
                        fontSize = 13.sp,
                        color = StudyPilotTextMuted,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onBackToHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("end_screen_back_home_btn"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudyPilotPrimary)
                    ) {
                        Text(
                            text = AppStrings.get("back_to_home", lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Brand Wordmark and Handwritten Slogan at bottom
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "StudyPilot",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = StudyPilotPrimary
                )
                Text(
                    text = AppStrings.get("tagline", lang),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = StudyPilotTextSubtle
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = AppStrings.get("better_students", lang),
                    fontSize = 13.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = StudyPilotPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
