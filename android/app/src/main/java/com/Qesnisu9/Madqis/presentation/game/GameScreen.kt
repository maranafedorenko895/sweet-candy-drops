package com.Qesnisu9.Madqis.presentation.game

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.config.GameConfig
import com.Qesnisu9.Madqis.core.di.ServiceLocator
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.OnPrimary
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.Secondary
import com.Qesnisu9.Madqis.core.theme.SurfaceCard
import com.Qesnisu9.Madqis.core.theme.SurfaceOutline
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary
import com.Qesnisu9.Madqis.core.ui.CandyBackground
import com.Qesnisu9.Madqis.core.ui.LivesRow
import com.Qesnisu9.Madqis.core.ui.TreatPill
import com.Qesnisu9.Madqis.domain.model.RunResult
import com.Qesnisu9.Madqis.presentation.dialog.PauseDialog

@Composable
fun GameScreen(
    dailySeed: Int?,
    token: Long,
    onFinished: (RunResult) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(token) {
        viewModel.prepare(dailySeed, token)
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished(event.result)
                is GameEvent.ExitToMenu -> onMenu()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }
    GameContent(
        state = state,
        onLaneTap = viewModel::moveTo,
        onLeft = { viewModel.moveBy(-1) },
        onRight = { viewModel.moveBy(1) },
        onPause = viewModel::openPause,
        onResume = viewModel::dismissPause,
        onLeave = viewModel::leaveToMenu
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onLaneTap: (Int) -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onLeave: () -> Unit
) {
    val animatedScore by animateIntAsState(
        targetValue = state.score,
        animationSpec = tween(durationMillis = 320),
        label = "score"
    )
    CandyBackground(
        overlay = listOf(
            Color(0xE6FFF4E6),
            Color(0xD9FFE0EB),
            Color(0xF2FFD9C2)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                lives = state.lives,
                segment = state.segment,
                progress = state.segmentProgress,
                score = animatedScore,
                multiplier = state.multiplier,
                combo = state.combo
            )
            Text(
                text = state.routeTitle,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                style = MaterialTheme.typography.labelSmall
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                RouteBoard(
                    rows = state.rows,
                    heroLane = state.heroLane,
                    stepIndex = state.stepIndex,
                    stepMs = state.stepMs,
                    phase = state.phase,
                    hazardFlash = state.hazardFlash,
                    bigCandyMode = state.bigCandyMode,
                    reducedMotion = state.reducedMotion,
                    onLaneTap = onLaneTap,
                    modifier = Modifier.fillMaxSize()
                )
                if (state.pickupPoints > 0) {
                    PickupLabel(
                        points = state.pickupPoints,
                        token = state.pickupToken,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 22.dp)
            ) {
                ControlBar(
                    onLeft = onLeft,
                    onRight = onRight,
                    onPause = onPause
                )
            }
        }
    }
    if (state.pauseVisible) {
        PauseDialog(onResume = onResume, onMenu = onLeave)
    }
}

@Composable
private fun GameHeader(
    lives: Int,
    segment: Int,
    progress: Float,
    score: Int,
    multiplier: Int,
    combo: Int
) {
    val shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
    val segmentLabel = stringResource(R.string.game_segment, segment, GameConfig.SEGMENTS)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clip(shape)
            .background(SurfaceCard.copy(alpha = 0.78f))
            .padding(horizontal = 14.dp)
    ) {
        LivesRow(
            lives = lives,
            max = GameConfig.MAX_LIVES,
            description = stringResource(R.string.lives_state, lives, GameConfig.MAX_LIVES)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = segmentLabel,
                color = TextPrimary.copy(alpha = 0.72f),
                modifier = Modifier.semantics { stateDescription = segmentLabel },
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(SurfaceOutline)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Accent)
                )
            }
            if (combo >= GameConfig.COMBO_CHIP_AT) {
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Secondary.copy(alpha = 0.20f))
                        .border(1.dp, Secondary, RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .semantics { liveRegion = LiveRegionMode.Polite }
                ) {
                    Text(
                        text = stringResource(R.string.game_multiplier, multiplier),
                        color = Secondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
        TreatPill(
            value = score.toString(),
            iconRes = R.drawable.sprite_2,
            description = stringResource(R.string.cd_score, score),
            accent = Primary,
            iconSize = 18
        )
    }
}

@Composable
private fun PickupLabel(
    points: Int,
    token: Long,
    modifier: Modifier = Modifier
) {
    val lift by animateIntAsState(
        targetValue = (token % 2L).toInt(),
        animationSpec = tween(durationMillis = 520),
        label = "pickupLift"
    )
    Box(
        modifier = modifier
            .padding(bottom = 12.dp + (lift * 6).dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Secondary.copy(alpha = 0.92f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = stringResource(R.string.pickup_points, points),
            color = OnPrimary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
