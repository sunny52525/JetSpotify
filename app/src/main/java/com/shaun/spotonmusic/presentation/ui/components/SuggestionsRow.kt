package com.shaun.spotonmusic.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.R

@Composable
fun SuggestionsRow() {

    Column(Modifier.padding(start = 20.dp, top = 30.dp)) {


        Text(
            text = "Throwback", textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 23.sp,
            color = Color.White
        )
        LazyRow() {
            repeat(
                10
            ) {
                item {
                    SuggestionCard()
                }
            }
        }
    }
}


@Preview
@Composable
fun SuggestionCard(
    cornerRadius: Int = 0
) {


    Column(Modifier.padding(end = 10.dp, bottom = 10.dp, top = 10.dp)) {
        Card(shape = RoundedCornerShape(cornerRadius.dp)) {
            Image(
                painter = painterResource(id = R.drawable.album_art),
                contentDescription = "",

                modifier =
                Modifier.size(170.dp)
            )
        }
        Text(
            text = "Lana Del Ray\nGirl in red",
            color = Color.Gray,
            textAlign = TextAlign.Left
        )

    }
}