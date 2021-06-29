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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class PaletteExtractor() {


    fun getColorFromSwatch(imageUrl: String): MutableLiveData<Color> {


        val color = MutableLiveData<Color>()
        if (imageUrl.isEmpty()) {
            color.postValue(green)
        } else {
            GlobalScope.launch {
                val bitmap = getBitmapFromURL(imageUrl)
                withContext(Dispatchers.Main) {
                    if (bitmap != null && !bitmap.isRecycled) {
                        val palette: Palette = Palette.from(bitmap).generate()


                        val dominant = palette.dominantSwatch?.rgb?.let { color ->
                            arrayListOf(color.red, color.green, color.blue)

                        }
                        val composeColor =
                            dominant?.get(0)?.let { it1 ->
                                Color(
                                    red = it1,
                                    green = dominant[1],
                                    blue = dominant[2]
                                )
                            }
                        if (composeColor != null)
                            color.postValue(composeColor)
                    }

                }
            }
        }

        return color
    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
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

    companion object {
        private const val TAG = "PaletteExtractor"
    }
}