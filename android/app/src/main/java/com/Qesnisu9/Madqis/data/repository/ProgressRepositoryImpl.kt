package com.Qesnisu9.Madqis.data.repository

import com.Qesnisu9.Madqis.data.local.PreferencesStore
import com.Qesnisu9.Madqis.domain.model.PlayerProgress
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository

class ProgressRepositoryImpl(private val store: PreferencesStore) : ProgressRepository {

    override fun load(): PlayerProgress = PlayerProgress(
        bestScore = store.readInt(PreferencesStore.KEY_BEST_SCORE, 0),
        totalScore = store.readInt(PreferencesStore.KEY_TOTAL_SCORE, 0),
        routesFinished = store.readInt(PreferencesStore.KEY_ROUTES_FINISHED, 0),
        treatsCollected = store.readInt(PreferencesStore.KEY_TREATS, 0),
        bestCombo = store.readInt(PreferencesStore.KEY_BEST_COMBO, 0),
        bestSegment = store.readInt(PreferencesStore.KEY_BEST_SEGMENT, 0),
        sugarCoins = store.readInt(PreferencesStore.KEY_SUGAR_COINS, 0),
        claimedRewards = store.readIdSet(PreferencesStore.KEY_CLAIMED_REWARDS),
        unlockedCandies = store.readIdSet(PreferencesStore.KEY_UNLOCKED_CANDIES),
        dailyClaimedDayOfYear = store.readInt(PreferencesStore.KEY_DAILY_CLAIMED, -1)
    )

    override fun save(progress: PlayerProgress) {
        store.writeInt(PreferencesStore.KEY_BEST_SCORE, progress.bestScore)
        store.writeInt(PreferencesStore.KEY_TOTAL_SCORE, progress.totalScore)
        store.writeInt(PreferencesStore.KEY_ROUTES_FINISHED, progress.routesFinished)
        store.writeInt(PreferencesStore.KEY_TREATS, progress.treatsCollected)
        store.writeInt(PreferencesStore.KEY_BEST_COMBO, progress.bestCombo)
        store.writeInt(PreferencesStore.KEY_BEST_SEGMENT, progress.bestSegment)
        store.writeInt(PreferencesStore.KEY_SUGAR_COINS, progress.sugarCoins)
        store.writeIdSet(PreferencesStore.KEY_CLAIMED_REWARDS, progress.claimedRewards)
        store.writeIdSet(PreferencesStore.KEY_UNLOCKED_CANDIES, progress.unlockedCandies)
        store.writeInt(PreferencesStore.KEY_DAILY_CLAIMED, progress.dailyClaimedDayOfYear)
    }

    override fun reset() {
        save(PlayerProgress())
    }
}
