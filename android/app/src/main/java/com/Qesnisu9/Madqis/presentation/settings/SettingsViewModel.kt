package com.Qesnisu9.Madqis.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qesnisu9.Madqis.domain.model.AppSettings
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository
import com.Qesnisu9.Madqis.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val settings = settingsRepository.load()
            _state.update {
                it.copy(
                    loading = false,
                    soundCues = settings.soundCues,
                    haptics = settings.haptics,
                    bigCandyMode = settings.bigCandyMode,
                    reducedMotion = settings.reducedMotion
                )
            }
        }
    }

    fun toggleSound(value: Boolean) = persist { it.copy(soundCues = value) }

    fun toggleHaptics(value: Boolean) = persist { it.copy(haptics = value) }

    fun toggleBigCandy(value: Boolean) = persist { it.copy(bigCandyMode = value) }

    fun toggleReducedMotion(value: Boolean) = persist { it.copy(reducedMotion = value) }

    fun askReset() {
        _state.update { it.copy(confirmResetVisible = true) }
    }

    fun cancelReset() {
        _state.update { it.copy(confirmResetVisible = false) }
    }

    fun confirmReset() {
        viewModelScope.launch {
            progressRepository.reset()
            _state.update { it.copy(confirmResetVisible = false, resetDone = true) }
        }
    }

    private fun persist(transform: (AppSettings) -> AppSettings) {
        viewModelScope.launch {
            val updated = transform(settingsRepository.load())
            settingsRepository.save(updated)
            _state.update {
                it.copy(
                    soundCues = updated.soundCues,
                    haptics = updated.haptics,
                    bigCandyMode = updated.bigCandyMode,
                    reducedMotion = updated.reducedMotion
                )
            }
        }
    }
}
