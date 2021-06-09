package com.shaun.spotonmusic.presentation.ui.components.routescreens

import android.util.Log
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
import com.shaun.spotonmusic.database.model.TYPE
import com.shaun.spotonmusic.presentation.ui.components.librarycomponents.Header
import com.shaun.spotonmusic.presentation.ui.components.librarycomponents.LibraryItemRow
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
    onAlbumClicked: (String) -> Unit = {},
    onArtistClicked: (String) -> Unit = {},
    onPlaylistClicked: (String) -> Unit = {}

) {
//
//    val libraryViewModel: LibraryViewModel =
//        hiltViewModel()

//    var items by remember {
//        mutableStateOf(LibraryModel(arrayListOf()))
//    }
//    var sorted by remember {
//        mutableStateOf(LibraryModel(arrayListOf()))
//    }
//
//    var sortMode by remember {
//        mutableStateOf(false)
//    }

    val list: LibraryModel by libraryViewModel.libraryItemsList.observeAsState(
        initial = LibraryModel(
            arrayListOf()
        )
    )

    libraryViewModel.libraryItemsList.observeForever {
        Log.d("TAG", "Library: CHanged")

        it.items.forEach {
            Log.d("TAG", "Library: ${it.title}")
        }
    }

    val isGrid by libraryViewModel.isGrid.observeAsState(initial = true)
    val userDetails by libraryViewModel.userDetails.observeAsState(initial = UserPrivate())

    Column(
        Modifier
            .fillMaxSize()
            .background(spotifyDarkBlack)
    ) {
        Header(userDetails)

        LibraryItemRow(
            modalBottomSheetState = modalBottomSheetState,
            libraryItems = list,
            listState = listStateLibrary,
            scope = scope,
            isGrid = isGrid,
            sortRowCLick = {
                libraryViewModel.updateGrid()
            }, onClick = { type, id ->

                Log.d("TAG", "Library: $type")

                if (type == TYPE.PLAYLIST) {
                    onPlaylistClicked(id)
                }
                if (type == TYPE.ALBUM) {
                    onAlbumClicked(id)
                }

            }, chipSelected = { type, sortModeBool ->
//                Log.d("TAG", "Library: $type,$sortModeBool")
//
//                if (sortModeBool) {
//                    sorted.items = items.items.filter {
//                        it.type == type
//                    } as ArrayList<LibraryItem>
//
//                    Log.d("TAG", "Library: $sorted")
//                } else {
//                    sorted = items
//                    Log.d("TAG", "Library: $items")
//
//                }
//                sortMode = sortModeBool

            })


    }


}