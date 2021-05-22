package com.shaun.spotonmusic.presentation.ui.components.routeScreens

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
import com.shaun.spotonmusic.getBitmapFromURL
import com.shaun.spotonmusic.getGreeting
import com.shaun.spotonmusic.model.RecentlyPlayed
import com.shaun.spotonmusic.presentation.ui.components.*
import com.shaun.spotonmusic.toPlayListPager
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.blue
import com.shaun.spotonmusic.viewmodel.HomeScreenViewModel
import kaaes.spotify.webapi.android.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "Home"


@Composable
fun Home(
    viewModel: HomeScreenViewModel,
    listState: LazyListState
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
    val charts: List<Playlist?> by viewModel.charts.observeAsState(initial = ArrayList())

    val firstFavouriteArtistRecommendations: Recommendations by viewModel
        .firstFavouriteArtistRecommendations.observeAsState(initial = Recommendations())
    val secondFavouriteArtistRecommendations: Recommendations by viewModel
        .firstFavouriteArtistRecommendations.observeAsState(initial = Recommendations())




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
                SuggestionsRow(playlistsPager = playList, title = "Mood",)
            }
            item {
                FavouriteArtistSongs(
                    title = "For the fans of",
                    data = secondFavouriteArtistSongs,
                    secondFavouriteArtistImage
                )
            }

            item {
                PlaylistRow("Your Playlists", myPlayList)
            }
            item {
                SuggestionsRow(playlistsPager = party, title = "Party" )
            }

            item {
                RecentlyPlayedRow(title = "Recently Played", recentlyPlayed = recentlyPlayed)
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
                    playlistsPager = featuredPlaylists.toPlayListPager(),
                    title = "Featured Playlists")
            }
            item {
                ChartsRow(title = "Charts", playlist = charts)
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
                    data = favouriteArtistSongs,
                    favouriteArtistImage
                )

            }

            item {
                ArtistRow(title = "Your Favourites", artistsPager = favouriteArtists)
            }


            item {
                NewReleasesRow(newReleases = newReleases, title = "New Releases")
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



