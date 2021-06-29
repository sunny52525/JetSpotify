package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.R

@Composable
fun AlbumArt(albumArtLink: String?) {
    Row(
        Modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberGlidePainter(request = albumArtLink),
            contentDescription = null,
            Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

    }
}