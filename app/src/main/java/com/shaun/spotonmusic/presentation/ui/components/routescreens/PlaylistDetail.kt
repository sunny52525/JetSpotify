package com.shaun.spotonmusic.presentation.ui.components.routescreens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.AnimatedToolBar
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.BoxTopSection
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.SongList
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.TopSectionOverlay
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.utils.PaletteExtractor
import com.shaun.spotonmusic.viewmodel.MusicDetail
import kaaes.spotify.webapi.android.models.Playlist
import kaaes.spotify.webapi.android.models.UserPrivate


private const val TAG: String = "PlayListDetail"

@ExperimentalFoundationApi
@Composable
fun PlaylistDetail(id: String?, myDetails: UserPrivate?) {

    val viewModel = hiltViewModel<MusicDetail>()
    var follow by remember {
        mutableStateOf(false)
    }




    viewModel.followsPlaylist(id.toString(), myDetails?.id.toString()).observeForever {
        if (it != null)
            follow = it
    }

    Log.d(TAG, "PlaylistDetail: $id")
    id?.let { viewModel.newPlaylist(it) }


    val currentAlbum
            : Playlist? by viewModel.playList.observeAsState(initial = Playlist())


    val colors = remember {
        mutableStateOf(arrayListOf<Color>(black, spotifyDarkBlack, black))
    }

    val paletteExtractor = PaletteExtractor()

    currentAlbum?.images?.let {
        val shade = paletteExtractor.getColorFromSwatch(it[0].url)
        shade.observeForever { shadeColor ->
            shadeColor?.let { col ->
                colors.value = arrayListOf(col, spotifyDarkBlack)
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(spotifyDarkBlack)


    ) {


        val scrollState = rememberLazyListState()
        currentAlbum?.images?.get(0)?.url?.let {
            BoxTopSection(
                album = currentAlbum?.name ?: "",
                listState = scrollState,
                surfaceGradient = colors.value,
                imageUrl = it,
                currentAlbum = currentAlbum ?: Playlist(),
                isFollowing = follow,

                )
        }
        TopSectionOverlay(scrollState = scrollState)
        SongList(
            scrollState = scrollState,

            tracks = currentAlbum?.tracks,
            onFollowClicked = {
                viewModel.follows.postValue(!follow)

                if (!follow)
                    id?.let { viewModel.followAPlaylist(it) }
                else
                    id?.let { viewModel.unFollowPlaylist(it) }
            },
            viewModel = viewModel,
        )
        AnimatedToolBar(
            album = currentAlbum?.name ?: "",
            scrollState = scrollState
        )
    }
}