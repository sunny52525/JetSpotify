package com.shaun.spotonmusic.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kaaes.spotify.webapi.android.models.PlaylistSimple
import kaaes.spotify.webapi.android.models.PlaylistsPager

@Composable
fun SuggestionsRow(title: String = "Throwback", playlistsPager: PlaylistsPager) {

    Log.d("TAG", "SuggestionsRow: ")
    Column(Modifier.padding(start = 20.dp, top = 30.dp)) {


        Text(
            text = title, textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 23.sp,
            color = Color.White
        )
        LazyRow() {


            playlistsPager.playlists?.let {

                it.items.forEach {
                    item {

                        SuggestionCard(0, it)
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
    playlistSimple: PlaylistSimple
) {


    Column(Modifier.padding(end = 10.dp, bottom = 10.dp, top = 10.dp)) {
        Card(shape = RoundedCornerShape(cornerRadius.dp)) {


            Image(
                painter = rememberGlidePainter(request = playlistSimple.images[0].url),
                contentDescription = "",

                modifier =
                Modifier.size(170.dp)
            )
        }
        Text(
            text = playlistSimple.name,
            color = Color.Gray,
            textAlign = TextAlign.Left
        )

    }
}