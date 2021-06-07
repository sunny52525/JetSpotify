package com.shaun.spotonmusic.presentation.ui.components.albumcomponents

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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.utils.getImageUrl
import com.shaun.spotonmusic.viewmodel.MusicDetail
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistTrack

@ExperimentalFoundationApi
@Composable
fun SongList(
    scrollState: LazyListState,
    tracks: Pager<PlaylistTrack?>?,
    onFollowClicked: () -> Unit,
    viewModel: MusicDetail,

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
                .height(90.dp)
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
                ShuffleButton()
            }
        }
        tracks?.let { tracksPager ->
            items(tracksPager?.items) {
                //TODO(DO SOMETHING ABOUT IT)
//                val liked: Boolean by viewModel.hasLikedThisSong(it?.track?.id.toString())
//                    .observeAsState(initial = false)
                if (it != null) {
                    Log.d("TAG", "SongList: ${it.track.id}, ${it.track.name}")
                    SpotifySongListItem(
                        album = it.track.name,
                        viewModel = viewModel,
                        trackId = true,
                        singer = it.track.artists,
                        explicit = it.track.explicit,
                        imageUrl = getImageUrl(it.track.album.images.map { url ->
                            url.url
                        }, 0)
                    )

                }
            }
        }
        item {

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
