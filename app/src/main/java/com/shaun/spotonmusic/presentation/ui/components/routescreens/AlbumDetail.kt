package com.shaun.spotonmusic.presentation.ui.components.routescreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.AlbumSongList
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.TopBar
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.shaun.spotonmusic.utils.PaletteExtractor
import com.shaun.spotonmusic.viewmodel.AlbumDetailViewModel
import kaaes.spotify.webapi.android.models.Album


private const val TAG = "ALbumDETAIL"

@Composable
fun AlbumDetail(id: String?, viewModel: AlbumDetailViewModel) {


    val liked by viewModel.liked.observeAsState(initial = false)


    var isLoaded by remember {

        mutableStateOf(false)
    }
    Log.d(TAG, "AlbumDetail: $id")


    val currentAlbum: Album? by viewModel.album.observeAsState()

    viewModel.album.observeForever {
        it?.let {
            isLoaded = true
        }

    }

    val colors = remember {
        mutableStateOf(arrayListOf(black, spotifyDarkBlack))
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

    Log.d(TAG, "AlbumDetail: ${currentAlbum?.name}")

    if (isLoaded) {
        Column() {
            TopBar(isAlbum = true, title = currentAlbum?.name, onBackPressed = {

            }, onMoreOptionClicked = {

            }, onLikeButtonClicked = {

            }, backgroundColor = spotifyGray,
                liked = liked ?: false
            )

            AlbumSongList(currentAlbum, colors.value)
        }
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .background(spotifyDarkBlack)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}