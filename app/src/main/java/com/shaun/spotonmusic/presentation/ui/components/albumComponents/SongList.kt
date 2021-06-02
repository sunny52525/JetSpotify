package com.shaun.spotonmusic.presentation.ui.components.albumComponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.presentation.ui.components.libraryComponents.Chip
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.TrackSimple

val items =
    arrayListOf(
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help",
        "Help"
    )

@ExperimentalFoundationApi
@Composable
fun SongList(
    scrollState: LazyListState,
    surfaceGradient: List<Color>,
    tracks: Pager<TrackSimple?>?
) {
    LazyColumn(state = scrollState) {
        item {

            Spacer(
                modifier = Modifier
                    .height(360.dp)
                    .background(spotifyDarkBlack)
            )
        }
        stickyHeader {
            Spacer(modifier = Modifier.height(50.dp))
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
            ) {
                Chip()
            }
        }
        tracks?.let { tracksPager ->
            items(tracksPager.items) {
                if (it != null) {
                    SpotifySongListItem(album = it.name)
                }
            }
        }
        item {

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
