package com.shaun.spotonmusic.presentation.ui.components.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.presentation.ui.components.playlist.ShuffleButton
import java.util.ArrayList

@ExperimentalMaterialApi
@Composable

fun AlbumTopSection(
    image: String,
    title: String,
    owner: String,
    year: String,
    colorShaded: ArrayList<Color>?,
    shuffleClicked: () -> Unit = {}
) {

    Column(
        modifier = Modifier

            .background(
                Brush.verticalGradient(colorShaded?.toList()!!)
            )
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(70.dp))
        Image(
            painter = rememberGlidePainter(request = image),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Album by $owner•$year",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))
        ShuffleButton(shuffleClicked = {
            shuffleClicked()
        }, modifier = Modifier.align(Alignment.CenterHorizontally))
    }

}