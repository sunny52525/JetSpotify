package com.shaun.spotonmusic.presentation.ui.components.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.shaun.spotonmusic.utils.getImageUrl


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
fun RecentHeardBlock(recentlyPlayed: List<SpotOnMusicModel>, onCardClicked: (String) -> Unit) {



    if (recentlyPlayed.isEmpty())
        return

    val recentlyPlayedArray: List<SpotOnMusicModel> = recentlyPlayed.distinctBy {
        it.title
    }


    val length = recentlyPlayedArray.size

    val name = recentlyPlayedArray.subList(0, 6.coerceAtMost(length)).map {
        if (it.title.length > 15) {
            Pair(it.title.substring(0, 14) + "...", getImageUrl(it.imageUrls, 1))
        } else {
            Pair(it.title, getImageUrl(it.imageUrls, 1))
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

                        Log.d("TAG", "RecentHeardBlock: CLICKED 0 ${recentlyPlayed[0].title}")
                        onCardClicked(recentlyPlayedArray[0].id)

                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[0]
                )
                Spacer(modifier = Modifier.width(5.dp))
                RecentHeardCards(
                    onCardClicked = {

                        Log.d("TAG", "RecentHeardBlock: CLICKED 1 ${recentlyPlayed[1].title}")
                        onCardClicked(recentlyPlayedArray[1].id)
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

                        Log.d("TAG", "RecentHeardBlock: CLICKED 2 ${recentlyPlayed[2].title}")
                        onCardClicked(recentlyPlayedArray[2].id)
                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[2]
                )
                Spacer(modifier = Modifier.width(5.dp))
                RecentHeardCards(
                    onCardClicked = {

                        Log.d("TAG", "RecentHeardBlock: CLICKED 3 ${recentlyPlayed[3].title}")
                        onCardClicked(recentlyPlayedArray[3].id)
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

                        Log.d("TAG", "RecentHeardBlock: CLICKED  4 ${recentlyPlayed[4].title}")
                        onCardClicked(recentlyPlayedArray[4].id)
                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[4]
                )
                Spacer(modifier = Modifier.width(5.dp))
                RecentHeardCards(
                    onCardClicked = {
                        Log.d("TAG", "RecentHeardBlock: CLICKED 5 ${recentlyPlayed[5].title}")
                        onCardClicked(recentlyPlayedArray[5].id)
                    }, modifier = Modifier
                        .weight(0.9f),
                    data = name[5]
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }

}