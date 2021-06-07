package com.shaun.spotonmusic.presentation.ui.components.albumcomponents

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistTrack

@ExperimentalFoundationApi
@Composable
fun SongList(
    scrollState: LazyListState,
    tracks: Pager<PlaylistTrack?>?
) {
    LazyColumn(state = scrollState) {
        item {

            Spacer(
                modifier = Modifier
                    .height(300.dp)  .clickable {
                        Log.d("TAG", "BoxTopSection: CLIEDC")
                    }

            )
        }
        stickyHeader {
            Spacer(modifier = Modifier.height(50.dp))
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
            ) {
                ShuffleButton()
            }
        }
        tracks?.let { tracksPager ->
            items(tracksPager?.items) {

                if (it != null) {
                    Log.d("TAG", "SongList: ${it.track.id}, ${it.track.name}")
                    SpotifySongListItem(it.track.name)
                }
            }
        }
        item {

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
