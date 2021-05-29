package com.shaun.spotonmusic.presentation.ui.components.libraryComponents

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.R

@ExperimentalAnimationApi
@Composable
fun LibraryItemRow() {
    val listState = rememberLazyListState()

    val chipsVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }


    LazyColumn(state = listState) {
        item {


            AnimatedVisibility(visible = chipsVisible,
                enter = slideInVertically(
                    initialOffsetY = { -40 }
                ) + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(initialAlpha = 0.1f),
                exit = fadeOut() + slideOutVertically()
            ) {
                LibraryChips()
            }
        }
        repeat(20) {
            item {
                Item()
            }
        }

    }
}

@Preview
@Composable
fun Item(
    title: String = "Whole",
    type: String = "Playlist",
    owner: String = "Shaun",
//    imageUrl:String
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp, start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.spotify_liked),
            contentDescription = null,
            modifier = Modifier.size(64.dp),

            )
        Column(Modifier.padding(start = 10.dp)) {
            Text(text = title, color = Color.White, fontSize = 20.sp)
            Row {
                Text(text = type, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = type, color = Color.Green, fontSize = 12.sp)

            }
        }
    }


}