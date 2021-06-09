package com.shaun.spotonmusic.presentation.ui.components.routescreens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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


    Log.d(TAG, "AlbumDetail: $id")


    val currentAlbum: Album? by viewModel.album.observeAsState()

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

    Column() {
        TopBar(isAlbum = true, title = currentAlbum?.name, onBackPressed = {

        }, onMoreOptionClicked = {

        }, onLikeButtonClicked = {

        }, backgroundColor = spotifyGray,
            liked = liked ?: false
        )

        AlbumSongList(currentAlbum, colors.value)
    }
}