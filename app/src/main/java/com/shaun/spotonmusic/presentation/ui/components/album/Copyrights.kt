package com.shaun.spotonmusic.presentation.ui.components.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack

@Composable

fun CopyWrites(title: String) {

    Row(
        Modifier
            .fillMaxWidth()
            .background(spotifyDarkBlack)
    ) {
        Text(text = "©$title", color = Color.White, modifier = Modifier.padding(10.dp))
    }

}