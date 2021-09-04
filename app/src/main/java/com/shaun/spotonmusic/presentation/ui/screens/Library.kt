package com.shaun.spotonmusic.presentation.ui.screens

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
import com.shaun.spotonmusic.presentation.ui.components.library.Header
import com.shaun.spotonmusic.presentation.ui.components.library.LibraryItemRow
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
    modalBottomSheetState: ModalBottomSheetState,
    libraryViewModel: LibraryViewModel,
    scope: CoroutineScope,
    listStateLibrary: LazyListState,
    onAlbumClicked: (String) -> Unit = {},
    onArtistClicked: (String) -> Unit = {},
    onPlaylistClicked: (String) -> Unit = {}

) {

    val list: LibraryModel by libraryViewModel.libraryItemsList.observeAsState(
        initial = LibraryModel(
            arrayListOf()
        )
    )


    val isGrid by libraryViewModel.isGrid.observeAsState(initial = true)
    val userDetails by libraryViewModel.userDetails.observeAsState(initial = UserPrivate())
    val chipItem by libraryViewModel.chipSelected.observeAsState()
    val sortMode by libraryViewModel.sortMode.observeAsState()

    libraryViewModel.isChipSelected.observeForever {
        if (!it)
            libraryViewModel.chipSelected.postValue("")
    }
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
                } else if (type == TYPE.ALBUM) {
                    onAlbumClicked(id)
                } else if (type == TYPE.ARTIST
                ) {
                    onArtistClicked(id)
                }

            }, chipSelected = { type, sortModeBool ->


                libraryViewModel.groupItems(type, isSort = sortModeBool)


            }, chipItemSelected = chipItem,
            sortMode=sortMode
        )


    }


}