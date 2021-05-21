package com.shaun.spotonmusic.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistSimple


@Composable
fun PlaylistRow(title: String = "Throwback", playlistsPager: Pager<PlaylistSimple>) {

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


            playlistsPager.let {

                it.items?.forEachIndexed { index, it ->
                    item {

                        SuggestionCard(
                            imageUrl = it.images[0].url,
                            title = it.name,
                            size = 150,
                            paddingValues = if (index == 0) 20 else 10
                        )
                    }
                }
            }

        }
    }
}
