package com.shaun.spotonmusic

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.first
import kaaes.spotify.webapi.android.models.*
import java.util.*
import java.io.IOException

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.graphics.Color

import java.io.InputStream

import java.net.HttpURLConnection

import java.net.URL
import kotlin.collections.ArrayList


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

fun getBitmapFromURL(src: String?): Bitmap? {
    return try {
        val url = URL(src)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
fun getHexColor(color: ArrayList<Int>): Int {
    return Color.rgb(color[0], color[1], color[2])
}