package com.Qesnisu9.Madqis.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.OnPrimary
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.PrimaryWarm
import com.Qesnisu9.Madqis.core.theme.SurfaceCard
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.ui.clickableRole

@Composable
fun ControlBar(
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(30.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .shadow(elevation = 18.dp, shape = shape, ambientColor = Primary, spotColor = Primary)
            .clip(shape)
            .background(SurfaceCard.copy(alpha = 0.86f))
            .border(2.dp, Accent.copy(alpha = 0.50f), shape)
            .padding(horizontal = 12.dp)
    ) {
        LaneButton(
            iconRes = R.drawable.ic_arrow_left,
            description = stringResource(R.string.cd_move_left),
            onClick = onLeft
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(SurfaceCard.copy(alpha = 0.92f))
                .border(2.dp, TextPrimary.copy(alpha = 0.25f), CircleShape)
                .clickableRole(
                    onClick = onPause,
                    description = stringResource(R.string.cd_pause)
                )
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
        LaneButton(
            iconRes = R.drawable.ic_arrow_right,
            description = stringResource(R.string.cd_move_right),
            onClick = onRight
        )
    }
}

@Composable
private fun LaneButton(
    iconRes: Int,
    description: String,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(64.dp)
            .shadow(elevation = 10.dp, shape = CircleShape, ambientColor = Primary, spotColor = Primary)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(Primary, PrimaryWarm)))
            .clickableRole(onClick = onClick, description = description)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = OnPrimary,
            modifier = Modifier.size(28.dp)
        )
    }
}
