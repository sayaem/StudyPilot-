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
import androidx.compose.ui.draw.shadow
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
            // Main Motivational Card exactly matching reference
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = Color(0x181E3A8A))
                    .testTag("motivational_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudyPilotBorder))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    // High-resolution digital vector sunset mountain artwork
                    Image(
                        painter = painterResource(id = R.drawable.img_motivation_sunset_clean),
                        contentDescription = "Motivational Sunset",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Top Left subtle category pill
                    Text(
                        text = "DAILY TIP",
                        fontSize = 11.sp,
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f),
                        letterSpacing = 1.sp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 18.dp, top = 16.dp)
                    )

                    // Subtle darkening overlay at the bottom mountain base for high contrast text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color(0x80101828),
                                        Color(0xDE0B1220)
                                    )
                                )
                            )
                    )

                    // Motivational copy and Back to Home button embedded over the mountain base
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 20.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppStrings.get("you_are_doing_great", lang),
                            fontSize = 22.sp,
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = AppStrings.get("every_step_counts", lang),
                            fontSize = 13.sp,
                            fontFamily = PlusJakartaSansFamily,
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onBackToHome,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("end_screen_back_home_btn"),
                            shape = RoundedCornerShape(23.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = StudyPilotPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = AppStrings.get("back_to_home", lang),
                                fontSize = 14.sp,
                                fontFamily = PlusJakartaSansFamily,
                                fontWeight = FontWeight.Bold,
                                color = StudyPilotPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Brand Wordmark and Slogan at bottom
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.img_rocket_clean),
                        contentDescription = "Rocket",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "StudyPilot",
                        fontSize = 22.sp,
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = StudyPilotPrimary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Your Study Partner  ·  For a Brighter Future",
                    fontSize = 12.sp,
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Medium,
                    color = StudyPilotTextSubtle
                )
            }
        }
    }
}
