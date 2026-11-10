package com.Qesnisu9.Madqis.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Qesnisu9.Madqis.R

@Composable
fun LivesRow(
    lives: Int,
    max: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.semantics { stateDescription = description }
    ) {
        for (index in 0 until max) {
            val alive = index < lives
            val heartScale by animateFloatAsState(
                targetValue = if (alive) 1f else 0.76f,
                animationSpec = tween(durationMillis = 220),
                label = "heartScale"
            )
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(20.dp)
                    .scale(heartScale)
                    .alpha(if (alive) 1f else 0.22f)
            )
        }
    }
}
