package com.Qesnisu9.Madqis.data.repository

import com.Qesnisu9.Madqis.data.local.PreferencesStore
import com.Qesnisu9.Madqis.domain.model.AppSettings
import com.Qesnisu9.Madqis.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val store: PreferencesStore) : SettingsRepository {

    override fun load(): AppSettings = AppSettings(
        soundCues = store.readBool(PreferencesStore.KEY_SOUND, true),
        haptics = store.readBool(PreferencesStore.KEY_HAPTICS, true),
        bigCandyMode = store.readBool(PreferencesStore.KEY_BIG_CANDY, false),
        reducedMotion = store.readBool(PreferencesStore.KEY_REDUCED_MOTION, false)
    )

    override fun save(settings: AppSettings) {
        store.writeBool(PreferencesStore.KEY_SOUND, settings.soundCues)
        store.writeBool(PreferencesStore.KEY_HAPTICS, settings.haptics)
        store.writeBool(PreferencesStore.KEY_BIG_CANDY, settings.bigCandyMode)
        store.writeBool(PreferencesStore.KEY_REDUCED_MOTION, settings.reducedMotion)
    }
}
