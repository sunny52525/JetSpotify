package com.shaun.spotonmusic.presentation.ui.components.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack

@Composable

@Preview
fun ArtistRow(image: String = "", singer: String = "Lana Del Ray") {
    Row(
        Modifier
            .fillMaxWidth()
            .background(spotifyDarkBlack)
    ) {

        Spacer(modifier = Modifier.width(10.dp))
        Card(shape = RoundedCornerShape(50)) {
            Image(
                painter = rememberGlidePainter(request = image),
                contentDescription = null,

                modifier = Modifier.size(44.dp)
            )


        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = singer,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}