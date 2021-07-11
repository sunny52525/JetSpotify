package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SeekBar(seekPosition: Float,
            onValueChanged:()->Unit,
            onSeek:(Float)->Unit,
) {


    Slider(
        value = seekPosition, onValueChange = onSeek, colors = SliderDefaults.colors(
            activeTrackColor = Color.White,
            thumbColor = Color.White,
            inactiveTrackColor = Color.Gray
        ),
        modifier = Modifier.height(5.dp).padding(horizontal = 20.dp).requiredSizeIn(minHeight = 10.dp),onValueChangeFinished = onValueChanged
    )
}