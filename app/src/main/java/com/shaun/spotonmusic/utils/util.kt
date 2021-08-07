package com.shaun.spotonmusic.utils

import kaaes.spotify.webapi.android.models.ArtistSimple
import kaaes.spotify.webapi.android.models.Image
import java.text.DateFormatSymbols
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

fun List<Image>.toListString(): List<String> {

    return this.map { it.url }

}

fun getImageUrl(list: List<String>?, demand: Int/* refers size, 0 for smallest,1 for medium,2 for largest*/): String {

    list?.let {
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
    return ""

}

fun getMonth(month: Int): String? {
    return DateFormatSymbols().months[month - 1]
}


fun dateToString(date: String): String {
    val separated = date.split("-")

    return """
        ${getMonth(separated[1].toInt())} ${separated[2]},${separated[0]}
    """.trimIndent()

}


fun getArtistName(artist: MutableList<ArtistSimple>): String {

    val list = artist.map {
        it.name
    }


    var artistName = ""

    list.forEachIndexed { index, s ->

        artistName += s

        if (index != list.size - 1)
            artistName += ","
    }
    return artistName

}