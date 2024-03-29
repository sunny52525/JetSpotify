package com.shaun.spotonmusic.presentation.ui.components.library

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.database.model.LibraryModel
import com.shaun.spotonmusic.utils.TypeConverters.toListString
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
    isGrid: Boolean,
    sortRowCLick: () -> Unit,
    onClick: (String, String) -> Unit,
    chipSelected: (String, Boolean) -> Unit,
    chipItemSelected: String?,
    sortMode: String?

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
                LibraryChips(
                    chipSelected = { type, selected ->
                        chipSelected(type, selected)
                    },
                    chipItemSelected = chipItemSelected,

                )
            }
        }
        item {
            SortRow(
                modalBottomSheetState,
                onChangeViewCLicked = {
                    sortRowCLick()
                },
                isGrid = isGrid,
                sortMode = sortMode
            )
        }

        itemsIndexed(libraryItems.items) { _, item ->

            if (isGrid)
                Item(
                    title = item.title,
                    type = item.type,
                    owner = item.owner,
                    imageUrl = if (item.imageUrl.isEmpty()) "" else item.imageUrl[0].url,
                    onClick = {
                        onClick(item.typeID, item.id)
                    }
                )
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
                        ),
                        onFirstClick = {
                            onClick(item[i].typeID, item[i].id)
                        }, onSecondClick = {

                            onClick(item[i + 1].typeID, item[i + 1].id)
                        }

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
                        count = 1,
                        onSecondClick = {

                        }, onFirstClick = {

                            onClick(item[i].typeID, item[i].id)
                        }
                    )
                }


        }

    }
}

@Composable
fun Item(
    title: String = "Whole",
    type: String = "Playlist",
    owner: String = "Shaun",
    imageUrl: String,
    onClick: () -> Unit
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 7.dp, start = 20.dp, top = 8.dp)
            .clickable {
                onClick()

            },
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
                Text(text = "• $owner", color = Color.LightGray, fontSize = 12.sp)

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
    count: Int = 2,
    onFirstClick: () -> Unit,
    onSecondClick: () -> Unit,
    textAlign: TextAlign = TextAlign.Left
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
                .clickable {
                    onFirstClick()
                }
        ) {

            SingleGrid(title.first, type.first, owner.first, imageUrl.first, textAlign)
        }
        if (count == 1) return
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            Modifier
                .fillMaxWidth(0.5f)

                .weight(1f)
                .clickable {
                    onSecondClick()
                }
        ) {

            SingleGrid(title.second, type.second, owner.second, imageUrl.second,textAlign)
        }

    }

}


@Composable
fun SingleGrid(
    title: String,
    type: String,
    owner: String,
    imageUrl: String,
    align: TextAlign = TextAlign.Left
) {
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
    Text(
        text = title,
        style = MaterialTheme.typography.body1,
        color = Color.White,
        textAlign = align,
        modifier = Modifier.fillMaxWidth()
    )
    Row {
        Text(
            text = "$type  $owner",
            color = Color.LightGray,
            fontSize = 10.sp,
            textAlign = align,
            modifier = Modifier.fillMaxWidth()
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