package com.shaun.spotonmusic.utils

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

fun getMonth(month: Int): String? {
    return DateFormatSymbols().months[month - 1]
}


fun dateToString(date: String): String {
    val separated = date.split("-")

    return """
        ${getMonth(separated[1].toInt())} ${separated[2]},${separated[0]}
    """.trimIndent()

}

//
//fun getHexColor(color: ArrayList<Int>): Int {
//    return Color.rgb(color[0], color[1], color[2])
//}