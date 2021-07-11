package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.shaun.spotonmusic.presentation.ui.screens.NowPlaying
import com.shaun.spotonmusic.viewmodel.MusicPlayerViewModel

@ExperimentalMaterialApi
@Composable
fun PlayerBottomSheet(isPlayer: Boolean, musicPlayerViewModel: MusicPlayerViewModel) {

    Log.d("TAG", "PlayerBottomSheet: $isPlayer")

    if (isPlayer) {
        NowPlaying(musicPlayerViewModel = musicPlayerViewModel)
    } else {
        PlaybackChange(musicPlayerViewModel = musicPlayerViewModel)
    }

}