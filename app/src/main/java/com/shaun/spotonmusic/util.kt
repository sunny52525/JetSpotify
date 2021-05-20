package com.shaun.spotonmusic

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.first


 suspend fun save(dataStore: DataStore<Preferences>, key: String, value: String) {
    val dataStoreKey = preferencesKey<String>(key)
    dataStore.edit { settings ->
        settings[dataStoreKey] = value
    }
}

 suspend fun read(dataStore: DataStore<Preferences>, key: String): String {
    val dataStoreKey = preferencesKey<String>(key)
    val preferences = dataStore.data.first()
    return preferences[dataStoreKey].toString()
}