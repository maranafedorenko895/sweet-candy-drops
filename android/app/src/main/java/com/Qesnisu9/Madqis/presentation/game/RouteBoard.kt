package com.Qesnisu9.Madqis.presentation.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.Secondary
import com.Qesnisu9.Madqis.core.theme.SurfaceCard
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary
import com.Qesnisu9.Madqis.core.ui.clickableRole
import com.Qesnisu9.Madqis.domain.model.BoardRow
import com.Qesnisu9.Madqis.domain.model.CandyCell
import com.Qesnisu9.Madqis.domain.model.Lane
import kotlin.math.floor

private const val BOARD_MAX_WIDTH = 380
private const val BOARD_FRAME = 13
private const val MIN_ROWS = 3

@Composable
fun RouteBoard(
    rows: List<BoardRow>,
    heroLane: Int,
    stepIndex: Int,
    stepMs: Long,
    phase: GamePhase,
    hazardFlash: Boolean,
    bigCandyMode: Boolean,
    reducedMotion: Boolean,
    onLaneTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gliding = phase == GamePhase.RUNNING && !reducedMotion
    val scroll by animateFloatAsState(
        targetValue = stepIndex.toFloat(),
        animationSpec = if (gliding) tween(stepMs.toInt(), easing = LinearEasing) else snap(),
        label = "routeScroll"
    )
    val rowOffset = (scroll - stepIndex + 1f).coerceIn(0f, 1f)
    val boardShape = RoundedCornerShape(28.dp)
    val borderColor = if (hazardFlash) Primary else Primary.copy(alpha = 0.22f)

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        val frame = BOARD_FRAME.dp
        val outerWidth = if (maxWidth < BOARD_MAX_WIDTH.dp) maxWidth else BOARD_MAX_WIDTH.dp
        val innerWidth = outerWidth - frame * 2
        val cell = floor((innerWidth / 3).value).dp
        val contentWidth = cell * 3
        val availableHeight = maxHeight - frame * 2
        val maxRows = rows.size.coerceAtLeast(MIN_ROWS)
        val rowCount = floor(availableHeight / cell).toInt().coerceIn(MIN_ROWS, maxRows)
        val contentHeight = cell * rowCount
        val cellPx = with(LocalDensity.current) { cell.toPx() }
        val spriteSize = if (bigCandyMode) cell - 6.dp else cell - 12.dp
        val heroSize = cell - 6.dp
        val heroX by animateDpAsState(
            targetValue = cell * heroLane,
            animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow),
            label = "heroLane"
        )

        Box(
            modifier = Modifier
                .width(outerWidth)
                .height(contentHeight + frame * 2)
                .shadow(elevation = 16.dp, shape = boardShape, ambientColor = Primary, spotColor = Primary)
                .clip(boardShape)
                .background(SurfaceCard.copy(alpha = 0.66f))
                .border(3.dp, borderColor, boardShape)
                .padding(frame)
        ) {
            Box(
                modifier = Modifier
                    .width(contentWidth)
                    .height(contentHeight)
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (lane in 0 until Lane.COUNT) {
                        Box(
                            modifier = Modifier
                                .width(cell)
                                .fillMaxHeight()
                                .background(
                                    if (lane == heroLane) {
                                        Brush.verticalGradient(
                                            listOf(Color(0x00FFD166), Color(0x40FFD166))
                                        )
                                    } else {
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color.Transparent)
                                        )
                                    }
                                )
                                .clickableRole(
                                    onClick = { onLaneTap(lane) },
                                    description = stringResource(R.string.cd_lane, lane + 1)
                                )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .offset(x = cell)
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Accent.copy(alpha = 0.35f))
                )
                Box(
                    modifier = Modifier
                        .offset(x = cell * 2)
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Accent.copy(alpha = 0.35f))
                )

                val shown = rows.takeLast(rowCount)
                shown.forEachIndexed { index, row ->
                    Row(
                        modifier = Modifier
                            .width(contentWidth)
                            .height(cell)
                            .graphicsLayer { translationY = (index - 1 + rowOffset) * cellPx }
                    ) {
                        row.cells.forEach { candy ->
                            CandyCellView(
                                candy = candy,
                                cell = cell,
                                spriteSize = spriteSize
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = heroX, y = cell * (rowCount - 1))
                        .width(cell)
                        .height(cell)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0x00FF6B6B), Color(0x33FF6B6B))
                            )
                        )
                )
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = stringResource(R.string.cd_hero),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = heroX + (cell - heroSize) / 2, y = cell * (rowCount - 1) + 3.dp)
                        .size(heroSize)
                )

                if (phase == GamePhase.READY) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .shadow(10.dp, RoundedCornerShape(20.dp), ambientColor = Secondary, spotColor = Secondary)
                            .clip(RoundedCornerShape(20.dp))
                            .background(SurfaceCard.copy(alpha = 0.92f))
                            .border(2.dp, Secondary.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 18.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.game_tap_to_begin),
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                if (phase == GamePhase.PAUSED) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color(0x59FFF4E6))
                    )
                }

                if (rows.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(18.dp))
                            .background(SurfaceCard.copy(alpha = 0.70f))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.game_board_loading),
                            color = TextSecondary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CandyCellView(
    candy: CandyCell,
    cell: Dp,
    spriteSize: Dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(cell)
    ) {
        val sprite = when (candy) {
            CandyCell.TREAT -> R.drawable.sprite_2
            CandyCell.BONUS -> R.drawable.sprite_3
            CandyCell.HAZARD -> R.drawable.sprite_4
            CandyCell.BOOSTER -> R.drawable.sprite_5
            CandyCell.EMPTY -> 0
        }
        if (sprite != 0) {
            val description = when (candy) {
                CandyCell.TREAT -> stringResource(R.string.cd_treat)
                CandyCell.BONUS -> stringResource(R.string.cd_bonus)
                CandyCell.HAZARD -> stringResource(R.string.cd_hazard)
                else -> stringResource(R.string.cd_booster)
            }
            Image(
                painter = painterResource(sprite),
                contentDescription = description,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(spriteSize)
                    .alpha(0.96f)
            )
        }
    }
}
