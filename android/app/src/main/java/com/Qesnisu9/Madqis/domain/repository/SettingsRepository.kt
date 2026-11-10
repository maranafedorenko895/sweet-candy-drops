package com.Qesnisu9.Madqis.domain.repository

import com.Qesnisu9.Madqis.domain.model.AppSettings

interface SettingsRepository {
    fun load(): AppSettings
    fun save(settings: AppSettings)
}
