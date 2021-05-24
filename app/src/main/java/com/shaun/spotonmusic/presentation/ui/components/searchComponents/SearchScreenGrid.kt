package com.shaun.spotonmusic.presentation.ui.components.searchComponents


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.lifecycle.MutableLiveData
import androidx.palette.graphics.Palette
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.getBitmapFromURL
import com.shaun.spotonmusic.ui.theme.green
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SearchGrid(
    albumId: Pair<String, String>,
    imageUrl: Pair<String, String>,
    title: Pair<String, String>,
    heading: String = "Your top genres",
    color: Pair<Color?, Color?>
) {

    Row {
        Card(
            Modifier
                .padding(10.dp)
                .weight(1f)
                .fillMaxWidth(.5f)
                .height(100.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            RotatedImage(imageUrl = imageUrl.first, title = title.first, color = color.first)

        }
        Card(
            Modifier
                .padding(10.dp)
                .weight(1f)
                .fillMaxWidth(.5f)
                .height(100.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            RotatedImage(imageUrl = imageUrl.second, title = title.second, color = color.second)

        }

    }

}

@Composable
fun RotatedImage(title: String = "Pop", imageUrl: String, color: Color?) {


    color?.let {
        Modifier
            .clickable(onClick = {
            })
            .height(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = it)
    }?.let {
        Row(
        modifier = it,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.White,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)

                .fillMaxWidth(0.6f),
        )
        Image(
            painter = rememberGlidePainter(request = imageUrl),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Bottom)
                .graphicsLayer(translationX = 40f, rotationZ = 32f, shadowElevation = 16f)
        )
    }
    }
}

fun getColorFromSwatch(imageUrl: String): MutableLiveData<Color> {

    Log.d(TAG, "getColorFromSwatch: $imageUrl")
    val color = MutableLiveData<Color>()
    if (imageUrl.isEmpty()) {
        Log.d(TAG, "getColorFromSwatch: why")
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
                    Log.d(TAG, "getColorFromSwatch: $dominant")
                    val composeColor =
                        dominant?.get(0)?.let { it1 ->
                            Color(
                                red = it1,
                                green = dominant[1],
                                blue = dominant[2]
                            )
                        }
                    Log.d(TAG, "getColorFromSwatch: $composeColor")
                    if (composeColor != null)
                        color.postValue(composeColor)
                }

            }
        }
    }

    return color
}

private const val TAG = "SearchScreenGrid"