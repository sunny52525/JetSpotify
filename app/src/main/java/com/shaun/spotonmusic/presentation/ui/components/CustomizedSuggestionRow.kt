package com.shaun.spotonmusic.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.Pager


@Composable
fun FavouriteArtistSongs(title: String, data: Pager<Album>?, image: String) {
    Column(Modifier.padding(top = 30.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        ) {
            Card(shape = RoundedCornerShape(50)) {
                Image(
                    painter = rememberGlidePainter(request = image),
                    contentDescription = null,

                    modifier = Modifier.size(32.dp)
                )
            }
            Column() {
                Text(text = title, fontSize = 12.sp, modifier = Modifier.padding(start = 10.dp))
                if (data != null) {
                    data.items?.get(0)?.artists?.get(0)?.let {

                        Text(
                            text = it.name,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 10.dp)
                        )

                    }
                }
            }
        }

        LazyRow {
            data?.items?.let {
                it.forEachIndexed { index, its ->
                    item {
                        CustomizedSuggestionCard(
                            album = its,
                            paddingValues = if (index == 0) 20 else 10
                        )
                    }
                }
            }

        }


    }
}


@Composable
fun CustomizedSuggestionCard(
    cornerRadius: Int = 0,
    album: Album,
    paddingValues: Int
) {


    Column(Modifier.padding(start = paddingValues.dp, bottom = 10.dp, top = 10.dp)) {
        Card(shape = RoundedCornerShape(cornerRadius.dp)) {


            Image(
                painter = rememberGlidePainter(request = album.images[0].url),
                contentDescription = "",
                modifier =
                Modifier
                    .size(170.dp)

            )
        }
        Text(
            text = album.name,
            color = Color.Gray,
            textAlign = TextAlign.Left,
            fontSize = 13.sp

        )

    }


}
