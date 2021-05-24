package com.shaun.spotonmusic.presentation.ui.components.homeComponents

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


    if(data?.items==null || image.isNullOrEmpty())
        return

    Column(Modifier.padding(top = 30.dp)) {

        CustomizedHeading(image, title, data?.items?.get(0)?.artists?.get(0)?.name)

        LazyRow {
            data?.items?.let {
                it.forEachIndexed { index, its ->
                    item {
                        CustomizedSuggestionCard(
                            album = Pair(its.images[0].url, its.name),
                            paddingValues = if (index == 0) 20 else 10
                        )
                    }
                }
            }

        }


    }
}

@Composable
fun CustomizedHeading(
    image: String,
    title: String,
    data: String?
) {
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
        Column {
            Text(text = title, fontSize = 12.sp, modifier = Modifier.padding(start = 10.dp))
            if (data != null) {


                Text(
                    text = data,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )

            }
        }
    }
}


@Composable
fun CustomizedSuggestionCard(
    cornerRadius: Int = 0,
    album: Pair<String, String>,
    paddingValues: Int
) {


    Column(Modifier.padding(start = paddingValues.dp, bottom = 10.dp, top = 10.dp)) {
        Card(shape = RoundedCornerShape(cornerRadius.dp)) {


            Image(
                painter = rememberGlidePainter(request = album.first),
                contentDescription = "",
                modifier =
                Modifier
                    .size(170.dp)

            )
        }
        Text(
            text = album.second,
            color = Color.Gray,
            textAlign = TextAlign.Left,
            fontSize = 13.sp,
            modifier = Modifier.width(170.dp)

        )

    }


}
