package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SeekBar(){

    Column(Modifier) {
        Card(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.Unspecified,
            modifier = Modifier.padding(start=20.dp,end=20.dp,bottom=0.dp,top = 0.dp)
        ) {
            Row(
                Modifier
                    .height(7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .background(Color.White)
                        .height(3.dp)
                ) {

                }
                Card(
                    shape = CircleShape,
                    contentColor = Color.White,
                    modifier = Modifier
                        .size(7.dp)

                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {

                    }


                }
            }
        }
    }
}