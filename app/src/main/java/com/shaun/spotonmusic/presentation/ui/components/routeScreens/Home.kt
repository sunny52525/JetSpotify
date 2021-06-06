package com.shaun.spotonmusic.presentation.ui.components.routeScreens

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
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.palette.graphics.Palette
import com.shaun.spotonmusic.network.model.RecentlyPlayed
import com.shaun.spotonmusic.presentation.ui.components.homeComponents.*
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.blue
import com.shaun.spotonmusic.utils.TypeConverters.Companion.toSuggestionModel
import com.shaun.spotonmusic.utils.getBitmapFromURL
import com.shaun.spotonmusic.utils.getGreeting
import com.shaun.spotonmusic.viewmodel.SharedViewModel
import kaaes.spotify.webapi.android.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "Home"


@Composable
fun Home(
    viewModel: SharedViewModel,
    listState: LazyListState,
    tokenExpired: () -> Unit,
    onAlbumClicked: (String) -> Unit = {},
    onPlayListClicked: (String) -> Unit = {},
    onUserPlayListClicked: (String) -> Unit = {},
    onArtistClicked: (String) -> Unit = {},

    ) {


    val headerBackgroundColor = remember {
        mutableStateOf(listOf(blue, black))
    }

    val playList: PlaylistsPager by viewModel.moodAlbum.observeAsState(PlaylistsPager())
    val party: PlaylistsPager by viewModel.partyAlbum.observeAsState(PlaylistsPager())
    val featuredPlaylists: FeaturedPlaylists by viewModel.featuredPlaylists.observeAsState(
        FeaturedPlaylists()
    )
    val favouriteArtistSongs: Pager<Album> by viewModel.favouriteArtist.observeAsState(Pager<Album>())
    val secondFavouriteArtistSongs: Pager<Album> by viewModel.secondFavouriteArtist.observeAsState(
        Pager<Album>()
    )
    val favouriteArtistImage: String by viewModel.favouriteArtistImage.observeAsState("")
    val secondFavouriteArtistImage: String by viewModel.secondFavouriteArtistImage.observeAsState("")
    val newReleases: NewReleases by viewModel.newReleases.observeAsState(NewReleases())
    val myPlayList: Pager<PlaylistSimple> by viewModel.getMyPlayList.observeAsState(Pager<PlaylistSimple>())
    val recentlyPlayed: RecentlyPlayed by viewModel.recentlyPlayed.observeAsState(initial = RecentlyPlayed())
    val favouriteArtists: Pager<Artist> by viewModel.favouriteArtists.observeAsState(initial = Pager<Artist>())
    val charts: List<Playlist> by viewModel.charts.observeAsState(initial = ArrayList())

    val firstFavouriteArtistRecommendations: Recommendations by viewModel
        .firstFavouriteArtistRecommendations.observeAsState(initial = Recommendations())
    val secondFavouriteArtistRecommendations: Recommendations by viewModel
        .secondFavouriteArtistRecommendations.observeAsState(initial = Recommendations())




    viewModel.recentlyPlayed.observeForever {
        if (it != null) {
            GlobalScope.launch {
                val myBitmap = getBitmapFromURL(it.items[0].track.album.images[0].url)
                withContext(Dispatchers.Main) {

                    if (myBitmap != null && !myBitmap.isRecycled) {
                        val palette: Palette = Palette.from(myBitmap).generate()
                        val color = palette.dominantSwatch?.rgb?.let { color ->
                            arrayListOf(color.red, color.green, color.blue)

                        }

                        val composeColor =
                            color?.get(0)?.let { it1 ->
                                Color(
                                    red = it1,
                                    green = color[1],
                                    blue = color[2]
                                )
                            }

                        if (composeColor != null) {

                            headerBackgroundColor.value = listOf(composeColor, black)
                        }
                    }
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
                    RecentHeardBlock(recentlyPlayed)
                }

            }

            item {
                SuggestionsRow(
                    data = playList.toSuggestionModel(),
                    title = "Mood",
                    onCardClicked = {
                        onPlayListClicked(it)
                    })
            }
            item {
                FavouriteArtistSongs(
                    title = "For the fans of",
                    data = favouriteArtistSongs,
                    favouriteArtistImage, albumClicked = {
                        Log.d(TAG, "Home:Lana ")
                        onAlbumClicked(it)
                    }
                )

            }

            item {
                SuggestionsRow("Your Playlists", myPlayList.toSuggestionModel(), onCardClicked = {
                    onUserPlayListClicked(it)
                })
            }
            item {
                SuggestionsRow(data = party.toSuggestionModel(), title = "Party", onCardClicked = {
                    onPlayListClicked(it)
                })
            }

            item {
                SuggestionsRow(
                    title = "Recently Played",
                    data = recentlyPlayed.toSuggestionModel(),
                    onCardClicked = {
                        onAlbumClicked(it)
                    })
            }


            item {
                RecommendationsRow(
                    title = "More Like",
                    recommendations = firstFavouriteArtistRecommendations,
                    image = favouriteArtistImage,
                    artistName = favouriteArtists,
                    index = 0
                )
            }

            item {

                SuggestionsRow(
                    data = featuredPlaylists.toSuggestionModel(),
                    title = "Featured Playlists",
                    onCardClicked = {
                        onPlayListClicked(it)
                    }
                )
            }
            item {
                SuggestionsRow(
                    title = "Charts",
                    data = charts.toSuggestionModel(),
                    size = 150,
                    onCardClicked = {
                        onPlayListClicked(it)
                    })
            }


            item {
                RecommendationsRow(
                    title = "More Like",
                    recommendations = secondFavouriteArtistRecommendations,
                    image = secondFavouriteArtistImage,
                    artistName = favouriteArtists,
                    index = 1
                )
            }


            item {
                FavouriteArtistSongs(
                    title = "For the fans of",
                    data = secondFavouriteArtistSongs,
                    secondFavouriteArtistImage,
                    albumClicked = {
                        onAlbumClicked(it)
                    }
                )
            }

            item {
                SuggestionsRow(
                    title = "Your Favourites",
                    data = favouriteArtists.toSuggestionModel(),
                    onCardClicked = {
                        onArtistClicked(it)
                    })
            }


            item {
                SuggestionsRow(
                    data = newReleases.toSuggestionModel(),
                    title = "New Releases",
                    onCardClicked = {
                        onAlbumClicked(it)
                    })
            }



            item {
                Spacer(modifier = Modifier.height(50.dp))
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



