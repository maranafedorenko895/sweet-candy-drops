package com.Qesnisu9.Madqis.presentation.menu

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.di.ServiceLocator
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.PrimaryWarm
import com.Qesnisu9.Madqis.core.theme.Rare
import com.Qesnisu9.Madqis.core.theme.Secondary
import com.Qesnisu9.Madqis.core.theme.SurfaceCard
import com.Qesnisu9.Madqis.core.theme.SurfaceOutline
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary
import com.Qesnisu9.Madqis.core.ui.CandyBackground
import com.Qesnisu9.Madqis.core.ui.HubTile
import com.Qesnisu9.Madqis.core.ui.PrimaryButton
import com.Qesnisu9.Madqis.core.ui.StatCard
import com.Qesnisu9.Madqis.core.ui.TreatPill
import com.Qesnisu9.Madqis.core.ui.clickableRole

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onRewards: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    MenuContent(
        state = state,
        onPlay = onPlay,
        onDaily = onDaily,
        onProgress = onProgress,
        onRewards = onRewards,
        onCollection = onCollection,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onRewards: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        appeared = true
    }
    val entrance by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(durationMillis = 420),
        label = "menuEntrance"
    )

    CandyBackground(
        overlay = listOf(
            Color(0xF2FFF4E6),
            Color(0xE6FFE4D6),
            Color(0xF7FFD9E4)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 18.dp)
                .padding(bottom = 10.dp)
                .graphicsLayer { translationY = (1f - entrance) * 26f }
        ) {
            MenuHeader(treats = state.treats)
            Spacer(modifier = Modifier.height(10.dp))
            DailyTile(
                title = state.dailyTitle,
                progress = state.dailyProgress,
                onOpen = onDaily
            )
            Spacer(modifier = Modifier.height(12.dp))
            HeroBanner(
                routeTitle = state.dailyTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (state.hasStats) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_best_score),
                        value = state.bestScore.toString(),
                        accent = Accent,
                        iconRes = R.drawable.sprite_5,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    StatCard(
                        label = stringResource(R.string.stat_routes_done),
                        value = state.routesFinished.toString(),
                        accent = Secondary,
                        iconRes = R.drawable.sprite_3,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            } else {
                FreshRunStrip()
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.menu_cta_hint),
                color = TextPrimary.copy(alpha = 0.72f),
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onPlay
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                HubTile(
                    label = stringResource(R.string.hub_progress),
                    accent = Primary,
                    iconRes = R.drawable.ic_progress,
                    onClick = onProgress,
                    modifier = Modifier
                        .weight(1f)
                        .height(86.dp)
                )
                HubTile(
                    label = stringResource(R.string.hub_rewards),
                    accent = Accent,
                    iconRes = R.drawable.ic_rewards,
                    onClick = onRewards,
                    modifier = Modifier
                        .weight(1f)
                        .height(86.dp)
                )
                HubTile(
                    label = stringResource(R.string.hub_collection),
                    accent = Rare,
                    iconRes = R.drawable.ic_collection,
                    onClick = onCollection,
                    modifier = Modifier
                        .weight(1f)
                        .height(86.dp)
                )
                HubTile(
                    label = stringResource(R.string.hub_settings),
                    accent = TextPrimary,
                    iconRes = R.drawable.ic_settings,
                    onClick = onSettings,
                    modifier = Modifier
                        .weight(1f)
                        .height(86.dp)
                )
            }
        }
    }
}

@Composable
private fun MenuHeader(treats: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.app_title),
            color = TextPrimary,
            maxLines = 2,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 16.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Spacer(modifier = Modifier.width(10.dp))
        TreatPill(
            value = treats.toString(),
            iconRes = R.drawable.sprite_2,
            description = stringResource(R.string.cd_treats_total, treats)
        )
    }
}

@Composable
private fun HeroBanner(
    routeTitle: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(32.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(elevation = 20.dp, shape = shape, ambientColor = Primary, spotColor = Primary)
            .clip(shape)
            .background(Brush.linearGradient(listOf(Primary, PrimaryWarm, Accent)))
            .border(2.dp, Color(0x73FFFFFF), shape)
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(84.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0x26FFFFFF))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(64.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0x1FFFFFFF))
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = stringResource(R.string.cd_hero),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(116.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.menu_hero_kicker),
                    color = Color(0xFF4A2633).copy(alpha = 0.70f),
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = routeTitle,
                    color = Color(0xFF4A2633),
                    maxLines = 2,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 21.sp,
                        lineHeight = 25.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = stringResource(R.string.cd_hazard),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.menu_hero_hint),
                        color = Color(0xFF4A2633).copy(alpha = 0.78f),
                        maxLines = 2,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun FreshRunStrip() {
    val shape = RoundedCornerShape(24.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .shadow(elevation = 10.dp, shape = shape, ambientColor = Secondary, spotColor = Secondary)
            .clip(shape)
            .background(SurfaceCard.copy(alpha = 0.80f))
            .border(2.dp, Secondary.copy(alpha = 0.35f), shape)
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = stringResource(R.string.cd_booster),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.menu_fresh_title),
                color = Secondary,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.menu_fresh_hint),
                color = TextSecondary,
                maxLines = 2,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun DailyTile(
    title: String,
    progress: Float,
    onOpen: () -> Unit
) {
    val shape = RoundedCornerShape(26.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .shadow(elevation = 12.dp, shape = shape, ambientColor = Secondary, spotColor = Secondary)
            .clip(shape)
            .background(SurfaceCard.copy(alpha = 0.80f))
            .border(2.dp, Secondary.copy(alpha = 0.40f), shape)
            .clickableRole(
                onClick = onOpen,
                description = stringResource(R.string.daily_challenge)
            )
            .padding(start = 14.dp, end = 4.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_3),
            contentDescription = stringResource(R.string.cd_bonus),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(38.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.daily_challenge),
                color = Secondary,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = title,
                color = TextPrimary,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp, lineHeight = 20.sp)
            )
            Spacer(modifier = Modifier.height(7.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(SurfaceOutline)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Secondary)
                )
            }
        }
        TextButton(onClick = onOpen) {
            Text(
                text = stringResource(R.string.open_label),
                color = Secondary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
