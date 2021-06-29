package com.shaun.spotonmusic.presentation.ui.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.shaun.spotonmusic.presentation.ui.components.nowplaying.*

@ExperimentalMaterialApi
@Preview(showBackground = true, device = Devices.PIXEL_2_XL)
@Composable
fun NowPlaying() {

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .scrollable(scrollState, Orientation.Vertical)
    ) {

        Top()
        AlbumArt()
        CurrentSong()
        SeekBar()
        Controller()
    }


}