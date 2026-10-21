package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    var startAnim by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.2f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "alpha"
    )
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.95f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        startAnim = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F2FE),
                        Color(0xFFF7FAFD),
                        Color(0xFFEBF4FE)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_splash_complete),
            contentDescription = "StudyPilot Splash",
            modifier = Modifier
                .fillMaxSize()
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value),
            contentScale = ContentScale.FillBounds
        )
    }
}
