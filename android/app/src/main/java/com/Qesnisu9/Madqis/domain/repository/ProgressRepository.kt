package com.Qesnisu9.Madqis.domain.repository

import com.Qesnisu9.Madqis.domain.model.PlayerProgress

interface ProgressRepository {
    fun load(): PlayerProgress
    fun save(progress: PlayerProgress)
    fun reset()
}
