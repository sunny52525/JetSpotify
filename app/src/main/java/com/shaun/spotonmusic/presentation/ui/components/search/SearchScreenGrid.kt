package com.shaun.spotonmusic.presentation.ui.components.search


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
import com.google.accompanist.glide.rememberGlidePainter

@Composable
fun SearchGrid(
    albumId: Pair<String, String>,
    imageUrl: Pair<String, String>,
    title: Pair<String, String>,
    color: Pair<Color?, Color?>,
    onCardClicked: (String,Int) -> Unit
) {

    Row() {
        Card(
            Modifier
                .padding(10.dp)
                .weight(1f)
                .fillMaxWidth(.5f)
                .height(100.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            RotatedImage(
                imageUrl = imageUrl.first,
                title = title.first,
                color = color.first,
                onCardClicked = {
                    onCardClicked(albumId.first,0)
                })

        }
        Card(
            Modifier
                .padding(10.dp)
                .weight(1f)
                .fillMaxWidth(.5f)
                .height(100.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            RotatedImage(
                imageUrl = imageUrl.second,
                title = title.second,
                color = color.second,
                onCardClicked = {
                    onCardClicked(albumId.second,1)
                })

        }

    }

}

@Composable
fun RotatedImage(
    title: String = "Pop",
    imageUrl: String,
    color: Color?,
    onCardClicked: () -> Unit
) {


    color?.let {
        Modifier
            .clickable(onClick = {
                onCardClicked()
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

private const val TAG = "SearchScreenGrid"