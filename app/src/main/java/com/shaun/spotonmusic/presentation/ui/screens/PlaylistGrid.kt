package com.shaun.spotonmusic.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.shaun.spotonmusic.presentation.ui.components.album.TopBar
import com.shaun.spotonmusic.presentation.ui.components.playlistgrid.TopSpace

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun PlaylistGridScreen(
    title: String = "Chill",
    color: Color = Color.Cyan,
    id: String? = "chill"
) {
    Column(Modifier.fillMaxSize()) {

        TopBar(
            showHeart = false,
            showThreeDots = false,
            title = title,
            onBackPressed = {

            },
            backgroundColor = color,
            paddingStart = 0
        )

        LazyColumn {

            item {
                TopSpace()
            }
            item {
                Text(
                    text = "Popular Playlists",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }

}