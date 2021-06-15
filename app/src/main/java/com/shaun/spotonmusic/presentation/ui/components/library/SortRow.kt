package com.shaun.spotonmusic.presentation.ui.components.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import kotlinx.coroutines.launch

@ExperimentalMaterialApi

@Composable
fun SortRow(
    modalBottomSheetState: ModalBottomSheetState,
    onChangeViewCLicked: () -> Unit,
    isGrid: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(spotifyDarkBlack)
            .padding(top = 10.dp, bottom = 10.dp, end = 20.dp)
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Row(modifier = Modifier.clickable {
            coroutineScope.launch {
                modalBottomSheetState.show()
            }
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_sort),
                contentDescription = "",
                modifier = Modifier.size(15.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Recently Played", color = Color.White, fontSize = 14.sp)

        }
        Box(
            contentAlignment = Alignment.TopEnd, modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(
                    id =
                    if (isGrid) R.drawable.ic_list
                    else R.drawable.ic_grid
                ),
                contentDescription = "",
                modifier = Modifier
                    .size(15.dp)
                    .clickable {
                        onChangeViewCLicked()
                    }
                    ,
                colorFilter = ColorFilter.tint(Color.White)
            )

        }
    }
}