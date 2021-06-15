package com.shaun.spotonmusic.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.shaun.spotonmusic.presentation.ui.components.album.TopBar
import com.shaun.spotonmusic.presentation.ui.components.library.LibraryGrid
import com.shaun.spotonmusic.presentation.ui.components.playlistgrid.TopSpace
import com.shaun.spotonmusic.ui.theme.gridColors
import kotlin.math.min

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun PlaylistGridScreen(
    title: String = "Chill",
    color: Int = 0,
    id: String? = "chill"
) {


    val scrollState = rememberLazyListState()
    val dynamicAlpha =
        if (scrollState.firstVisibleItemIndex < 1) {
            ((min(scrollState.firstVisibleItemScrollOffset, 400) * 0.2) / 100).toFloat()
        } else {
            0.99f
        }
    Column(Modifier.fillMaxSize()) {

        TopBar(
            showHeart = false,
            showThreeDots = true,
            title = title,
            onBackPressed = {

            },
            backgroundColor = gridColors.get(index = color),
            paddingStart = 8,
            alpha = dynamicAlpha
        )

        LazyColumn(state = scrollState) {

            item {
                TopSpace(color = gridColors.get(index = color))
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
            repeat(10) {
                item {
                    LibraryGrid(
                        title = Pair("Lana", "Del"),
                        type = Pair("Lana", "del"),
                        owner = Pair("Me", "Me"),
                        imageUrl = Pair(
                            "https://i.scdn.co/image/17a4cbd857bb8d8d7e9cfc14897b7799e7858465",
                            "https://i.scdn.co/image/17a4cbd857bb8d8d7e9cfc14897b7799e7858465"
                        ),
                        onFirstClick = { /*TODO*/ },
                        onSecondClick = {},
                        textAlign = TextAlign.Center
                    )

                }
            }
        }

    }

}