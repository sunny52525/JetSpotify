package com.shaun.spotonmusic.database.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.first

 suspend fun save(key: String, value: String,dataStore: DataStore<Preferences>) {
    val dataStoreKey = preferencesKey<String>(key)

    dataStore.edit { settings ->
        settings[dataStoreKey] = value
    }
}

 suspend fun read(key: String,dataStore: DataStore<Preferences>): String? {
    val dataStoreKey = preferencesKey<String>(key)
    val preferences = dataStore.data.first()
    return preferences[dataStoreKey]
}