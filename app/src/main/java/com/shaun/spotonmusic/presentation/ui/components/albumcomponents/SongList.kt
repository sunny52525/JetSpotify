package com.shaun.spotonmusic.presentation.ui.components.albumcomponents

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
import com.shaun.spotonmusic.presentation.ui.components.librarycomponents.Chip
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistTrack

@ExperimentalFoundationApi
@Composable
fun SongList(
    scrollState: LazyListState,
    surfaceGradient: ArrayList<Color>?,
    tracks: Pager<PlaylistTrack?>?
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
            items(tracksPager?.items) {
                if (it != null) {
                    SpotifySongListItem(it.track.name)
                }
            }
        }
        item {

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
