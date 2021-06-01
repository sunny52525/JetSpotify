package com.shaun.spotonmusic.presentation.ui.components.routeScreens


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
import com.shaun.spotonmusic.presentation.ui.components.searchComponents.SearchBar
import com.shaun.spotonmusic.presentation.ui.components.searchComponents.SearchGrid
import com.shaun.spotonmusic.presentation.ui.components.searchComponents.SearchHeading
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.gridColors
import com.shaun.spotonmusic.viewmodel.SharedViewModel
import kaaes.spotify.webapi.android.models.CategoriesPager

private const val TAG = "Search"

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun Search(sharedViewModel: SharedViewModel,onSearchClicked:()->Unit) {

    val categories: CategoriesPager by sharedViewModel.categoriesPager.observeAsState(
        CategoriesPager()
    )





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


        categories.categories?.items?.let {

            if (gridColors?.size!! > 2)
                for (i in 0..it.size step (2)) {
                    if (i + 1 >= it.size)
                        break
                    item {

                        SearchGrid(
                            albumId = Pair(it[i].id, it[i + 1].id),
                            imageUrl = Pair(it[i].icons[0].url, it[i + 1].icons[0].url),
                            title = Pair(it[i].name, it[i + 1].name),
                            color = Pair(gridColors[i], gridColors?.get(i + 1))
                        )
                    }
                }
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }

}