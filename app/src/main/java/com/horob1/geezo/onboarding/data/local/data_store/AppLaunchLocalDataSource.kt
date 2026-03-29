package com.horob1.geezo.onboarding.data.local.data_store

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppLaunchLocalDataSource(
    private val context: Context
) {
    fun isFirstRun(): Flow<Boolean> {
        return context.appLaunchDataStore.data.map { preferences ->
            preferences[IS_FIRST_RUN_KEY] ?: true
        }
    }

    suspend fun setFirstRunCompleted() {
        context.appLaunchDataStore.edit { preferences ->
            preferences[IS_FIRST_RUN_KEY] = false
        }
    }

    private companion object {
        private val Context.appLaunchDataStore by preferencesDataStore(name = "app_launch")
        private val IS_FIRST_RUN_KEY: Preferences.Key<Boolean> =
            booleanPreferencesKey("is_first_run")
    }
}
