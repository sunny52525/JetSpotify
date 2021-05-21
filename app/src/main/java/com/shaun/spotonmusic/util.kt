package com.shaun.spotonmusic

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.first
import android.widget.Toast
import kaaes.spotify.webapi.android.models.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


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

fun FeaturedPlaylists.toPlayListPager(): PlaylistsPager {
    val pager = PlaylistsPager()
    pager.playlists = this.playlists
    return pager
}




fun getGreeting(): String {
    val c: Calendar = Calendar.getInstance()

    return when (c.get(Calendar.HOUR_OF_DAY)) {
        in 6..11 -> {
            "Good Morning"
        }
        in 12..15 -> {
            "Good Afternoon"
        }
        in 16..24 -> {
            "Good Evening"
        }
        else -> {
            "Good Night"
        }
    }
}