package com.Qesnisu9.Madqis.presentation.settings

data class SettingsUiState(
    val loading: Boolean = true,
    val soundCues: Boolean = true,
    val haptics: Boolean = true,
    val bigCandyMode: Boolean = false,
    val reducedMotion: Boolean = false,
    val confirmResetVisible: Boolean = false,
    val resetDone: Boolean = false
)
