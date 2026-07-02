package com.dineverse.ai.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {
}
