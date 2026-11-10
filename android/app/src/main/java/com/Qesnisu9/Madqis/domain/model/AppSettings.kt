package com.Qesnisu9.Madqis.domain.model

data class AppSettings(
    val soundCues: Boolean = true,
    val haptics: Boolean = true,
    val bigCandyMode: Boolean = false,
    val reducedMotion: Boolean = false
)
