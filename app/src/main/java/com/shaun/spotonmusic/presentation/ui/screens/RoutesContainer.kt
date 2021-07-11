package com.shaun.spotonmusic.presentation.ui.screens


import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.shaun.spotonmusic.navigation.BottomNavRoutes
import com.shaun.spotonmusic.presentation.ui.activity.HomeActivity
import com.shaun.spotonmusic.presentation.ui.components.library.LibraryBottomSheet
import com.shaun.spotonmusic.presentation.ui.components.navigaiton.BottomNavigationSpotOnMusic
import com.shaun.spotonmusic.presentation.ui.components.navigaiton.HomeScreenNavigationConfiguration
import com.shaun.spotonmusic.presentation.ui.components.nowplaying.PlayerBottomSheet
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.viewmodel.LibraryViewModel
import com.shaun.spotonmusic.viewmodel.MusicPlayerViewModel
import com.shaun.spotonmusic.viewmodel.SharedViewModel
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    context: HomeActivity,
    musicPlayerViewModel: MusicPlayerViewModel,
    homeViewModel: SharedViewModel,
    libraryViewModel: LibraryViewModel,
) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()


    libraryViewModel.tokenExpired.observeForever {
        if (it == true) {
            Toast.makeText(context, "Expired", Toast.LENGTH_SHORT).show()
            context.spotifyAuthClient.refreshAccessToken()
        }
    }


    homeViewModel.tokenExpired.observeForever {
        if (it == true) {
            Toast.makeText(context, "Expired", Toast.LENGTH_SHORT).show()
            context.spotifyAuthClient.refreshAccessToken()


        }
    }

    val currentScreen by homeViewModel.currentScreen.observeAsState()


    val bottomNavItems = listOf(
        BottomNavRoutes.Home,
        BottomNavRoutes.Search,
        BottomNavRoutes.Library
    )

    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )


    val isPlayerOpening = remember {
        mutableStateOf(true)
    }
    musicPlayerViewModel.isCollapsed.observeForever {
        it?.let { collapsed ->

            if (collapsed) {
                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            } else {

                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()

                }

            }
        }
    }


    val currentSort by libraryViewModel.sortMode.observeAsState()



    BottomSheetScaffold(
        sheetContent = {
            PlayerBottomSheet(
                isPlayer = isPlayerOpening.value,
                musicPlayerViewModel = musicPlayerViewModel
            )
        },
        sheetPeekHeight = 0.dp,
        scaffoldState = bottomSheetScaffoldState,

        ) {

        BackHandler(bottomSheetScaffoldState.bottomSheetState.isExpanded) {
            musicPlayerViewModel.isCollapsed.postValue(true)
        }



        ModalBottomSheetLayout(
            sheetContent = {
                LibraryBottomSheet(
                    state,
                    scope,
                    currentSort.toString(),
                    onSortItemClicked = {
                        libraryViewModel.sortItems(it)
                    })
            },
            sheetState = state,
            scrimColor = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(black),


                bottomBar = {

                    if (currentScreen is BottomNavRoutes.Home)
                        BottomNavigationSpotOnMusic(
                            navController = navController,
                            items = bottomNavItems,
                            musicPlayerViewModel = musicPlayerViewModel,
                            nowPlayingClicked = {
                                isPlayerOpening.value = true
                                musicPlayerViewModel.isCollapsed.postValue(false)
                            },
                            onChangePlayerClicked = {

                                Log.d("TAG", "HomeScreen: clicked change player")
                                isPlayerOpening.value = false
                                musicPlayerViewModel.updateDevices()
                                musicPlayerViewModel.isCollapsed.postValue(false)
                            }
                        )

                },
                scaffoldState = scaffoldState,
                content = {

                    HomeScreenNavigationConfiguration(
                        navHostController = navController,
                        sharedViewModel = homeViewModel,
                        tokenExpired = {
                            context.spotifyAuthClient.refreshAccessToken()
                            Handler(Looper.getMainLooper()).postDelayed({
                                homeViewModel.getAccessToken()
                            }, 1000)
                        },
                        modalBottomSheetState = state,
                        libraryViewModel = libraryViewModel,
                        scope = scope,
                        paddingValues = it,
                        musicPlayerViewModel = musicPlayerViewModel
                    )


                }

            )
        }

    }


}


fun playSpotifyMedia(
    spotifyAppRemote: SpotifyAppRemote?,
    spotifyUri: String?,
    shuffle: Boolean = false,
    isPlaylist: Boolean = false
) {


    spotifyUri?.let {
        if (isPlaylist)
            spotifyAppRemote?.playerApi?.setShuffle(shuffle)
        spotifyAppRemote?.playerApi?.play(it)
    }

}