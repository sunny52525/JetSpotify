package com.shaun.spotonmusic.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import kaaes.spotify.webapi.android.models.NewReleases
import kaaes.spotify.webapi.android.models.PlaylistsPager

@Composable
fun SuggestionsRow(title: String = "Throwback", playlistsPager: PlaylistsPager) {

    Column(Modifier.padding(top = 30.dp)) {


        Text(
            text = title, textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            fontSize = 23.sp,
            color = Color.White

        )
        LazyRow {


            playlistsPager.playlists?.let {

                it.items.forEachIndexed { index, it ->
                    item {

                        SuggestionCard(
                            0,
                            imageUrl = it.images[0].url,
                            it.name,
                            paddingValues = if (index == 0) 20 else 10
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun NewReleasesRow(title: String = "Throwback", newReleases: NewReleases) {

    Log.d("TAG", "SuggestionsRow: ")
    Column(Modifier.padding(top = 30.dp)) {


        Text(
            text = title, textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            fontSize = 23.sp,
            color = Color.White
        )
        LazyRow {


            newReleases.albums?.items?.let {


                it.forEachIndexed { index, it ->

                    item {

                        SuggestionCard(
                            0,
                            imageUrl = it.images[0].url,
                            it.name,
                            paddingValues = if (index == 0) 20 else 10
                        )
                    }
                }
            }

        }
    }
}


//@Preview
@Composable
fun SuggestionCard(
    cornerRadius: Int = 0,
    imageUrl: String,
    title: String,
    size: Int = 170,
    paddingValues: Int
) {


    Column(
        Modifier
            .padding(bottom = 10.dp, top = 10.dp, start = paddingValues.dp)
            .clickable {

            }) {
        Card(shape = RoundedCornerShape(cornerRadius.dp)) {


            Image(
                painter = rememberGlidePainter(request = imageUrl),
                contentDescription = "",

                modifier =
                Modifier.size(size.dp)
            )
        }
        Text(
            text = title,
            color = Color.Gray,
            textAlign = TextAlign.Left,
            fontSize = 13.sp,
            modifier = Modifier.width(size.dp)
        )

    }


}
