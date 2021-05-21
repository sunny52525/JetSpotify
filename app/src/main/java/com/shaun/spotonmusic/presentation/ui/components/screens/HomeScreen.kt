package com.shaun.spotonmusic.presentation.ui.components.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Home
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Library
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Search
import com.shaun.spotonmusic.presentation.ui.navigation.Routes
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.shaun.spotonmusic.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(
) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val homeViewModel: HomeScreenViewModel = viewModel()

    homeViewModel.accessToken.observeForever {
        Log.d("TAG", "HomeScreen: $it")
    }
    val accessToken: String by homeViewModel.accessToken.observeAsState("")

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
            BottomNavigationSpotOnMusic(navController = navController, items = bottomNavItems)

        },
        scaffoldState = scaffoldState,
        content = {

            Text(text = accessToken)
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
            val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

            var dimension by remember {
                mutableStateOf(arrayListOf(20, 20, 20))
            }

            items.forEach {
                BottomNavigationItem(
                    selected = currentRoute == it.route, onClick = {

                        navController.navigate(it.route) {
                            popUpTo = navController.graph.startDestination
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


@Composable
fun HomeScreenNavigationConfiguration(
    navHostController: NavHostController, viewModel: HomeScreenViewModel
) {

    NavHost(
        navController = navHostController,
        startDestination = Routes.Home.route,
        modifier = Modifier.background(
            black
        )
    ) {

        composable(Routes.Home.route) {
            Home(viewModel = viewModel)
        }
        composable(Routes.Search.route) {
            Search(viewModel)

        }
        composable(Routes.Library.route) {
            Library(viewModel)

        }
    }
}
