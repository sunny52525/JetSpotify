package com.shaun.spotonmusic.presentation.ui.components.routeScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shaun.spotonmusic.presentation.ui.components.albumComponents.AnimatedToolBar
import com.shaun.spotonmusic.presentation.ui.components.albumComponents.BoxTopSection
import com.shaun.spotonmusic.presentation.ui.components.albumComponents.SongList
import com.shaun.spotonmusic.presentation.ui.components.albumComponents.TopSectionOverlay
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.blue
import com.shaun.spotonmusic.ui.theme.green
import com.shaun.spotonmusic.viewmodel.AlbumDetailViewModel
import kaaes.spotify.webapi.android.models.Album

@ExperimentalFoundationApi
@Preview
@Composable
fun AlbumDetail(id: String? = "Test") {

    val viewModel: AlbumDetailViewModel = viewModel()

    val currentAlbum: Album by viewModel.getAlbum(id.toString()).observeAsState(initial = Album())

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {


        val scrollState = rememberLazyListState()
        BoxTopSection(album = "Help", listState = scrollState)
        TopSectionOverlay(scrollState = scrollState)
        SongList(scrollState = scrollState, surfaceGradient = arrayListOf(black, blue, green),

            currentAlbum.tracks
            )
        AnimatedToolBar(
            album = "Help",
            scrollState = scrollState,
            surfaceGradient = arrayListOf(black, green)
        )
    }
}