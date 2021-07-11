package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.viewmodel.MusicPlayerViewModel

@ExperimentalMaterialApi
@Composable
fun PlaybackChange(musicPlayerViewModel: MusicPlayerViewModel) {

    val devices by musicPlayerViewModel.devices.observeAsState()

    val volume by musicPlayerViewModel.volume.observeAsState()
    Column(
        Modifier
            .fillMaxSize()
            .background(spotifyDarkBlack)
            .padding(top = 50.dp)
    ) {

        TopClose {
            musicPlayerViewModel.isCollapsed.postValue(true)
        }
        devices?.devices?.filter { device ->
            device.is_active
        }?.let {
            if (it.isNotEmpty()) {
                it[0].let { device ->

                    CurrentDevice(deviceName = device.name, isPhone = device.type == "Smartphone")
                }
            }
        }

        Log.d("TAG", "PlaybackChange: ")

        Text(
            text = "Select a device",
            color = Color.White,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 8.dp),
            fontSize = 15.sp
        )


        devices?.devices?.forEach {
            val isPhone = it.type == "Smartphone"
            if (!it.is_active)
                AvailableDevice(isPhone = isPhone, deviceName = it.name) {
                    musicPlayerViewModel.changePlayer(it.id)
                    musicPlayerViewModel.isCollapsed.postValue(true)
                }
        }





        volume?.let {


            Row(
                Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
            ) {

                Slider(
                    value = it, onValueChange = {
                        musicPlayerViewModel.spotifyRemote.value?.connectApi?.connectSetVolume(it)
                    }, modifier = Modifier
                        .align(Alignment.Bottom),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        inactiveTrackColor = Color.Gray

                    )
                )
            }


        }
    }
}