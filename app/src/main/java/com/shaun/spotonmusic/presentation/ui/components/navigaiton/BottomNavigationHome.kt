package com.shaun.spotonmusic.presentation.ui.components.navigaiton

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shaun.spotonmusic.navigation.BottomNavRoutes
import com.shaun.spotonmusic.presentation.ui.components.nowplaying.BottomTrackController
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.shaun.spotonmusic.viewmodel.MusicPlayerViewModel
import com.spotify.android.appremote.api.SpotifyAppRemote


@Composable
fun BottomNavigationSpotOnMusic(
    navController: NavController,
    items: List<BottomNavRoutes>,
    musicPlayerViewModel: MusicPlayerViewModel,
    onChangePlayerClicked:()->Unit,
    nowPlayingClicked: () -> Unit
) {

    val trackName: String by musicPlayerViewModel.trackName.observeAsState(initial = "")
    val artistName: String by musicPlayerViewModel.singerName.observeAsState(initial = "")
    val imageUrl: String by musicPlayerViewModel.imageUrl.observeAsState(initial = "")
    val seekState: Float by musicPlayerViewModel.seekState.observeAsState(0.0f)
    var spotifyAppRemote: SpotifyAppRemote? = null

    val likesTheSong by musicPlayerViewModel.likesThisSong.observeAsState(initial = false)
    val isPlaying by musicPlayerViewModel.isPlaying.observeAsState(initial = false)

    musicPlayerViewModel.spotifyRemote.observeForever {
        spotifyAppRemote = it
    }


    Column {
        BottomTrackController(
            trackName = trackName,
            seekState = seekState,
            imageUrl = imageUrl,
            nowPlayingClicked = nowPlayingClicked,
            artistName = artistName,
            isPlaying = isPlaying,
            spotifyAppRemote = spotifyAppRemote,
            hasLiked = likesTheSong,
            onChangePlayerClicked = onChangePlayerClicked,
            onLikeClicked = {
                musicPlayerViewModel.toggleLikeSong()
            }
        )
        BottomNavigationHome(
            navController = navController,
            items = items
        )


    }

}

@Composable
fun BottomNavigationHome(
    navController: NavController,
    items: List<BottomNavRoutes>
) {
    androidx.compose.material.BottomNavigation(
        modifier = Modifier
            .background(black)
            .background(black)

    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val dimension by remember {
            mutableStateOf(arrayListOf(20, 20, 20))
        }

        items.forEach {
            BottomNavigationItem(
                selected = currentRoute == it.route, onClick = {

                    navController.navigate(it.route) {
                        launchSingleTop = true
                    }

                    dimension.forEachIndexed { index, _ ->
                        if (index == it.index)
                            dimension[index] = 21
                        else
                            dimension[index] = 20
                    }

                },
                modifier = Modifier
                    .background(spotifyGray)
                    .animateContentSize(),
                label = { Text(text = it.route) },

                icon = {

                    Icon(
                        painter = painterResource(id = it.resId),
                        contentDescription = "",
                        modifier = Modifier
                            .width(dimension[it.index].dp)
                            .height(dimension[it.index].dp)
                            .animateContentSize()


                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}

