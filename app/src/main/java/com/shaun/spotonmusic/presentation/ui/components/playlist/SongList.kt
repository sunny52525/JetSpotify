package com.shaun.spotonmusic.presentation.ui.components.playlist

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.utils.getArtistName
import com.shaun.spotonmusic.utils.getImageUrl
import com.shaun.spotonmusic.viewmodel.PlaylistDetailViewModel
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistTrack
import kotlin.math.min

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SongList(
    scrollState: LazyListState,
    tracks: Pager<PlaylistTrack?>?,
    onFollowClicked: () -> Unit,
    shuffleClicked: () -> Unit,
    onSongClicked: (String) -> Unit,
    likedSongs: BooleanArray?
) {


    LazyColumn(state = scrollState) {
        item {

            Spacer(
                modifier = Modifier
                    .height(350.dp)
                    .clickable {
                        Log.d("TAG", "BoxTopSection: CLIEDC")
                    }

            )
        }
        stickyHeader {
            Spacer(modifier = Modifier
                .height(85.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onFollowClicked()
                        }
                    )
                }

            )
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxWidth()

            ) {
                ShuffleButton(shuffleClicked = {
                    shuffleClicked()
                })
            }
        }
        tracks?.let { tracksPager ->
            itemsIndexed(tracksPager.items) { index, track ->

                if (track != null) {
                    SpotifySongListItem(
                        album = track.track.name,
                        liked = likedSongs?.get(index = min(48,index)) ?: false,
                        singer = getArtistName(track.track.artists),
                        explicit = track.track.explicit,
                        imageUrl = getImageUrl(track.track.album.images.map { url ->
                            url.url
                        }, 0),
                        onSongClicked = {
                            onSongClicked(track.track.uri)
                        },
                    )

                }
            }

        }

    }
}
