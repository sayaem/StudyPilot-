package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PlusJakartaSansFamily

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    var startAnim by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "alpha"
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.94f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        startAnim = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("splash_screen")
    ) {
        // Fullscreen atmospheric mountain & dawn background (9:16 portrait)
        Image(
            painter = painterResource(id = R.drawable.img_splash_bg_full),
            contentDescription = "StudyPilot Dawn Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Upper-Center Brand Presentation: Crisp Rocket + StudyPilot + Tagline (inside halo)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 130.dp)
                .alpha(alphaAnim)
                .scale(scaleAnim),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // High-resolution clean modern rocket logo
            Image(
                painter = painterResource(id = R.drawable.img_rocket_clean),
                contentDescription = "StudyPilot Rocket",
                modifier = Modifier.size(118.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "StudyPilot",
                fontSize = 34.sp,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E3A8A),
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Plan  ·  Focus  ·  Achieve",
                fontSize = 14.sp,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2563EB),
                letterSpacing = 0.5.sp
            )
        }

        // Bottom Slogan inside the mountains
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 44.dp)
                .alpha(alphaAnim),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Study Partner",
                fontSize = 14.sp,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.95f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "For a Brighter Future",
                fontSize = 15.sp,
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}
