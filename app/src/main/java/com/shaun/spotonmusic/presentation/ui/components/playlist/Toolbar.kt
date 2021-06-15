package com.shaun.spotonmusic.presentation.ui.components.playlist

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.presentation.ui.components.album.TopBar
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import kotlin.math.min

@Composable
fun AnimatedToolBar(album: String, scrollState: LazyListState) {

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

        TopBar(showHeart = false, alpha = dynamicAlpha,title=album)
//        Row(
//            Modifier
//                .padding(top = 40.dp,start = 8.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack, tint = MaterialTheme.colors.onSurface,
//                contentDescription = null,
//            )
//
//
//            Text(
//                text = album,
//                color = MaterialTheme.colors.onSurface,
//                modifier = Modifier
//                    .padding(16.dp)
//                    .alpha(
//                        dynamicAlpha
//                    )
//            )
//
//
//            Icon(
//                imageVector = Icons.Default.MoreVert, tint = MaterialTheme.colors.onSurface,
//                contentDescription = null,
//            )
//
//
//        }
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

