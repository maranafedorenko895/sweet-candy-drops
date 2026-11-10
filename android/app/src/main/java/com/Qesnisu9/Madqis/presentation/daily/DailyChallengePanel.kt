package com.Qesnisu9.Madqis.presentation.daily

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
import androidx.compose.material3.Icon
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
import com.Qesnisu9.Madqis.core.di.ServiceLocator
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.Secondary
import com.Qesnisu9.Madqis.core.theme.SurfaceCard
import com.Qesnisu9.Madqis.core.theme.SurfaceOutline
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary
import com.Qesnisu9.Madqis.core.ui.CandyBackground
import com.Qesnisu9.Madqis.core.ui.GlassCard
import com.Qesnisu9.Madqis.core.ui.PrimaryButton
import com.Qesnisu9.Madqis.core.ui.SectionHeader

private val DAY_LABELS = listOf(
    R.string.day_sun,
    R.string.day_mon,
    R.string.day_tue,
    R.string.day_wed,
    R.string.day_thu,
    R.string.day_fri,
    R.string.day_sat
)

@Composable
fun DailyChallengePanel(
    onBack: () -> Unit,
    onStart: (Int) -> Unit
) {
    val viewModel: DailyChallengeViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    DailyChallengeContent(state = state, onBack = onBack, onStart = onStart)
}

@Composable
private fun DailyChallengeContent(
    state: DailyChallengeUiState,
    onBack: () -> Unit,
    onStart: (Int) -> Unit
) {
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
        ) {
            SectionHeader(
                title = stringResource(R.string.daily_title),
                subtitle = stringResource(R.string.daily_subtitle),
                onBack = onBack,
                backIconRes = R.drawable.icon_home,
                backDescription = stringResource(R.string.cd_home)
            )
            Spacer(modifier = Modifier.height(18.dp))
            if (!state.available) {
                GlassCard(modifier = Modifier.fillMaxWidth(), accent = Secondary) {
                    Text(
                        text = stringResource(R.string.daily_empty),
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            } else {
                GlassCard(modifier = Modifier.fillMaxWidth(), accent = Secondary) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.sprite_3),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.daily_challenge),
                                color = Secondary,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.title,
                                color = TextPrimary,
                                maxLines = 2,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Accent.copy(alpha = 0.26f))
                                .border(2.dp, Accent, RoundedCornerShape(14.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.daily_today_only),
                                color = TextPrimary,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.daily_target, state.target),
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(SurfaceOutline)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(state.progress.coerceIn(0f, 1f))
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Secondary)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.daily_progress, state.bestScore, state.target),
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = stringResource(R.string.start),
                        onClick = { onStart(state.seed) }
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DAY_LABELS.forEachIndexed { index, labelRes ->
                        val isToday = index == state.todayIndex
                        val done = state.claimed && isToday
                        DayPill(
                            label = stringResource(labelRes),
                            isToday = isToday,
                            done = done,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayPill(
    label: String,
    isToday: Boolean,
    done: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    val container = when {
        done -> Secondary
        isToday -> SurfaceCard.copy(alpha = 0.90f)
        else -> SurfaceCard.copy(alpha = 0.60f)
    }
    val borderColor = if (isToday) Accent else SurfaceOutline
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(container)
            .border(2.dp, borderColor, shape)
    ) {
        Text(
            text = label,
            color = if (done) SurfaceCard else TextPrimary,
            style = MaterialTheme.typography.labelSmall
        )
        if (done) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = SurfaceCard,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
