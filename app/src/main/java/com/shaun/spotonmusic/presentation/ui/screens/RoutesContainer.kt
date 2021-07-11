package com.shaun.spotonmusic.presentation.ui.screens


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.navigation.BottomNavRoutes
import com.shaun.spotonmusic.navigation.Routes
import com.shaun.spotonmusic.presentation.ui.activity.HomeActivity
import com.shaun.spotonmusic.presentation.ui.animation.SlideInEnterAnimation
import com.shaun.spotonmusic.presentation.ui.components.library.LibraryBottomSheet
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.green
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.shaun.spotonmusic.viewmodel.*
import com.spotify.android.appremote.api.SpotifyAppRemote
import kaaes.spotify.webapi.android.models.UserPrivate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    context: HomeActivity,
    musicPlayerViewModel: MusicPlayerViewModel,
) {


    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val activity = LocalContext.current as Activity
    val libraryViewModel: LibraryViewModel = viewModel()
    val homeViewModel: SharedViewModel = viewModel()


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
            NowPlaying(musicPlayerViewModel)
        },
        sheetPeekHeight = 0.dp,
        scaffoldState = bottomSheetScaffoldState,

        ) {

        BackHandler(bottomSheetScaffoldState.bottomSheetState.isExpanded) {
            scope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
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


@Composable
fun BottomNavigationSpotOnMusic(
    navController: NavController,
    items: List<BottomNavRoutes>,
    musicPlayerViewModel: MusicPlayerViewModel,
    nowPlayingClicked: () -> Unit
) {

    val trackName: String by musicPlayerViewModel.trackName.observeAsState(initial = "")
    val artistName: String by musicPlayerViewModel.singerName.observeAsState(initial = "")
    val imageUrl: String by musicPlayerViewModel.imageUrl.observeAsState(initial = "")
    val seekState: Float by musicPlayerViewModel.seekState.observeAsState(0.0f)
    var spotifyAppRemote: SpotifyAppRemote? = null

    val isPlaying by musicPlayerViewModel.isPlaying.observeAsState(initial = false)

    musicPlayerViewModel.spotifyRemote.observeForever {
        spotifyAppRemote = it
    }




    Surface(
        modifier = Modifier

    ) {

        Column {
            Column(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(spotifyGray)
            ) {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .background(Color.White)
                        .fillMaxWidth(seekState)
                        .animateContentSize()
                ) {

                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(spotifyGray)
            ) {

                Image(
                    painter = rememberGlidePainter(request = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(60.dp),
                )

                Column(
                    Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            nowPlayingClicked()
                        }
                        .fillMaxWidth(0.7f)
                ) {
                    Text(
                        text = trackName,
                        color = Color.White,
                        fontWeight = Bold,
                        fontSize = 15.sp,
                        maxLines = 1
                    )
                    Text(
                        text = artistName,
                        color = Color.Gray,
                        fontWeight = Normal,
                        fontSize = 13.sp
                    )
                }
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .width(150.dp)
                            .padding(end = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cast),
                            contentDescription = "Cast",
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(Color.Gray)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_like),
                            contentDescription = "Like",
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(green)
                        )
                        Image(
                            painter = if (isPlaying) painterResource(id = R.drawable.ic_pause) else painterResource(
                                id = R.drawable.ic_play
                            ),
                            contentDescription = "Like",
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    if (isPlaying)
                                        spotifyAppRemote?.playerApi?.pause()
                                    else
                                        spotifyAppRemote?.playerApi?.resume()

                                },
                            colorFilter = ColorFilter.tint(Color.White),
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(black)
            )
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
    }

}


@DelicateCoroutinesApi
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
    paddingValues: PaddingValues,
    musicPlayerViewModel: MusicPlayerViewModel,

    ) {
    val listState = rememberLazyListState()
    val listStateLibrary = rememberLazyListState()






    NavHost(
        navController = navHostController,
        startDestination = BottomNavRoutes.Home.route,
        modifier = Modifier
            .background(
                black
            )
            .padding(paddingValues)
    ) {

        composable(BottomNavRoutes.Home.route) {

            SlideInEnterAnimation {
                Home(viewModel = sharedViewModel, listState = listState, tokenExpired = {
                    tokenExpired()
                }, onPlayListClicked = {
                    Log.d("", "MOODD: $it")
                    navHostController.navigate(Routes.PlaylistDetail.route + "/$it") {
                        restoreState = true

                    }
                }, onAlbumClicked = {
                    navHostController.navigate(Routes.AlbumDetail.route + "/$it")
                    {
                        restoreState = true

                    }
                }, onArtistClicked = {
                    navHostController.navigate(Routes.Artist.route + "/$it") {
                        restoreState = true

                    }
                }
                )
            }
        }

        navigation(
            startDestination = Routes.SearchMain.route,
            route = BottomNavRoutes.Search.route
        ) {
            composable(Routes.SearchMain.route) {
                SlideInEnterAnimation {

                    Search(sharedViewModel, onSearchClicked = {

                        navHostController.navigate(Routes.SearchScreen.route)

                    }, onCategoryClicked = { id, color ->
                        navHostController.currentBackStackEntry?.arguments = Bundle().apply {
                            putInt("color", color)

                        }

                        navHostController.navigate(Routes.PlaylistGrid.route + "/$id") {
                            restoreState = true

                        }
                    })

                }


            }
            composable(Routes.SearchScreen.route) {
                val searchScreenMainViewModel = hiltViewModel<SearchViewModel>()


                SearchScreen(
                    searchScreenMainViewModel = searchScreenMainViewModel,
                    scope = scope,
                    onPlaylistClicked = {
                        navHostController.navigate(Routes.PlaylistDetail.route + "/$it") {
                            restoreState = true

                        }
                    },
                    onArtistClicked = {
                        navHostController.navigate(Routes.Artist.route + "/$it") {
                            restoreState = true

                        }
                    },
                    onAlbumClicked = {
                        navHostController.navigate(Routes.AlbumDetail.route + "/$it")
                        {
                            restoreState = true

                        }
                    }) {
                    playSpotifyMedia(musicPlayerViewModel.spotifyRemote.value, it)
                }
            }
        }

        composable(route = BottomNavRoutes.Library.route) {

            SlideInEnterAnimation {

                Library(
                    viewModel = sharedViewModel,
                    modalBottomSheetState = modalBottomSheetState,
                    libraryViewModel,
                    scope = scope,
                    listStateLibrary = listStateLibrary, onPlaylistClicked = {

                        navHostController.navigate(Routes.PlaylistDetail.route + "/$it") {
                            restoreState = true

                        }
                    }, onAlbumClicked = {
                        navHostController.navigate(Routes.AlbumDetail.route + "/$it") {
                            restoreState = true

                        }

                    }, onArtistClicked = {
                        navHostController.navigate(Routes.Artist.route + "/$it") {
                            restoreState = true

                        }
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
                        playSpotifyMedia(
                            musicPlayerViewModel.spotifyRemote.value, it,
                            shuffle = true,
                            isPlaylist = true
                        )
                    },
                    onSongClicked = {
                        playSpotifyMedia(musicPlayerViewModel.spotifyRemote.value, it)
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
                        playSpotifyMedia(musicPlayerViewModel.spotifyRemote.value, it)
                    }) {
                    playSpotifyMedia(musicPlayerViewModel.spotifyRemote.value, it)
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

        composable(Routes.Artist.route + "/{id}") {
            val id = it.arguments?.getString("id")
            val albumDetailViewModel = hiltViewModel<ArtistDetailViewModel>()
            albumDetailViewModel.setArtist(id.toString())

            val likedSongs by libraryViewModel.likedSongs.observeAsState()


            val artist by albumDetailViewModel.artist.observeAsState()
            SlideInEnterAnimation {
                ArtistPage(artist = artist,
                    albumDetailViewModel,
                    onAlbumClicked = {
                        navHostController.navigate(Routes.AlbumDetail.route + "/$it") {
                            restoreState = true
                        }
                    },
                    onSongClicked = { id ->
                        playSpotifyMedia(musicPlayerViewModel.spotifyRemote.value, id)
                    },
                    items = likedSongs?.items,
                    updatePlaylist = {
                        libraryViewModel.getLibraryItems()
                    },
                    onArtistClicked = { artist ->
                        navHostController.navigate(Routes.Artist.route + "/$artist") {
                            restoreState = true
                        }
                    })
            }

        }


    }
}


fun playSpotifyMedia(
    spotifyAppRemote: SpotifyAppRemote?,
    spotifyUri: String?,
    shuffle: Boolean = false,
    isPlaylist: Boolean = false
) {

    println(spotifyUri)
    println(spotifyAppRemote)
    spotifyUri?.let {
        if (isPlaylist)
            spotifyAppRemote?.playerApi?.setShuffle(shuffle)
        spotifyAppRemote?.playerApi?.play(it)
    }

}