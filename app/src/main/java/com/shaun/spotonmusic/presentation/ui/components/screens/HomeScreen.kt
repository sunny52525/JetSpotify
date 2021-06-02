package com.shaun.spotonmusic.presentation.ui.components.screens

import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shaun.spotonmusic.presentation.ui.activity.HomeActivity
import com.shaun.spotonmusic.presentation.ui.components.libraryComponents.LibraryBottomSheet
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.AlbumDetail
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Home
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Library
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Search
import com.shaun.spotonmusic.presentation.ui.navigation.BottomNavRoutes
import com.shaun.spotonmusic.presentation.ui.navigation.Routes
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.shaun.spotonmusic.viewmodel.AlbumDetailViewModel
import com.shaun.spotonmusic.viewmodel.LibraryViewModel
import com.shaun.spotonmusic.viewmodel.SharedViewModel
import kotlinx.coroutines.CoroutineScope

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    context: HomeActivity
) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val homeViewModel: SharedViewModel = viewModel()
    val libraryViewModel: LibraryViewModel = viewModel()
    val albumDetailViewModel: AlbumDetailViewModel = viewModel()

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
                    viewModel = homeViewModel,
                    tokenExpired = {
                        context.spotifyAuthClient.refreshAccessToken()
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            homeViewModel.getAccessToken()
                        }, 1000)
                    },
                    modalBottomSheetState = state,
                    libraryViewModel = libraryViewModel,
                    scope = scope,
                    albumDetailViewModel = albumDetailViewModel
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
    navHostController: NavHostController, viewModel: SharedViewModel,
    tokenExpired: () -> Unit,
    modalBottomSheetState: ModalBottomSheetState,
    libraryViewModel: LibraryViewModel,
    scope: CoroutineScope,
    albumDetailViewModel: AlbumDetailViewModel,

    ) {
    val listState = rememberLazyListState()
    val listStateLibrary = rememberLazyListState()
    NavHost(
        navController = navHostController,
        startDestination = BottomNavRoutes.Home.route,
//        startDestination = Routes.AlbumDetail.route,
        modifier = Modifier.background(
            black
        )
    ) {

        composable(BottomNavRoutes.Home.route) {

            EnterAnimation {
                Home(viewModel = viewModel, listState = listState, tokenExpired = {
                    tokenExpired()
                }, onAlbumClicked = {
                    navHostController.navigate(Routes.AlbumDetail.route + "/$it")
                }
                )
            }
        }
        composable(BottomNavRoutes.Search.route) {
            EnterAnimation {

                Search(viewModel, onSearchClicked = {

                })

            }

        }
        composable(BottomNavRoutes.Library.route) {

            EnterAnimation {

                Library(
                    viewModel = viewModel,
                    modalBottomSheetState = modalBottomSheetState,
                    libraryViewModel = libraryViewModel,
                    scope = scope,
                    listStateLibrary = listStateLibrary
                )
            }


        }
        composable(Routes.AlbumDetail.route + "/{id}") {
            EnterAnimation {
                AlbumDetail(
                    it.arguments?.getString("id"),
                    albumDetailViewModel
                )

            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun EnterAnimation(content: @Composable () -> Unit) {


    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        content = content,
        initiallyVisible = false
    )

}