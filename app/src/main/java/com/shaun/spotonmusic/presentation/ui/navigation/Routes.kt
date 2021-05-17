package com.shaun.spotonmusic.presentation.ui.navigation

import com.shaun.spotonmusic.R


sealed class Routes(val route: String,val resId: Int,val index:Int) {
    object Home : Routes("Home", R.drawable.ic_home,0)
    object Search : Routes("Search", R.drawable.ic_search,1)
    object Library : Routes("Library", R.drawable.ic_library,2)


}
