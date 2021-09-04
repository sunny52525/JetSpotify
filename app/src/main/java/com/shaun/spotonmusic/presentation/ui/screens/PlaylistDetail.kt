package com.shaun.spotonmusic.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.shaun.spotonmusic.presentation.ui.components.Progress
import com.shaun.spotonmusic.presentation.ui.components.playlist.AnimatedToolBar
import com.shaun.spotonmusic.presentation.ui.components.playlist.BoxTopSection
import com.shaun.spotonmusic.presentation.ui.components.playlist.SongList
import com.shaun.spotonmusic.presentation.ui.components.playlist.TopSectionOverlay
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.utils.PaletteExtractor
import com.shaun.spotonmusic.viewmodel.PlaylistDetailViewModel
import kaaes.spotify.webapi.android.models.Playlist


private const val TAG: String = "PlayListDetail"

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun PlaylistDetail(
    id: String?,
    updatePlaylist: () -> Unit,
    viewModel: PlaylistDetailViewModel,
    onShufflePlayListClicked: (String?) -> Unit,
    onSongClicked: (String) -> Unit
) {

    val follow by viewModel.follows.observeAsState(false)

    var isLoaded by remember {
        mutableStateOf(false)
    }

    Log.d(TAG, "PlaylistDetail: $id")

    val currentPlaylist
            : Playlist? by viewModel.playList.observeAsState(initial = Playlist())

    val likedSongs by viewModel.likedSongs.observeAsState()

    viewModel.playList.observeForever {
        if (it.images != null)
            isLoaded = true
        Log.d(TAG, "PlaylistDetail: ${it.images}")
    }

    val colors = remember {
        mutableStateOf(arrayListOf<Color>(black, spotifyDarkBlack, black))
    }

    val paletteExtractor = PaletteExtractor()

    currentPlaylist?.images?.let {
        val shade = paletteExtractor.getColorFromSwatch(it[0].url)
        shade.observeForever { shadeColor ->
            shadeColor?.let { col ->
                colors.value = arrayListOf(col, spotifyDarkBlack)
            }

        }
    }


    if (isLoaded) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(spotifyDarkBlack)


        ) {


            val scrollState = rememberLazyListState()
            currentPlaylist?.images?.get(0)?.url?.let {
                BoxTopSection(
                    album = currentPlaylist?.name ?: "",
                    listState = scrollState,
                    surfaceGradient = colors.value,
                    imageUrl = it,
                    currentAlbum = currentPlaylist ?: Playlist(),
                    isFollowing = follow,

                    )
            }

            TopSectionOverlay(scrollState = scrollState)
            SongList(
                scrollState = scrollState,

                tracks = currentPlaylist?.tracks,
                onFollowClicked = {
                    viewModel.follows.value = !follow

                    if (follow)
                        id?.let {
                            viewModel.followAPlaylist(it, onFollowed = {

                                updatePlaylist()
                            })
                        }
                    else
                        id?.let {
                            viewModel.unFollowPlaylist(it, onUnFollowed = {

                                updatePlaylist()
                            })
                        }


                },
                shuffleClicked = {
                    onShufflePlayListClicked(currentPlaylist?.uri)
                },
                onSongClicked = {
                    onSongClicked(it)
                },
                likedSongs=likedSongs
            )
            AnimatedToolBar(
                album = currentPlaylist?.name ?: "",
                scrollState = scrollState
            )
        }
    } else {
        Progress()
    }
}