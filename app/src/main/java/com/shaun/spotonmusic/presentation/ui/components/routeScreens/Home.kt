package com.shaun.spotonmusic.presentation.ui.components.routeScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.getGreeting
import com.shaun.spotonmusic.model.RecentlyPlayed
import com.shaun.spotonmusic.presentation.ui.activity.HomeActivity
import com.shaun.spotonmusic.presentation.ui.components.*
import com.shaun.spotonmusic.toPlayListPager
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.headerBackgroundColor
import com.shaun.spotonmusic.viewmodel.HomeScreenViewModel
import kaaes.spotify.webapi.android.models.*

private const val TAG = "Home"

@Composable
fun Home(
    viewModel: HomeScreenViewModel, context: HomeActivity
) {


    viewModel.tokenExpired.observeForever {

        Log.d(TAG, "Home: $it")
        if (it == true) {
            viewModel.tokenExpired.postValue(false)
            context.spotifyAuthClient.refreshAccessToken()
            viewModel.getAccessToken()
            context.recreate()

        }
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





    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)

    ) {
        val brush = Brush.linearGradient(
            colors = headerBackgroundColor,
            start = Offset(0f, 0f), end = Offset(50f, 250f)
        )

        LazyColumn(
            Modifier
                .background(black)
        ) {
            item {
                Column(modifier = Modifier.background(brush = brush)) {
                    Spacer(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                    )
                    Greeting(onClick = { it ->
                        viewModel.getAlbum(it)
                        Log.d("TAG", "Home: ${viewModel.albums.value?.name}")
                    })
                    RecentHeardBlock()
                }


            }





            item {

                SuggestionsRow(playlistsPager = playList, title = "Mood")
            }
            item {
                FavouriteArtistSongs(
                    title = "For the fans of",
                    data = secondFavouriteArtistSongs,
                    secondFavouriteArtistImage
                )
            }
            item {
                PlaylistRow(playlistsPager = myPlayList, title = "Your Playlists")
            }
            item {

                SuggestionsRow(playlistsPager = party, title = "Party")
            }

            item {
                RecentlyPlayedRow(title = "Recently Played", recentlyPlayed = recentlyPlayed)
            }




            item {

                SuggestionsRow(
                    playlistsPager = featuredPlaylists.toPlayListPager(),
                    title = "Featured Playlists"
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

        onClick("2dIGnmEIy1WZIcZCFSj6i8")


    }, onHistoryClicked = {

        onClick("6QeosPQpJckkW0Obir5RT8")

    })
}

