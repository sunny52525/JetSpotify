package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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


    }
}