package com.shaun.spotonmusic.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kaaes.spotify.webapi.android.models.*
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*





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



fun getImageUrl(list: List<Image>, demand: Int): String {


    when (list.size) {
        3 -> {
            if (demand == 0)
                return list[2].url
            if (demand == 1)
                return list[1].url
            return list[0].url
        }
        2 -> {
            return if (demand == 2)
                list[0].url
            else list[1].url
        }
        1 -> {
            return list[0].url
        }
        else -> return ""
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