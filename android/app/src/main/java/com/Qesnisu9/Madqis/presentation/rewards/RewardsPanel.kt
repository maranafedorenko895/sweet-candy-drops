package com.Qesnisu9.Madqis.presentation.rewards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.Qesnisu9.Madqis.core.ui.SectionHeader
import com.Qesnisu9.Madqis.domain.model.RewardState

@Composable
fun RewardsPanel(onBack: () -> Unit) {
    val viewModel: RewardsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    RewardsContent(state = state, onBack = onBack, onClaim = viewModel::claim)
}

@Composable
private fun RewardsContent(
    state: RewardsUiState,
    onBack: () -> Unit,
    onClaim: (Int) -> Unit
) {
    CandyBackground(
        overlay = listOf(
            Color(0xF2FFF4E6),
            Color(0xE6FFE4D6),
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
                title = stringResource(R.string.rewards_title),
                subtitle = stringResource(R.string.rewards_subtitle),
                onBack = onBack,
                backIconRes = R.drawable.icon_home,
                backDescription = stringResource(R.string.cd_home)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                contentPadding = PaddingValues(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = state.rows, key = { it.reward.id }) { row ->
                    RewardCard(row = row, onClaim = onClaim)
                }
            }
        }
    }
}

@Composable
private fun RewardCard(
    row: RewardRow,
    onClaim: (Int) -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    val unlocked = row.state == RewardState.UNLOCKED
    val claimed = row.state == RewardState.CLAIMED
    val stateLabel = when (row.state) {
        RewardState.UNLOCKED -> stringResource(R.string.claim)
        RewardState.CLAIMED -> stringResource(R.string.claimed)
        RewardState.LOCKED -> stringResource(R.string.locked)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(shape)
            .background(SurfaceCard.copy(alpha = if (claimed) 0.55f else 0.80f))
            .border(2.dp, if (unlocked) Secondary else SurfaceOutline, shape)
            .padding(horizontal = 12.dp)
            .semantics { stateDescription = stateLabel }
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(36.dp)
                .alpha(if (row.state == RewardState.LOCKED) 0.45f else 1f)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = row.reward.title,
                color = TextPrimary,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(R.string.reward_requirement, row.reward.requiredRoutes),
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall
            )
        }
        if (unlocked) {
            Button(
                onClick = { onClaim(row.reward.id) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Secondary,
                    contentColor = SurfaceCard
                )
            ) {
                Text(
                    text = stateLabel,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (claimed) Accent.copy(alpha = 0.20f) else Color.Transparent)
                    .border(
                        2.dp,
                        if (claimed) Accent.copy(alpha = 0.50f) else SurfaceOutline,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .alpha(if (claimed) 0.75f else 0.45f)
            ) {
                Text(
                    text = stateLabel,
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
