package com.shaun.spotonmusic.presentation.ui.components.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.ui.theme.green
import com.shaun.spotonmusic.ui.theme.greenDark
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.utils.AppConstants

@ExperimentalAnimationApi
@Composable
fun LibraryChips(
    chipSelected: (String, Boolean) -> Unit,
    chipItemSelected: String?
) {


    LazyRow(
        Modifier
            .background(spotifyDarkBlack)
            .fillMaxWidth()
    ) {
        item { Spacer(modifier = Modifier.width(20.dp)) }
        items(AppConstants.LIBRARYCHIPS) { item ->
            Chip(
                item,
                onValueChanged = {

                    chipSelected(item, it)
                },
                isSelected = item == chipItemSelected,
                isVisible = chipItemSelected == "" || (item == chipItemSelected)
            )
        }
    }

}


@ExperimentalAnimationApi
@Preview
@Composable
fun Chip(
    title: String = "Albums",
    isSelected: Boolean = false,
    onValueChanged: (Boolean) -> Unit = {},
    isVisible: Boolean = true
) {
    var isSelectedChip by remember {
        mutableStateOf(isSelected)
    }

    AnimatedVisibility(visible = isVisible, enter = slideInHorizontally()) {

        Surface(
            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp),

            border = BorderStroke(
                width = 1.dp,
                color = when {
                    isSelectedChip -> green
                    else -> Color.White
                }
            )
        ) {

            Row(modifier = Modifier
                .toggleable(
                    value = isSelectedChip,
                    onValueChange = {
                        isSelectedChip = !isSelectedChip
                        onValueChanged(isSelectedChip)

                    }
                )
                .background(
                    color = when {
                        isSelectedChip -> greenDark
                        else -> spotifyDarkBlack
                    }
                )) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body2,
                    color = Color.White,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                )
            }


        }
    }
}