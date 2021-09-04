package com.shaun.spotonmusic.presentation.ui.screens


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.presentation.ui.components.search.SearchBar
import com.shaun.spotonmusic.presentation.ui.components.search.SearchGrid
import com.shaun.spotonmusic.presentation.ui.components.search.SearchHeading
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.gridColors
import com.shaun.spotonmusic.viewmodel.SharedViewModel

private const val TAG = "Search"

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun Search(
    sharedViewModel: SharedViewModel, onSearchClicked: () -> Unit,
    onCategoryClicked: (String, Int) -> Unit
) {

    val categories: List<SpotOnMusicModel> by sharedViewModel.categoriesPager.observeAsState(
        listOf()
    )




    Log.d(TAG, "Search: ${gridColors.size}")

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(black)
    ) {
        item {
            SearchHeading()
        }
        stickyHeader {
            Spacer(modifier = Modifier.height(10.dp))
            SearchBar(onSearch = {
                onSearchClicked()
            })
        }


        categories.let {

            if (gridColors.size > 2)
                for (i in 0..it.size step (2)) {
                    if (i + 1 >= it.size)
                        break
                    item {

                        SearchGrid(
                            albumId = Pair(it[i].id, it[i + 1].id),
                            imageUrl = Pair(it[i].imageUrls[0], it[i + 1].imageUrls[0]),
                            title = Pair(it[i].title, it[i + 1].title),
                            color = Pair(gridColors[i], gridColors[i + 1]),
                            onCardClicked = { category, index ->
                                Log.d(TAG, "Search: $i")
                                onCategoryClicked(category, index + i)

                            }
                        )
                    }
                }
        }


    }

}