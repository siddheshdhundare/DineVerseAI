package com.dineverse.ai.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dineverse.ai.core.constants.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = Constants.SESSION_PREFS)

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val USER_ID = stringPreferencesKey(Constants.USER_ID_KEY)
        private val USER_ROLE = stringPreferencesKey(Constants.USER_ROLE_KEY)
        private val IS_LOGGED_IN = booleanPreferencesKey(Constants.IS_LOGGED_IN_KEY)
    }

    suspend fun saveSession(userId: String, role: String) {
        Timber.d("Saving session for user: $userId with role: $role")
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_ROLE] = role
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        Timber.d("Clearing session")
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }.first()
    }

    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE] = role
        }
    }

    fun getRole(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ROLE]
        }
    }
}
