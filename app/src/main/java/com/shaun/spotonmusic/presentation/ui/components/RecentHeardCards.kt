package com.shaun.spotonmusic.presentation.ui.components

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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.ui.theme.green
import com.shaun.spotonmusic.ui.theme.spotifyGray


@Composable
fun RecentHeardCards(
    onCardClicked: () -> Unit = {},
    title: String = "Liked Song",
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
                    painter = painterResource(id = R.drawable.spotify_liked),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
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
fun RecentHeardBlock() {

    Column(Modifier.padding(top = 20.dp)) {
        Spacer(modifier = Modifier.width(20.dp))
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
                    .weight(0.9f)
            )
            Spacer(modifier = Modifier.width(5.dp))
            RecentHeardCards(
                onCardClicked = {
                }, modifier = Modifier
                    .weight(0.9f)
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
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
                    .weight(0.9f)
            )
            Spacer(modifier = Modifier.width(5.dp))
            RecentHeardCards(
                onCardClicked = {
                }, modifier = Modifier
                    .weight(0.9f)
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
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
                    .weight(0.9f)
            )
            Spacer(modifier = Modifier.width(5.dp))
            RecentHeardCards(
                onCardClicked = {
                }, modifier = Modifier
                    .weight(0.9f)
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
    }

}