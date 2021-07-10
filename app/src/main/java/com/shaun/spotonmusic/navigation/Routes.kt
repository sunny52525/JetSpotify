package com.shaun.spotonmusic.navigation

import com.shaun.spotonmusic.R


sealed class BottomNavRoutes(val route: String, val resId: Int, val index: Int) {
    object Home : BottomNavRoutes("Home", R.drawable.ic_home, 0)
    object Search : BottomNavRoutes("Search", R.drawable.ic_search, 1)
    object Library : BottomNavRoutes("Library", R.drawable.ic_library, 2)
}


sealed class Routes(val route: String) {
    object AlbumDetail : Routes("AlbumDetail")
    object PlaylistDetail : Routes("PlaylistDetail")
    object PlaylistGrid : Routes("SearchBoxDetail")
    object SearchScreen : Routes("SearchScreen")
    object SearchMain:Routes("SearchMain")
    object Artist:Routes("Artist")


}
