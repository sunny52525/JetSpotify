package com.shaun.spotonmusic.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.presentation.ui.components.nowplaying.*
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.utils.PaletteExtractor
import com.shaun.spotonmusic.viewmodel.MusicPlayerViewModel

@ExperimentalMaterialApi
@Composable
fun NowPlaying(musicPlayerViewModel: MusicPlayerViewModel) {

    val scrollState = rememberScrollState()

    val context = LocalContext.current

    val albumArtLink by musicPlayerViewModel.imageUrl.observeAsState()
    val songName: String? by musicPlayerViewModel.trackName.observeAsState(initial = "")
    val singerName: String? by musicPlayerViewModel.singerName.observeAsState(initial = "")
    val seekPosition: Float by musicPlayerViewModel.seekState.observeAsState(initial = 0.0f)
    val colors = remember {
        mutableStateOf(arrayListOf<Color>(black, spotifyDarkBlack, black))
    }

    val isPaused: Boolean by musicPlayerViewModel.isPlaying.observeAsState(initial = false)

    val repeatMode by musicPlayerViewModel.repeatMode.observeAsState()


    val paletteExtractor = PaletteExtractor()
    albumArtLink?.let {
        val shade = paletteExtractor.getColorFromSwatch(it)
        shade.observeForever { shadeColor ->
            shadeColor?.let { col ->
                colors.value = arrayListOf(col, spotifyDarkBlack)
            }

        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .scrollable(scrollState, Orientation.Vertical)
            .padding(top = 20.dp)
            .background(
                Brush.linearGradient(colors = colors.value)
            )
            .pointerInput(Unit) {

                detectVerticalDragGestures { change, dragAmount ->
                    if (change.position.y - change.previousPosition.y > 20f) {
                        musicPlayerViewModel.isCollapsed.postValue(true)
                    }
                }
            }
    ) {

        Top {
            musicPlayerViewModel.isCollapsed.postValue(true)
        }
        AlbumArt(albumArtLink)
        CurrentSong(
            songName = songName,
            singerName = singerName
        )
        SeekBar(seekPosition)
        Controller(
            repeatMode,
            isPaused = isPaused,
            onLiked = {
                musicPlayerViewModel.spotifyRemote.value?.playerApi?.toggleRepeat()

            },
            onNext = {
                musicPlayerViewModel.spotifyRemote.value?.playerApi?.skipNext()
            },
            onPrevious = {
                musicPlayerViewModel.spotifyRemote.value?.playerApi?.skipPrevious()

            }) {

            if (!isPaused) {
                Log.d("TAG", "NowPlaying: Resuming")
                musicPlayerViewModel.spotifyRemote.value?.playerApi?.resume()
            } else {

                Log.d("TAG", "NowPlaying: Pausing")
                musicPlayerViewModel.spotifyRemote.value?.playerApi?.pause()
            }
        }
    }


}