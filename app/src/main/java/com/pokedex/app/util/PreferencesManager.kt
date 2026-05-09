package com.pokedex.app.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("pokedex_prefs", Context.MODE_PRIVATE)

    var lastSyncTimestamp: Long
        get() = prefs.getLong("last_sync", 0L)
        set(value) = prefs.edit().putLong("last_sync", value).apply()

    fun needsSync(): Boolean {
        val sevenDays = 7L * 24 * 60 * 60 * 1000
        return System.currentTimeMillis() - lastSyncTimestamp > sevenDays
    }

    var dataVersion: Int
        get() = prefs.getInt("data_version", 0)
        set(value) = prefs.edit().putInt("data_version", value).apply()

    fun needsDataVersionSync(): Boolean = dataVersion < CURRENT_DATA_VERSION

    companion object { const val CURRENT_DATA_VERSION = 7 }
}
