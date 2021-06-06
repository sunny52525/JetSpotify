package com.shaun.spotonmusic.presentation.ui.components.routeScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.shaun.spotonmusic.presentation.ui.components.albumComponents.AnimatedToolBar
import com.shaun.spotonmusic.presentation.ui.components.albumComponents.BoxTopSection
import com.shaun.spotonmusic.presentation.ui.components.albumComponents.SongList
import com.shaun.spotonmusic.presentation.ui.components.albumComponents.TopSectionOverlay
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.blue
import com.shaun.spotonmusic.ui.theme.green
import com.shaun.spotonmusic.viewmodel.MusicDetail
import kaaes.spotify.webapi.android.models.Playlist

@ExperimentalFoundationApi

@Composable

fun PlaylistDetail(id: String? = "Test", musicDetail: MusicDetail) {

    val currentAlbum: Playlist
            by musicDetail.getPlaylist(id.toString()).observeAsState(initial = Playlist())

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {


        val scrollState = rememberLazyListState()
        BoxTopSection(album = "Help", listState = scrollState)
        TopSectionOverlay(scrollState = scrollState)
        SongList(
            scrollState = scrollState, surfaceGradient = arrayListOf(black, blue, green),

            currentAlbum.tracks
        )
        AnimatedToolBar(
            album = "Help",
            scrollState = scrollState,
            surfaceGradient = arrayListOf(black, green)
        )
    }
}