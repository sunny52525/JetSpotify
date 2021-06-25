@file:Suppress("UNUSED_VARIABLE")

package com.shaun.spotonmusic.presentation.ui.screens


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shaun.spotonmusic.navigation.BottomNavRoutes
import com.shaun.spotonmusic.navigation.Routes
import com.shaun.spotonmusic.presentation.ui.activity.HomeActivity
import com.shaun.spotonmusic.presentation.ui.animation.SlideInEnterAnimation
import com.shaun.spotonmusic.presentation.ui.components.library.LibraryBottomSheet
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.shaun.spotonmusic.viewmodel.*
import com.spotify.android.appremote.api.SpotifyAppRemote
import kaaes.spotify.webapi.android.models.UserPrivate
import kotlinx.coroutines.CoroutineScope

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    context: HomeActivity,
    homeViewModel: SharedViewModel
) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()


    val libraryViewModel: LibraryViewModel = viewModel()


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
    var currentSort by remember {
        mutableStateOf("Recently Played")
    }
    ModalBottomSheetLayout(
        sheetContent = {
            LibraryBottomSheet(state, scope, currentSort, onSortItemClicked = {
                currentSort = it
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
                        items = bottomNavItems
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
                )
            }

        )
    }


}


@Composable
fun BottomNavigationSpotOnMusic(
    navController: NavController,
    items: List<BottomNavRoutes>
) {
    Surface(
        modifier = Modifier

    ) {
        BottomNavigation(
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
                            painter = painterResource(id = it.resId), contentDescription = "",
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

}


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun HomeScreenNavigationConfiguration(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel,
    tokenExpired: () -> Unit,
    modalBottomSheetState: ModalBottomSheetState,
    libraryViewModel: LibraryViewModel,
    scope: CoroutineScope,

    ) {
    val listState = rememberLazyListState()
    val listStateLibrary = rememberLazyListState()





    NavHost(
        navController = navHostController,
        startDestination = BottomNavRoutes.Home.route,
        modifier = Modifier.background(
            black
        )
    ) {

        composable(BottomNavRoutes.Home.route) {

            SlideInEnterAnimation {
                Home(viewModel = sharedViewModel, listState = listState, tokenExpired = {
                    tokenExpired()
                }, onPlayListClicked = {
                    Log.d("", "MOODD: $it")
                    navHostController.navigate(Routes.PlaylistDetail.route + "/$it")
                }, onAlbumClicked = {
                    navHostController.navigate(Routes.AlbumDetail.route + "/$it")

                }
                )
            }
        }
        composable(BottomNavRoutes.Search.route) {
            SlideInEnterAnimation {

                Search(sharedViewModel, onSearchClicked = {


                }, onCategoryClicked = { id, color ->
                    navHostController.currentBackStackEntry?.arguments = Bundle().apply {
                        putInt("color", color)

                    }

                    navHostController.navigate(Routes.PlaylistGrid.route + "/$id")
                })

            }

        }
        composable(BottomNavRoutes.Library.route) {

            SlideInEnterAnimation {

                Library(
                    viewModel = sharedViewModel,
                    modalBottomSheetState = modalBottomSheetState,
                    libraryViewModel,
                    scope = scope,
                    listStateLibrary = listStateLibrary, onPlaylistClicked = {
                        Log.d("", "MOODD: $it")
                        navHostController.navigate(Routes.PlaylistDetail.route + "/$it")
                    }, onAlbumClicked = {
                        navHostController.navigate(Routes.AlbumDetail.route + "/$it")


                    }
                )
            }


        }
        composable(Routes.PlaylistDetail.route + "/{id}") { it ->
            val id = it.arguments?.getString("id")
            val playlistDetailViewModel = hiltViewModel<PlaylistDetailViewModel>()


            val myDetails: UserPrivate by sharedViewModel.myDetails.observeAsState(UserPrivate())

            myDetails.id.let { userId ->
                if (userId != null)
                    playlistDetailViewModel.setUserId(id.toString(), userId = userId)
            }
            SlideInEnterAnimation {
                PlaylistDetail(
                    id,
                    updatePlaylist = {
                        libraryViewModel.getLibraryItems()
                    },
                    viewModel = playlistDetailViewModel,
                    onShufflePlayListClicked = {
                        playSpotifyMedia(sharedViewModel.spotifyRemote.value, it)
                    },
                    onSongClicked = {
                        playSpotifyMedia(sharedViewModel.spotifyRemote.value, it)
                    }
                )

            }
        }
        composable(Routes.AlbumDetail.route + "/{id}") {

            val id = it.arguments?.getString("id")

            val albumDetailViewModel = hiltViewModel<AlbumDetailViewModel>()
            albumDetailViewModel.setUserId(id.toString())


            SlideInEnterAnimation {

                AlbumDetail(
                    id = id,
                    viewModel = albumDetailViewModel,
                    onAlbumPlayed = {
                        playSpotifyMedia(sharedViewModel.spotifyRemote.value, it)
                    }) {
                    playSpotifyMedia(sharedViewModel.spotifyRemote.value, it)
                }
            }
        }

        composable(Routes.PlaylistGrid.route + "/{id}") {
            val id = it.arguments?.getString("id")
            val playlistGridViewModel = hiltViewModel<PlaylistGridViewModel>()

            playlistGridViewModel.setCategory(id.toString())

            val color = navHostController.previousBackStackEntry?.arguments?.getInt("color")

            SlideInEnterAnimation {
                PlaylistGridScreen(
                    id = id,
                    color = color ?: 0,
                    viewModel = playlistGridViewModel,
                    title = id,
                    onPlaylistClicked = {
                        navHostController.navigate(Routes.PlaylistDetail.route + "/$it")
                    }
                )
            }
        }
    }
}


fun playSpotifyMedia(spotifyAppRemote: SpotifyAppRemote?, spotifyUri: String?) {

    println(spotifyUri)
    println(spotifyAppRemote)
    spotifyUri?.let {
        spotifyAppRemote?.playerApi?.play(it)
    }

}