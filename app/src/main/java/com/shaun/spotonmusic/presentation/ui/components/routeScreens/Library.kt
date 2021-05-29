package com.shaun.spotonmusic.presentation.ui.components.routeScreens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shaun.spotonmusic.presentation.ui.components.libraryComponents.Header
import com.shaun.spotonmusic.presentation.ui.components.libraryComponents.LibraryItemRow
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.viewmodel.SharedViewModel


@ExperimentalAnimationApi
@Composable
fun Library(viewModel: SharedViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .background(spotifyDarkBlack)
    ) {

        Header()
        LibraryItemRow()
    }
}