package com.shaun.spotonmusic.presentation.ui.components.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Home
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Library
import com.shaun.spotonmusic.presentation.ui.components.routeScreens.Search
import com.shaun.spotonmusic.presentation.ui.navigation.Routes
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.spotifyGray


@Preview
@Composable
fun HomeScreen() {

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

        }
    ) {
        HomeScreenNavigationConfiguration(navHostController = navController)
    }
}


@Composable
fun BottomNavigationSpotOnMusic(
    navController: NavController,
    items: List<Routes>
) {
    Surface(
        modifier = Modifier
            .background(black)
            .background(black)
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
    navHostController: NavHostController
) {

    NavHost(navController = navHostController, startDestination = Routes.Home.route) {

        composable(Routes.Home.route) {
            Home()
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

