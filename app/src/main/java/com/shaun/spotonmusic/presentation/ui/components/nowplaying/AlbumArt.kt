package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.R

@Composable
fun AlbumArt(){
    Row(
        Modifier
            .fillMaxHeight(0.7f)
            .border(1.dp, Color.Red)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.lana),
            contentDescription = null,
            Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

    }
}