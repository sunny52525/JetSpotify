package com.shaun.spotonmusic.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.presentation.ui.components.album.TopBar
import com.shaun.spotonmusic.presentation.ui.components.library.LibraryGrid
import com.shaun.spotonmusic.presentation.ui.components.playlistgrid.TopSpace
import com.shaun.spotonmusic.ui.theme.gridColors
import com.shaun.spotonmusic.utils.getImageUrl
import com.shaun.spotonmusic.viewmodel.PlaylistGridViewModel
import kaaes.spotify.webapi.android.models.PlaylistsPager
import kotlin.math.min

@ExperimentalFoundationApi
@Composable
fun PlaylistGridScreen(
    color: Int = 0,
    id: String? = "chill",
    viewModel: PlaylistGridViewModel,
    onPlaylistClicked: (String) -> Unit
) {


    val playlist: PlaylistsPager? by viewModel.playlistList.observeAsState(initial = PlaylistsPager())

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
            title = id?.toUpperCase(LocaleList.current),
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

            playlist?.playlists?.items?.let { items ->
                for (i in 0 until items.size - 1 step 2) {
                    with(items) {
                        get(i)?.let {
                            item {
                                LibraryGrid(
                                    title = Pair(get(i).name, get(i + 1).name),
                                    type = Pair(get(i).type, get(i + 1).type),
                                    owner = Pair(
                                        get(i).owner.display_name,
                                        get(i + 1).owner.display_name
                                    ),
                                    imageUrl = Pair(
                                        getImageUrl(get(i).images.map { it.url }, 2),
                                        getImageUrl(get(i + 1).images.map { it.url }, 2)
                                    ),
                                    onFirstClick = {
                                        onPlaylistClicked(get(i).id)
                                    },
                                    onSecondClick = {
                                        onPlaylistClicked(get(i + 1).id)
                                    },
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }


            }

        }

    }

}