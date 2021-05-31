package com.shaun.spotonmusic.presentation.ui.components.routeScreens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.shaun.spotonmusic.database.model.LibraryModel
import com.shaun.spotonmusic.presentation.ui.components.libraryComponents.Header
import com.shaun.spotonmusic.presentation.ui.components.libraryComponents.LibraryItemRow
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.viewmodel.LibraryViewModel
import com.shaun.spotonmusic.viewmodel.SharedViewModel
import kaaes.spotify.webapi.android.models.UserPrivate
import kotlinx.coroutines.CoroutineScope

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun Library(
    viewModel: SharedViewModel,
    modalBottomSheetState: ModalBottomSheetState,
    libraryViewModel: LibraryViewModel,
    scope: CoroutineScope,
    listStateLibrary: LazyListState,

    ) {


    val libraryItems by libraryViewModel.libraryItemsList.observeAsState(
        initial = LibraryModel(
            arrayListOf()
        )
    )


    val isGrid by libraryViewModel.isGrid.observeAsState(initial = true)
    val userDetails by libraryViewModel.userDetails.observeAsState(initial = UserPrivate())

    Column(
        Modifier
            .fillMaxSize()
            .background(spotifyDarkBlack)
    ) {
        Header(userDetails)

        LibraryItemRow(
            modalBottomSheetState,
            libraryItems,
            listStateLibrary,
            scope,
            isGrid,
            sortRowCLick = {
                libraryViewModel.updateGrid()
            })


    }


}