package com.shaun.spotonmusic.presentation.ui.components.albumcomponents

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import kotlin.math.min

@Composable
fun AnimatedToolBar(album: String, scrollState: LazyListState ) {

    val dynamicAlpha =
        if (scrollState.firstVisibleItemIndex < 1) {

            ((min(scrollState.firstVisibleItemScrollOffset, 400) * 0.2) / 100).toFloat()
        } else {
            0.99f
        }

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        spotifyDarkBlack, spotifyDarkBlack
                    )
                ), alpha = dynamicAlpha
            )
    ) {

        Row(
            Modifier
                .padding(top = 40.dp,start = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack, tint = MaterialTheme.colors.onSurface,
                contentDescription = null,
            )


            Text(
                text = album,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .padding(16.dp)
                    .alpha(
                        dynamicAlpha
                    )
            )


            Icon(
                imageVector = Icons.Default.MoreVert, tint = MaterialTheme.colors.onSurface,
                contentDescription = null,
            )


        }
    }
}

@Composable
fun TopSectionOverlay(scrollState: LazyListState) {


    val dynamicAlpha =
        if (scrollState.firstVisibleItemIndex < 1) {

            ((min(scrollState.firstVisibleItemScrollOffset, 400) * 0.2) / 100).toFloat()
        } else {
            0.9f
        }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)

            .background(
                MaterialTheme.colors.surface.copy(
                    alpha = animateFloatAsState(dynamicAlpha).value
                )
            )
    )
}

