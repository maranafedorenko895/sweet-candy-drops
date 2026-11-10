package com.Qesnisu9.Madqis.presentation.gameover

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.config.GameConfig
import com.Qesnisu9.Madqis.core.di.ServiceLocator
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.Secondary
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary
import com.Qesnisu9.Madqis.core.ui.CandyBackground
import com.Qesnisu9.Madqis.core.ui.PrimaryButton
import com.Qesnisu9.Madqis.core.ui.SecondaryButton
import com.Qesnisu9.Madqis.core.ui.StatCard
import com.Qesnisu9.Madqis.domain.model.RunResult

@Composable
fun GameOverScreen(
    result: RunResult,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(result) {
        viewModel.refresh()
    }
    GameOverContent(
        result = result,
        state = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    result: RunResult,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(result) {
        appeared = true
    }
    val medallionScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.72f,
        animationSpec = spring(dampingRatio = 0.52f, stiffness = Spring.StiffnessLow),
        label = "medallion"
    )
    val accent = if (result.completed) Secondary else Primary
    val overlay = if (result.completed) {
        listOf(Color(0xE606D6A0), Color(0xF2FFF4E6), Color(0xF2FFD9E4))
    } else {
        listOf(Color(0xE6FF6B6B), Color(0xF2FFF4E6), Color(0xF2FFE0EB))
    }
    val statsVisible = result.score > 0 && result.treats > 0

    CandyBackground(overlay = overlay) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 18.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(132.dp)
                    .scale(medallionScale)
                    .shadow(
                        elevation = 18.dp,
                        shape = RoundedCornerShape(44.dp),
                        ambientColor = accent,
                        spotColor = accent
                    )
                    .clip(RoundedCornerShape(44.dp))
                    .background(Color(0x40FFFFFF))
                    .border(2.dp, Color(0x73FFFFFF), RoundedCornerShape(44.dp))
            ) {
                Image(
                    painter = painterResource(
                        if (result.completed) R.drawable.sprite_5 else R.drawable.sprite_4
                    ),
                    contentDescription = stringResource(
                        if (result.completed) R.string.cd_booster else R.string.cd_hazard
                    ),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(78.dp)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(
                    if (result.completed) R.string.result_complete else R.string.result_failed
                ),
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    R.string.result_subline,
                    result.routeTitle,
                    result.segment,
                    GameConfig.SEGMENTS
                ),
                color = TextSecondary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(18.dp))
            if (statsVisible) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_score),
                        value = result.score.toString(),
                        accent = Primary,
                        modifier = Modifier
                            .weight(1f)
                            .height(96.dp)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_treats),
                        value = result.treats.toString(),
                        accent = Accent,
                        iconRes = R.drawable.sprite_2,
                        modifier = Modifier
                            .weight(1f)
                            .height(96.dp)
                    )
                    if (result.bestCombo > 0) {
                        StatCard(
                            label = stringResource(R.string.stat_best_combo),
                            value = result.bestCombo.toString(),
                            accent = Secondary,
                            modifier = Modifier
                                .weight(1f)
                                .height(96.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
            if (result.newBest) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Accent.copy(alpha = 0.26f))
                        .border(2.dp, Accent, RoundedCornerShape(24.dp))
                        .padding(horizontal = 22.dp)
                ) {
                    Text(
                        text = stringResource(R.string.new_best),
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (result.coinsEarned > 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.sprite_2),
                        contentDescription = stringResource(R.string.cd_treat),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.coins_earned, result.coinsEarned),
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.result_best, state.bestScore),
                color = TextSecondary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain
            )
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                leadingIcon = R.drawable.icon_home
            )
        }
    }
}
