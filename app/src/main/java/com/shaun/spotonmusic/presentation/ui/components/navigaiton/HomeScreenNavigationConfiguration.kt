package com.shaun.spotonmusic.presentation.ui.components.navigaiton

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shaun.spotonmusic.navigation.BottomNavRoutes
import com.shaun.spotonmusic.navigation.Routes
import com.shaun.spotonmusic.presentation.ui.screens.*
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.viewmodel.*
import kaaes.spotify.webapi.android.models.UserPrivate
import kotlinx.coroutines.CoroutineScope


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

                Home(viewModel = sharedViewModel, listState = listState, tokenExpired = {
                    tokenExpired()
                }, onPlayListClicked = {
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

        navigation(
            startDestination = Routes.SearchMain.route,
            route = BottomNavRoutes.Search.route
        ) {
            composable(Routes.SearchMain.route) {

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


                Library(
                    modalBottomSheetState = modalBottomSheetState,
                    libraryViewModel = libraryViewModel,
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
        composable(Routes.PlaylistDetail.route + "/{id}") { it ->
            val id = it.arguments?.getString("id")
            val playlistDetailViewModel = hiltViewModel<PlaylistDetailViewModel>()


            val myDetails: UserPrivate by sharedViewModel.myDetails.observeAsState(UserPrivate())

            myDetails.id.let { userId ->
                if (userId != null)
                    playlistDetailViewModel.setUserId(id.toString(), userId = userId)
            }

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
        composable(Routes.AlbumDetail.route + "/{id}") {

            val id = it.arguments?.getString("id")

            val albumDetailViewModel = hiltViewModel<AlbumDetailViewModel>()
            albumDetailViewModel.setUserId(id.toString())



                AlbumDetail(
                    id = id,
                    viewModel = albumDetailViewModel,
                    onAlbumPlayed = { albumId ->
                        playSpotifyMedia(musicPlayerViewModel.spotifyRemote.value, albumId)
                    }) { songId ->
                    playSpotifyMedia(musicPlayerViewModel.spotifyRemote.value, songId)
                }
        }

        composable(Routes.PlaylistGrid.route + "/{id}") {
            val id = it.arguments?.getString("id")
            val playlistGridViewModel = hiltViewModel<PlaylistGridViewModel>()

            playlistGridViewModel.setCategory(id.toString())

            val color = navHostController.previousBackStackEntry?.arguments?.getInt("color")

                PlaylistGridScreen(
                    id = id,
                    color = color ?: 0,
                    viewModel = playlistGridViewModel,
                    onPlaylistClicked = { playlistId ->
                        navHostController.navigate(Routes.PlaylistDetail.route + "/$playlistId")
                    }
                )
        }

        composable(Routes.Artist.route + "/{id}") {
            val id = it.arguments?.getString("id")
            val albumDetailViewModel = hiltViewModel<ArtistDetailViewModel>()
            albumDetailViewModel.setArtist(id.toString())

            val likedSongs by libraryViewModel.likedSongs.observeAsState()



            ArtistPage(

                albumDetailViewModel,
                onAlbumClicked = { albumId ->
                    navHostController.navigate(Routes.AlbumDetail.route + "/$albumId") {
                        restoreState = true
                    }
                },
                onSongClicked = {songId->
                    playSpotifyMedia(musicPlayerViewModel.spotifyRemote.value, songId)
                },
                items = likedSongs?.items,
                updatePlaylist = {
                    libraryViewModel.getLibraryItems()
                },

                onArtistClicked = { artistID ->
                    navHostController.navigate(Routes.Artist.route + "/$artistID") {
                        restoreState = true
                    }
                })

        }


    }
}