package com.Qesnisu9.Madqis.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.di.ServiceLocator
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.Rare
import com.Qesnisu9.Madqis.core.theme.SurfaceCard
import com.Qesnisu9.Madqis.core.theme.SurfaceOutline
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary
import com.Qesnisu9.Madqis.core.ui.CandyBackground
import com.Qesnisu9.Madqis.core.ui.SectionHeader
import com.Qesnisu9.Madqis.domain.model.CandyRarity

private val CANDY_SPRITES = listOf(
    R.drawable.sprite_1,
    R.drawable.sprite_2,
    R.drawable.sprite_3,
    R.drawable.sprite_5
)

@Composable
fun CollectionPanel(onBack: () -> Unit) {
    val viewModel: CollectionViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    CollectionContent(state = state, onBack = onBack)
}

@Composable
private fun CollectionContent(
    state: CollectionUiState,
    onBack: () -> Unit
) {
    CandyBackground(
        overlay = listOf(
            Color(0xF2FFF4E6),
            Color(0xE6FFE0EB),
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
                title = stringResource(R.string.collection_title),
                subtitle = stringResource(R.string.collection_subtitle),
                onBack = onBack,
                backIconRes = R.drawable.icon_home,
                backDescription = stringResource(R.string.cd_home)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceCard.copy(alpha = 0.82f))
                    .border(2.dp, Rare.copy(alpha = 0.40f), RoundedCornerShape(18.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.collection_count,
                        state.unlockedCount,
                        state.total
                    ),
                    color = TextPrimary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = state.entries, key = { it.candy.id }) { entry ->
                    CandyTile(entry = entry)
                }
            }
        }
    }
}

@Composable
private fun CandyTile(entry: CollectionEntry) {
    val shape = RoundedCornerShape(24.dp)
    val rarityColor = when (entry.candy.rarity) {
        CandyRarity.COMMON -> Accent
        CandyRarity.SWEET -> Primary
        CandyRarity.RARE -> Rare
    }
    val sprite = CANDY_SPRITES[(entry.candy.id - 1) % CANDY_SPRITES.size]
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape)
            .background(SurfaceCard.copy(alpha = if (entry.unlocked) 0.82f else 0.58f))
            .border(2.dp, if (entry.unlocked) rarityColor.copy(alpha = 0.45f) else SurfaceOutline, shape)
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(sprite),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(56.dp)
                .alpha(if (entry.unlocked) 1f else 0.18f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (entry.unlocked) entry.candy.title else stringResource(R.string.collection_hidden),
            color = TextPrimary,
            maxLines = 2,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(6.dp))
        if (entry.unlocked) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(rarityColor)
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.collection_unlock, entry.candy.treatThreshold),
                color = TextSecondary,
                maxLines = 1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
