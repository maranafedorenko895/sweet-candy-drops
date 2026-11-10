package com.Qesnisu9.Madqis.presentation.progress

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.config.GameConfig
import com.Qesnisu9.Madqis.core.di.ServiceLocator
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.Rare
import com.Qesnisu9.Madqis.core.theme.Secondary
import com.Qesnisu9.Madqis.core.theme.SurfaceCard
import com.Qesnisu9.Madqis.core.theme.SurfaceOutline
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary
import com.Qesnisu9.Madqis.core.ui.CandyBackground
import com.Qesnisu9.Madqis.core.ui.GlassCard
import com.Qesnisu9.Madqis.core.ui.SectionHeader
import com.Qesnisu9.Madqis.core.ui.StatCard

@Composable
fun ProgressPanel(onBack: () -> Unit) {
    val viewModel: ProgressViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    ProgressContent(state = state, onBack = onBack)
}

@Composable
private fun ProgressContent(
    state: ProgressUiState,
    onBack: () -> Unit
) {
    CandyBackground(
        overlay = listOf(
            Color(0xF2FFF4E6),
            Color(0xE6FFE0EB),
            Color(0xF7FFD9C2)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 18.dp)
        ) {
            SectionHeader(
                title = stringResource(R.string.progress_title),
                subtitle = stringResource(R.string.progress_subtitle),
                onBack = onBack,
                backIconRes = R.drawable.icon_home,
                backDescription = stringResource(R.string.cd_home)
            )
            Spacer(modifier = Modifier.height(18.dp))
            if (state.isEmpty) {
                GlassCard(modifier = Modifier.fillMaxWidth(), accent = Primary) {
                    Text(
                        text = stringResource(R.string.progress_empty),
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_total_score),
                        value = state.totalScore.toString(),
                        accent = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_treats),
                        value = state.treatsCollected.toString(),
                        accent = Accent,
                        iconRes = R.drawable.sprite_2,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_routes_done),
                        value = state.routesFinished.toString(),
                        accent = Secondary,
                        iconRes = R.drawable.sprite_3,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_sugar_coins),
                        value = state.sugarCoins.toString(),
                        accent = Rare,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = stringResource(R.string.route_ladder),
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(10.dp))
                LadderRow(
                    from = 1,
                    to = GameConfig.SEGMENTS / 2,
                    bestSegment = state.bestSegment
                )
                Spacer(modifier = Modifier.height(8.dp))
                LadderRow(
                    from = GameConfig.SEGMENTS / 2 + 1,
                    to = GameConfig.SEGMENTS,
                    bestSegment = state.bestSegment
                )
                Spacer(modifier = Modifier.height(18.dp))
                GlassCard(modifier = Modifier.fillMaxWidth(), accent = Accent) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.sprite_5),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.size(14.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.personal_best),
                                color = TextSecondary,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.bestScore.toString(),
                                color = TextPrimary,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = stringResource(R.string.stat_best_combo),
                                color = TextSecondary,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.bestCombo.toString(),
                                color = Secondary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LadderRow(
    from: Int,
    to: Int,
    bestSegment: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        for (segment in from..to) {
            LadderChip(
                label = segment.toString(),
                passed = segment < bestSegment,
                current = segment == bestSegment
            )
        }
    }
}

@Composable
private fun LadderChip(
    label: String,
    passed: Boolean,
    current: Boolean
) {
    val shape = RoundedCornerShape(12.dp)
    val container = when {
        passed -> Primary
        current -> Accent
        else -> SurfaceCard.copy(alpha = 0.60f)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .clip(shape)
            .background(container)
            .border(2.dp, if (passed || current) container else SurfaceOutline, shape)
    ) {
        Text(
            text = label,
            color = if (passed || current) SurfaceCard else TextSecondary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
