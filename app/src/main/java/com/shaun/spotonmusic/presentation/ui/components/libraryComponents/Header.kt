package com.shaun.spotonmusic.presentation.ui.components.libraryComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.ui.theme.black


@Preview
@Composable
fun Header() {

    Column(
        modifier = Modifier
            .padding(50.dp)
            .background(black)
            .height(100.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Card(shape = RoundedCornerShape(50)) {
                Image(
                    painter = painterResource(id = R.drawable.spotify_liked),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp)
                )
            }



        }
    }
}