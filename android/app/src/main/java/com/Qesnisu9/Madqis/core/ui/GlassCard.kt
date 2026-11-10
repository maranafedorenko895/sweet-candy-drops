package com.Qesnisu9.Madqis.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.SurfaceCard

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    accent: Color = Primary,
    radius: Dp = 26.dp,
    alpha: Float = 0.78f,
    innerPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(radius)
    Column(
        modifier = modifier
            .shadow(elevation = 12.dp, shape = shape, ambientColor = accent, spotColor = accent)
            .clip(shape)
            .background(SurfaceCard.copy(alpha = alpha))
            .border(2.dp, accent.copy(alpha = 0.34f), shape)
            .padding(innerPadding),
        content = content
    )
}
