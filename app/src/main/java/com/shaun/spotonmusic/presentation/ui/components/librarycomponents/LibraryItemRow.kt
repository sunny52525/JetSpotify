package com.shaun.spotonmusic.presentation.ui.components.librarycomponents

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.database.model.LibraryModel
import com.shaun.spotonmusic.utils.TypeConverters.Companion.toListString
import com.shaun.spotonmusic.utils.getImageUrl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun LibraryItemRow(
    modalBottomSheetState: ModalBottomSheetState,
    libraryItems: LibraryModel,
    listState: LazyListState,
    scope: CoroutineScope,
    isGrid:Boolean,
    sortRowCLick:()->Unit
) {
    var previousOffset = 0



    val chipsVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex < 1 || listState.firstVisibleItemIndex - previousOffset < 0
        }
    }

    launchHelper(scope,
        catchBlock = {},
        finallyBlock = {}) {
        listState.interactionSource.interactions.collect {
            previousOffset = listState.firstVisibleItemIndex
        }
    }



    LazyColumn(
        state = listState, modifier = Modifier
            .fillMaxHeight()
    )

    {


        stickyHeader {

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
        item {
            SortRow(modalBottomSheetState, onChangeViewCLicked = {
               sortRowCLick()
            },isGrid)
        }

        libraryItems.items.forEachIndexed { index, libraryItem ->
            item {
                if (isGrid)
                    Item(
                        title = libraryItem.title,
                        type = libraryItem.type,
                        owner = libraryItem.owner,
                        imageUrl = if (libraryItem.imageUrl.isEmpty()) "" else libraryItem.imageUrl[0].url
                    )
            }

        }



        for (i in 0 until libraryItems.items.size - 1 step 2) {
            val item = libraryItems.items
            item {

                if (!isGrid)
                    LibraryGrid(
                        title = Pair(item[i].title, item[i + 1].title),
                        type = Pair(item[i].type, item[i + 1].title),
                        owner = Pair(item[i].owner, item[i + 1].owner),
                        imageUrl = Pair(
                            getImageUrl(item[i].imageUrl.toListString(), 1),
                            getImageUrl(item[i + 1].imageUrl.toListString(), 1)
                        )
                    )

            }

        }

        if (libraryItems.items.size % 1 == 1) {
            if (!isGrid)
                item {

                    val i = libraryItems.items.size - 1
                    val item = libraryItems.items

                    LibraryGrid(
                        title = Pair(item[i].title, ""),
                        type = Pair(item[i].type, ""),
                        owner = Pair(item[i].owner, ""),
                        imageUrl = Pair(
                            getImageUrl(item[i].imageUrl.toListString(), 1),
                            ""
                        ),
                        count = 1
                    )
                }


        }
        item {

            Spacer(modifier = Modifier.height(60.dp))
        }

    }
}

@Composable
fun Item(
    title: String = "Whole",
    type: String = "Playlist",
    owner: String = "Shaun",
    imageUrl: String,

    ) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 7.dp, start = 20.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (imageUrl.isNotEmpty()) rememberGlidePainter(
                request = imageUrl
            ) else {
                painterResource(id = R.drawable.ic_spotify_no_name)
            },
            contentDescription = null,
            modifier = Modifier.size(64.dp),

            )
        Column(Modifier.padding(start = 10.dp)) {
            Text(text = title, color = Color.White, fontSize = 15.sp)
            Row {
                Text(text = type, color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = owner, color = Color.LightGray, fontSize = 12.sp)

            }
        }
    }

}


@Composable
fun LibraryGrid(
    title: Pair<String, String>,
    type: Pair<String, String>,
    owner: Pair<String, String>,
    imageUrl: Pair<String, String>,
    count: Int = 2
) {

    Row(
        Modifier
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.width(10.dp))
        Column(
            Modifier
                .fillMaxWidth(0.5f)

                .weight(1f)
        ) {

            SingleGrid(title.first, type.first, owner.first, imageUrl.first)
        }
        if (count == 1) return
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            Modifier
                .fillMaxWidth(0.5f)

                .weight(1f)
        ) {

            SingleGrid(title.second, type.second, owner.second, imageUrl.second)
        }

    }

}


@Composable
fun SingleGrid(title: String, type: String, owner: String, imageUrl: String) {
    Image(
        painter = if (imageUrl.isNotEmpty()) rememberGlidePainter(
            request = imageUrl
        ) else {
            painterResource(id = R.drawable.ic_spotify_no_name)
        },
        contentDescription = "",
        modifier = Modifier

            .padding(top = 10.dp, end = 10.dp, bottom = 10.dp)
            .fillMaxWidth()
            .height(200.dp)
    )
    Text(text = title, style = MaterialTheme.typography.body1, color = Color.White)
    Row {
        Text(
            text = "$type  $owner",
            color = Color.LightGray,
            fontSize = 10.sp
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}


fun launchHelper(
    coroutineScope: CoroutineScope,
    catchBlock: (Exception) -> Unit,
    finallyBlock: () -> Unit,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return coroutineScope.launch(context, start) {
        withContext(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                catchBlock(e)
            } finally {
                finallyBlock()
            }
        }
    }
}