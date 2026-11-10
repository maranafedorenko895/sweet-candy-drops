package com.Qesnisu9.Madqis.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.di.ServiceLocator
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.OnPrimary
import com.Qesnisu9.Madqis.core.theme.Secondary
import com.Qesnisu9.Madqis.core.ui.CandyBackground

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val viewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val finished by viewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashContent()
}

@Composable
private fun SplashContent() {
    val transition = rememberInfiniteTransition(label = "splash")
    val breath by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Reverse),
        label = "breath"
    )
    val wobble by transition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Reverse),
        label = "wobble"
    )
    val dropA by transition.animateFloat(
        initialValue = -40f,
        targetValue = 320f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Restart),
        label = "dropA"
    )
    val dropB by transition.animateFloat(
        initialValue = -80f,
        targetValue = 340f,
        animationSpec = infiniteRepeatable(tween(3800, easing = LinearEasing), RepeatMode.Restart),
        label = "dropB"
    )
    val dropC by transition.animateFloat(
        initialValue = -120f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4400, easing = LinearEasing), RepeatMode.Restart),
        label = "dropC"
    )

    CandyBackground(
        overlay = listOf(
            Color(0xF04A2633),
            Color(0xE67A2F45),
            Color(0xDCB33A5B)
        )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(268.dp)
                .clip(CircleShape)
                .background(Color(0x33FFD166))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .size(208.dp)
                .clip(CircleShape)
                .background(Color(0x2606D6A0))
        )
        SplashDrop(R.drawable.sprite_2, 44, dropA, Alignment.TopStart, 36.dp, 0.42f)
        SplashDrop(R.drawable.sprite_3, 30, dropB, Alignment.TopCenter, 0.dp, 0.34f)
        SplashDrop(R.drawable.sprite_5, 38, dropC, Alignment.TopEnd, 30.dp, 0.46f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(232.dp)
                    .graphicsLayer {
                        scaleX = breath
                        scaleY = breath
                        rotationZ = wobble
                    }
                    .shadow(
                        elevation = 24.dp,
                        shape = RoundedCornerShape(56.dp),
                        ambientColor = Color(0xFF2A1420),
                        spotColor = Color(0xFF2A1420)
                    )
                    .clip(RoundedCornerShape(56.dp))
                    .background(Color(0x26FFD166))
                    .border(2.dp, Color(0x59FFD166), RoundedCornerShape(56.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(180.dp)
                )
            }
            Spacer(modifier = Modifier.height(34.dp))
            Text(
                text = stringResource(R.string.app_title),
                color = OnPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(2.dp)
                    .background(Accent)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                color = Accent.copy(alpha = 0.82f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(40.dp))
            LinearProgressIndicator(
                color = Accent,
                trackColor = Color(0x33FFF4E6),
                modifier = Modifier
                    .width(180.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                color = OnPrimary.copy(alpha = 0.70f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Secondary.copy(alpha = 0.55f))
            )
        }
    }
}

@Composable
private fun BoxScope.SplashDrop(
    spriteRes: Int,
    sizeDp: Int,
    travel: Float,
    alignment: Alignment,
    inset: Dp,
    dropAlpha: Float
) {
    Image(
        painter = painterResource(spriteRes),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .align(alignment)
            .padding(horizontal = inset)
            .size(sizeDp.dp)
            .graphicsLayer { translationY = travel }
            .alpha(dropAlpha)
    )
}
