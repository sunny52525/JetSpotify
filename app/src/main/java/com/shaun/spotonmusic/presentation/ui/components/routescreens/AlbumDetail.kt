package com.shaun.spotonmusic.presentation.ui.components.routescreens

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.shaun.spotonmusic.viewmodel.AlbumDetailViewModel
import kaaes.spotify.webapi.android.models.Album


private const val TAG = "ALbumDETAIL"

@Composable
fun AlbumDetail(id: String?, viewModel: AlbumDetailViewModel) {


    var liked by remember {
        mutableStateOf(false)
    }


    Log.d(TAG, "AlbumDetail: $id")


    val currentAlbum: Album? by viewModel.album.observeAsState()

    Log.d(TAG, "AlbumDetail: ${currentAlbum?.name}")
}