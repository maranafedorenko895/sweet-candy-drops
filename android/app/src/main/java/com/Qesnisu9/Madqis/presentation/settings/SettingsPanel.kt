package com.Qesnisu9.Madqis.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.di.ServiceLocator
import com.Qesnisu9.Madqis.core.theme.Accent
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.Secondary
import com.Qesnisu9.Madqis.core.theme.SurfaceCard
import com.Qesnisu9.Madqis.core.theme.SurfaceOutline
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary
import com.Qesnisu9.Madqis.core.ui.CandyBackground
import com.Qesnisu9.Madqis.core.ui.GlassCard
import com.Qesnisu9.Madqis.core.ui.SectionHeader
import com.Qesnisu9.Madqis.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsPanel(onBack: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    SettingsContent(
        state = state,
        onBack = onBack,
        onSound = viewModel::toggleSound,
        onHaptics = viewModel::toggleHaptics,
        onBigCandy = viewModel::toggleBigCandy,
        onReducedMotion = viewModel::toggleReducedMotion,
        onAskReset = viewModel::askReset,
        onCancelReset = viewModel::cancelReset,
        onConfirmReset = viewModel::confirmReset
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onBack: () -> Unit,
    onSound: (Boolean) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onBigCandy: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
    val onLabel = stringResource(R.string.switch_on)
    val offLabel = stringResource(R.string.switch_off)
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
                title = stringResource(R.string.settings_title),
                subtitle = stringResource(R.string.settings_subtitle),
                onBack = onBack,
                backIconRes = R.drawable.icon_home,
                backDescription = stringResource(R.string.cd_home)
            )
            Spacer(modifier = Modifier.height(18.dp))
            SettingRow(
                label = stringResource(R.string.setting_sound),
                checked = state.soundCues,
                stateLabel = if (state.soundCues) onLabel else offLabel,
                onChange = onSound
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingRow(
                label = stringResource(R.string.setting_haptics),
                checked = state.haptics,
                stateLabel = if (state.haptics) onLabel else offLabel,
                onChange = onHaptics
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingRow(
                label = stringResource(R.string.setting_big_candy),
                checked = state.bigCandyMode,
                stateLabel = if (state.bigCandyMode) onLabel else offLabel,
                onChange = onBigCandy
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingRow(
                label = stringResource(R.string.setting_reduced_motion),
                checked = state.reducedMotion,
                stateLabel = if (state.reducedMotion) onLabel else offLabel,
                onChange = onReducedMotion
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = onAskReset,
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset_progress),
                    color = Primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            GlassCard(modifier = Modifier.fillMaxWidth(), accent = Accent) {
                Text(
                    text = stringResource(R.string.app_title),
                    color = TextPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.app_version),
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.compliance_note),
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
    if (state.confirmResetVisible) {
        ConfirmResetDialog(onConfirm = onConfirmReset, onCancel = onCancelReset)
    }
}

@Composable
private fun SettingRow(
    label: String,
    checked: Boolean,
    stateLabel: String,
    onChange: (Boolean) -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(shape)
            .background(SurfaceCard.copy(alpha = 0.80f))
            .border(2.dp, SurfaceOutline, shape)
            .padding(horizontal = 16.dp)
            .semantics { stateDescription = stateLabel }
    ) {
        Text(
            text = label,
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium
        )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SurfaceCard,
                checkedTrackColor = Secondary,
                uncheckedThumbColor = SurfaceCard,
                uncheckedTrackColor = SurfaceOutline
            )
        )
    }
}
