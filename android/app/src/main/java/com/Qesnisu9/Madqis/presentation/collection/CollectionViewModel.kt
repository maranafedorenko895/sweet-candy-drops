package com.Qesnisu9.Madqis.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qesnisu9.Madqis.data.sample.SampleData
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val entries = SampleData.candyCollection.map { candy ->
                CollectionEntry(
                    candy = candy,
                    unlocked = progress.unlockedCandies.contains(candy.id) ||
                        progress.treatsCollected >= candy.treatThreshold
                )
            }
            _state.update {
                it.copy(
                    loading = false,
                    entries = entries,
                    unlockedCount = entries.count { entry -> entry.unlocked },
                    total = entries.size
                )
            }
        }
    }
}
