package com.shaun.spotonmusic.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.presentation.ui.components.home.*
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.blue
import com.shaun.spotonmusic.utils.PaletteExtractor
import com.shaun.spotonmusic.utils.getGreeting
import com.shaun.spotonmusic.viewmodel.SharedViewModel
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.Pager

private const val TAG = "Home"


@Composable
fun Home(
    viewModel: SharedViewModel,
    listState: LazyListState,
    tokenExpired: () -> Unit,
    onAlbumClicked: (String) -> Unit = {},
    onPlayListClicked: (String) -> Unit = {},
    onArtistClicked: (String) -> Unit = {},

    ) {


    val headerBackgroundColor = remember {
        mutableStateOf(listOf(blue, black))
    }

    val playList: List<SpotOnMusicModel> by viewModel.moodAlbum.observeAsState(listOf())
    val party: List<SpotOnMusicModel> by viewModel.partyAlbum.observeAsState(listOf())
    val featuredPlaylists: List<SpotOnMusicModel> by viewModel.featuredPlaylists.observeAsState(
        listOf()
    )
    val favouriteArtistSongs: Pager<Album> by viewModel.favouriteArtist.observeAsState(Pager<Album>())
    val secondFavouriteArtistSongs: Pager<Album> by viewModel.secondFavouriteArtist.observeAsState(
        Pager<Album>()
    )
    val favouriteArtistImage: String by viewModel.favouriteArtistImage.observeAsState("")
    val secondFavouriteArtistImage: String by viewModel.secondFavouriteArtistImage.observeAsState("")
    val newReleases: List<SpotOnMusicModel> by viewModel.newReleases.observeAsState(listOf())
    val myPlayList: List<SpotOnMusicModel> by viewModel.getMyPlayList.observeAsState(listOf())
    val recentlyPlayed: List<SpotOnMusicModel> by viewModel.recentlyPlayed.observeAsState(initial = listOf())
    val favouriteArtists: List<SpotOnMusicModel> by viewModel.favouriteArtists.observeAsState(
        initial = listOf()
    )
    val charts: List<SpotOnMusicModel> by viewModel.charts.observeAsState(initial = ArrayList())

    val firstFavouriteArtistRecommendations: List<SpotOnMusicModel> by viewModel
        .firstFavouriteArtistRecommendations.observeAsState(initial = listOf())
    val secondFavouriteArtistRecommendations: List<SpotOnMusicModel> by viewModel
        .secondFavouriteArtistRecommendations.observeAsState(initial = listOf())


    val paletteExtractor = PaletteExtractor()

    viewModel.recentlyPlayed.observeForever {
        if (it != null) {
            val shade = paletteExtractor.getColorFromSwatch(it[0].imageUrls[0])
            shade.observeForever { shadeColor ->
                shadeColor?.let { col ->
                    headerBackgroundColor.value = listOf(
                        col,
                        black
                    )
                }
            }

        }
    }




    viewModel.tokenExpired.observeForever {
        if (it == true) {
            viewModel.tokenExpired.value = false
            tokenExpired()
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)

    ) {

        val brush = Brush.linearGradient(
            colors = headerBackgroundColor.value,
            start = Offset(0f, 0f), end = Offset(50f, 250f)
        )

        LazyColumn(
            Modifier
                .background(black),
            state = listState
        ) {
            item {
                Column(modifier = Modifier.background(brush = brush)) {
                    Spacer(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()
                    )
                    Greeting(onClick = {

                    })
                    RecentHeardBlock(recentlyPlayed, onCardClicked = {
                        onAlbumClicked(it)
                    })
                }

            }

            item {
                SuggestionsRow(
                    data = playList,
                    title = "Mood",
                    onCardClicked = {
                        onPlayListClicked(it)
                    }
                )
            }
            item {
                FavouriteArtistSongs(
                    title = "For the fans of",
                    data = favouriteArtistSongs,
                    favouriteArtistImage
                ) {
                    Log.d(TAG, "Home:Lana ")
                    onAlbumClicked(it)
                }

            }

            item {
                SuggestionsRow("Your Playlists", myPlayList) {
                    onPlayListClicked(it)
                }
            }
            item {
                SuggestionsRow(data = party, title = "Party") {

                    onPlayListClicked(it)
                }
            }

            item {
                SuggestionsRow(
                    title = "Recently Played",
                    data = recentlyPlayed
                ) {
                    onAlbumClicked(it)
                }
            }


            item {
                RecommendationsRow(
                    title = "More Like",
                    recommendations = firstFavouriteArtistRecommendations,
                    image = favouriteArtistImage,
                    artistName = favouriteArtists,
                    index = 0,
                    onCardClicked = {
                        onAlbumClicked(it)
                    }
                )
            }

            item {

                SuggestionsRow(
                    data = featuredPlaylists,
                    title = "Featured Playlists"
                ) {
                    onPlayListClicked(it)
                }
            }
            item {
                SuggestionsRow(
                    title = "Charts",
                    data = charts,
                    size = 150
                ) {
                    onPlayListClicked(it)
                }
            }


            item {
                RecommendationsRow(
                    title = "More Like",
                    recommendations = secondFavouriteArtistRecommendations,
                    image = secondFavouriteArtistImage,
                    artistName = favouriteArtists,
                    index = 1,
                    onCardClicked = {
                        onAlbumClicked(it)
                    }
                )
            }


            item {
                FavouriteArtistSongs(
                    title = "For the fans of",
                    data = secondFavouriteArtistSongs,
                    secondFavouriteArtistImage
                ) {
                    onAlbumClicked(it)
                }
            }

            item {
                SuggestionsRow(
                    title = "Your Favourites",
                    data = favouriteArtists,
                    cornerRadius = 50
                ) {
                    onArtistClicked(it)
                }
            }


            item {
                SuggestionsRow(
                    data = newReleases,
                    title = "New Releases"
                ) {
                    onAlbumClicked(it)
                }
            }


        }

    }

}

@Composable
fun Greeting(onClick: (String) -> Unit) {

    GreetingCard(title = getGreeting(), onSettingClicked = {

        onClick("setting")

    }, onHistoryClicked = {

        onClick("history")

    })
}



