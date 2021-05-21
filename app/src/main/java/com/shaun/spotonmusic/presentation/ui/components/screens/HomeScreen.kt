package com.shaun.spotonmusic.presentation.ui.components.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.shaun.spotonmusic.presentation.ui.activity.HomeActivity
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Home
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Library
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Search
import com.shaun.spotonmusic.presentation.ui.navigation.Routes
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.shaun.spotonmusic.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(
    context: HomeActivity,
    viewModel: HomeScreenViewModel
) {

    val navController = rememberNavController()

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
        content = {
            HomeScreenNavigationConfiguration(
                navController, context
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
            var dimension by remember {
                mutableStateOf(arrayListOf(20, 20, 20))
            }
            val currentRoute = currentRoute(navController)
            items.forEach {
                BottomNavigationItem(
                    selected = currentRoute == it.route, onClick = {

                        navController.navigate(it.route)

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
    navHostController: NavHostController, context: HomeActivity
) {

    NavHost(
        navController = navHostController,
        startDestination = Routes.Home.route,
        modifier = Modifier.background(
            black
        )
    ) {

        composable(Routes.Home.route) {
            val factory = HiltViewModelFactory(LocalContext.current, it)
            val viewModel: HomeScreenViewModel = viewModel("HomeScreenViewModel", factory)


            Home(viewModel = viewModel, context = context)
        }
        composable(Routes.Search.route) {
            Search()

        }
        composable(Routes.Library.route) {
            Library()

        }
    }
}


@Composable
private fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(KEY_ROUTE)
}

