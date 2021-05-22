package com.shaun.spotonmusic.presentation.ui.components.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Home
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Library
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Search
import com.shaun.spotonmusic.presentation.ui.navigation.Routes
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.shaun.spotonmusic.viewmodel.HomeScreenViewModel

@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    context: HomeActivity
) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val homeViewModel: HomeScreenViewModel = viewModel()

    homeViewModel.tokenExpired.observeForever {
        if (it == true) {
            context.spotifyAuthClient.refreshAccessToken()
            homeViewModel.tokenExpired.value = false
            homeViewModel.getAccessToken()
        }
    }

    val currentScreen by homeViewModel.currentScreen.observeAsState()


    val bottomNavItems = listOf(
        Routes.Home,
        Routes.Search,
        Routes.Library
    )


    Scaffold(

        modifier = Modifier
            .fillMaxSize()
            .background(black),


        bottomBar = {

            if (currentScreen is Routes.Home)
                BottomNavigationSpotOnMusic(navController = navController, items = bottomNavItems)

        },
        scaffoldState = scaffoldState,
        content = {

            HomeScreenNavigationConfiguration(
                navController, homeViewModel
            )
        }

    )
}


@Composable
fun BottomNavigationSpotOnMusic(
    navController: NavController,
    items: List<Routes>
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

                        dimension.forEachIndexed { index, i ->
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


@ExperimentalAnimationApi
@Composable
fun HomeScreenNavigationConfiguration(
    navHostController: NavHostController, viewModel: HomeScreenViewModel
) {
    val listState = rememberLazyListState()
    NavHost(
        navController = navHostController,
        startDestination = Routes.Home.route,
        modifier = Modifier.background(
            black
        )
    ) {

        composable(Routes.Home.route) {

            EnterAnimation {
                Home(viewModel, listState)
            }
        }
        composable(Routes.Search.route) {
            EnterAnimation {

                Search(viewModel)

            }

        }
        composable(Routes.Library.route) {
            EnterAnimation {

                Library(viewModel)

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