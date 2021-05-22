package com.shaun.spotonmusic.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.model.RecentlyPlayed
import com.shaun.spotonmusic.ui.theme.spotifyGray
import java.lang.Integer.min


@Composable
fun RecentHeardCards(
    onCardClicked: () -> Unit = {},
    data: Pair<String, String>,
    modifier: Modifier
) {
    Card(shape = RoundedCornerShape(7.dp), modifier = modifier) {
        Column(
            modifier = Modifier
                .clickable {

                    onCardClicked()
                }
                .fillMaxHeight()
                .fillMaxWidth(.5f)
                .height(50.dp)
                .background(color = spotifyGray)
                .border(color = spotifyGray, width = 0.1.dp)


        ) {


            Row {
                Image(
                    painter = rememberGlidePainter(request = data.second),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = data.first,
                    textAlign = TextAlign.Left,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)

                )
            }
        }

    }

}

@Composable
fun RecentHeardBlock(recentlyPlayed: RecentlyPlayed) {

    val length = recentlyPlayed.items.size

    val name = recentlyPlayed.items.subList(0, 6.coerceAtMost(length)).map {
        if (it.track.album.name.length > 15) {
            Pair(it.track.album.name.substring(0, 14) + "...", it.track.album.images[0].url)
        } else {
            Pair(it.track.album.name, it.track.album.images[0].url)
        }
    }



    Column(Modifier.padding(top = 20.dp)) {


        Spacer(modifier = Modifier.width(20.dp))
        if (length >= 2) {
            Row(

                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(55.dp)


            ) {
                Spacer(modifier = Modifier.width(20.dp))

                RecentHeardCards(
                    onCardClicked = {
                        Log.d("TAG", "RecentHeardBlock: CLICKED")

                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[0]
                )
                Spacer(modifier = Modifier.width(5.dp))
                RecentHeardCards(
                    onCardClicked = {
                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[1]
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
        if (length >= 4) {
            Row(

                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(65.dp)
                    .padding(top = 10.dp)


            ) {
                Spacer(modifier = Modifier.width(20.dp))

                RecentHeardCards(
                    onCardClicked = {
                        Log.d("TAG", "RecentHeardBlock: CLICKED")

                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[2]
                )
                Spacer(modifier = Modifier.width(5.dp))
                RecentHeardCards(
                    onCardClicked = {
                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[3]
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
        if (length >= 6) {
            Row(

                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(65.dp)
                    .padding(top = 10.dp)


            ) {
                Spacer(modifier = Modifier.width(20.dp))

                RecentHeardCards(
                    onCardClicked = {
                        Log.d("TAG", "RecentHeardBlock: CLICKED")

                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[4]
                )
                Spacer(modifier = Modifier.width(5.dp))
                RecentHeardCards(
                    onCardClicked = {
                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[5]
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }

}