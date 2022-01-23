package com.shaun.spotonmusic.presentation.ui.screens

import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.presentation.ui.Heading
import com.shaun.spotonmusic.presentation.ui.components.home.SuggestionsRow
import com.shaun.spotonmusic.presentation.ui.components.playlist.SpotifySongListItem
import com.shaun.spotonmusic.presentation.ui.components.search.MainSearchBar
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.utils.getArtistName
import com.shaun.spotonmusic.utils.getImageUrl
import com.shaun.spotonmusic.utils.toListString
import com.shaun.spotonmusic.viewmodel.SearchViewModel
import kaaes.spotify.webapi.android.models.AlbumSimple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    searchScreenMainViewModel: SearchViewModel,
    scope: CoroutineScope,
    onPlaylistClicked: (String) -> Unit,
    onArtistClicked: (String) -> Unit,
    onAlbumClicked: (String) -> Unit,
    onTrackClicked: (String) -> Unit
) {

    val isVisible = produceState(initialValue = false)
    {
        delay(100)
        value = true

    }



    val searchQuery = remember {
        mutableStateOf("")
    }

    val topArtist = searchScreenMainViewModel.artists.observeAsState()
    val playlists = searchScreenMainViewModel.playlists.observeAsState()
    val tracks = searchScreenMainViewModel.tracks.observeAsState()
    val albums = searchScreenMainViewModel.albums.observeAsState()





    Column(
        Modifier
            .padding(top = 50.dp)
            .background(spotifyDarkBlack)
            .fillMaxSize()

    ) {

        AnimatedVisibility(
            visible = isVisible.value,
            enter = slideInVertically(initialOffsetY = {
                100
            }),
        ) {
            MainSearchBar(query = searchQuery.value ) {
                if (searchQuery.value == it)
                    return@MainSearchBar

                searchQuery.value = it

                scope.launch {

                    delay(100)
                    if (it != searchQuery.value) {
                        return@launch
                    }
                    searchScreenMainViewModel.searchQuery(searchQuery.value)
                }
            }

        }

        LazyColumn {

            item {
                Spacer(modifier = Modifier.height(10.dp))
                topArtist.value?.get(0)?.let {
                    SpotifySongListItem(
                        album = it.title,
                        shape = CircleShape,
                        imageUrl = getImageUrl(it.imageUrls, 0),
                        singer = "Artist",
                        explicit = false,
                        paddingStart = 20
                    ) {
                        onArtistClicked(it.id)
                    }
                }
            }


            if (tracks.value?.tracks?.items?.size?.compareTo(0) == 1)
                item {
                    Heading(title = "Tracks")
                }
            tracks.value?.tracks?.items?.let {
                items(it.subList(0, 5)) { track ->
                    SpotifySongListItem(
                        album = track.name, singer = getArtistName(track.artists),
                        imageUrl = getImageUrl(track.album.images.toListString(), 0),
                        explicit = track.explicit,
                        paddingStart = 20
                    ) {
                        onTrackClicked(track.uri)
                    }
                }
            }

            topArtist.value?.let {
                item {
                    SuggestionsRow(title = "Artists", data = it) {
                        onArtistClicked(it)
                    }
                }

            }

            if (albums.value?.albums?.items?.size?.compareTo(0) == 1)
                item {
                    Heading(title = "Albums")
                }

            albums.value?.albums?.items?.subList(0, 6)?.let {
                items(it) { album: AlbumSimple? ->
                    album?.let {
                        SpotifySongListItem(
                            album = album.name,
                            imageUrl = getImageUrl(album.images.toListString(), 0),
                            singer = album.album_type, explicit = false,
                            paddingStart = 20
                        ) {
                            onAlbumClicked(it.id)
                        }

                    }
                }
            }


            playlists.value?.let {
                item {
                    SuggestionsRow(title = "Playlists", data = it) { id ->
                        onPlaylistClicked(id)
                    }
                }
            }


        }

    }
}