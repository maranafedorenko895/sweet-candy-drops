package com.Qesnisu9.Madqis.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun writeInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun readBool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun writeBool(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun readIdSet(key: String): Set<Int> {
        val raw = prefs.getString(key, "").orEmpty()
        if (raw.isBlank()) return emptySet()
        return raw.split(SEPARATOR).mapNotNull { it.trim().toIntOrNull() }.toSet()
    }

    fun writeIdSet(key: String, value: Set<Int>) {
        prefs.edit().putString(key, value.sorted().joinToString(SEPARATOR)).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val STORE_NAME = "sweet_candy_drops"
        const val KEY_BEST_SCORE = "best_score"
        const val KEY_TOTAL_SCORE = "total_score"
        const val KEY_ROUTES_FINISHED = "routes_finished"
        const val KEY_TREATS = "treats_collected"
        const val KEY_BEST_COMBO = "best_combo"
        const val KEY_BEST_SEGMENT = "best_segment"
        const val KEY_SUGAR_COINS = "sugar_coins"
        const val KEY_CLAIMED_REWARDS = "claimed_rewards"
        const val KEY_UNLOCKED_CANDIES = "unlocked_candies"
        const val KEY_DAILY_CLAIMED = "daily_claimed_day_of_year"
        const val KEY_SOUND = "sound_cues"
        const val KEY_HAPTICS = "haptics"
        const val KEY_BIG_CANDY = "big_candy_mode"
        const val KEY_REDUCED_MOTION = "reduced_motion"
        private const val SEPARATOR = ","
    }
}
