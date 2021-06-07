package com.shaun.spotonmusic.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.lifecycle.MutableLiveData
import androidx.palette.graphics.Palette
import com.shaun.spotonmusic.ui.theme.green
import kaaes.spotify.webapi.android.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

fun getImageUrl(list: List<String>, demand: Int): String {


    when (list.size) {
        3 -> {
            if (demand == 0)
                return list[2]
            if (demand == 1)
                return list[1]
            return list[0]
        }
        2 -> {
            return if (demand == 2)
                list[0]
            else list[1]
        }
        1 -> {
            return list[0]
        }
        else -> return ""
    }

}

//
//fun getHexColor(color: ArrayList<Int>): Int {
//    return Color.rgb(color[0], color[1], color[2])
//}