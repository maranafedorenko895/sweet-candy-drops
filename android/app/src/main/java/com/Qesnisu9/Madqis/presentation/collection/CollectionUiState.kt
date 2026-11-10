package com.Qesnisu9.Madqis.presentation.collection

import com.Qesnisu9.Madqis.domain.model.CandyCollectible

data class CollectionEntry(
    val candy: CandyCollectible,
    val unlocked: Boolean
)

data class CollectionUiState(
    val loading: Boolean = true,
    val entries: List<CollectionEntry> = emptyList(),
    val unlockedCount: Int = 0,
    val total: Int = 0
)
