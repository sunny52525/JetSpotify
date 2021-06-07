package com.shaun.spotonmusic.presentation.ui.components.routescreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.AnimatedToolBar
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.BoxTopSection
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.SongList
import com.shaun.spotonmusic.presentation.ui.components.albumcomponents.TopSectionOverlay
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.green
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.utils.PaletteExtractor
import com.shaun.spotonmusic.viewmodel.MusicDetail
import kaaes.spotify.webapi.android.models.Playlist


private const val TAG: String = "PlayListDetail"

@ExperimentalFoundationApi
@Composable
fun PlaylistDetail(viewModel: MusicDetail, id: String?) {

//    val view = viewModel(MusicDetail::class.java)

    val currentAlbum: Playlist?
            by viewModel.playList.observeAsState(initial = Playlist())


    val colors = remember {
        mutableStateOf(arrayListOf<Color>(black, spotifyDarkBlack, black))
    }

    val paletteExtractor = PaletteExtractor()

    currentAlbum?.images?.let {
        val shade = paletteExtractor.getColorFromSwatch(it[0].url)
        shade.observeForever { shadeColor ->
            shadeColor?.let { col ->
                colors.value = arrayListOf(col, black)
            }

        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {


        val scrollState = rememberLazyListState()
        currentAlbum?.images?.get(0)?.url?.let {
            BoxTopSection(
                album = currentAlbum?.name ?: "", listState = scrollState,
                surfaceGradient = colors.value,
                imageUrl = it

            )
        }
        TopSectionOverlay(scrollState = scrollState)
        SongList(
            scrollState = scrollState, surfaceGradient = colors.value,

            currentAlbum?.tracks,

            )
        AnimatedToolBar(
            album = "Help",
            scrollState = scrollState,
            surfaceGradient = arrayListOf(black, green)
        )
    }
}