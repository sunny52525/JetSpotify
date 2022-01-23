package com.shaun.spotonmusic.presentation.ui.components.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.ui.theme.searchBarColor
import com.shaun.spotonmusic.ui.theme.searchQueryGray
import com.shaun.spotonmusic.ui.theme.spotifyGray
import kotlinx.coroutines.delay

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview
@Composable
fun MainSearchBar(query: String = "", onQuery: (String) -> Unit = { }) {

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val expanded = remember {
        mutableStateOf(false)
    }

    val height by derivedStateOf {
        if (expanded.value)
            1f
        else
            0.8f
    }
    val width by derivedStateOf {
        if (expanded.value)
            1f
        else
            0.95f
    }


    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(spotifyGray), contentAlignment = Alignment.Center
    ) {


        Card(
            modifier = Modifier
                .fillMaxWidth(width)
                .fillMaxHeight(height)
                .animateContentSize(),
            backgroundColor = searchBarColor,
            onClick = {
                expanded.value = !expanded.value

            }

        ) {

            if (!expanded.value) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "Search",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)

                    )
                }
            }

        }

        AnimatedVisibility(visible = expanded.value, enter = fadeIn()) {
            TextField(
                value = query,
                onValueChange = { onQuery(it) },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,

                    ),
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1f)
                    .background(searchBarColor)
                    .animateContentSize()
                    .clickable {
                        expanded.value = !expanded.value
                    }
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        text = "Search query",
                        color = searchQueryGray,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize(),
                        fontSize = 17.sp,

                        )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                    autoCorrect = true,
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                    }
                ),
                leadingIcon = {

                    Image(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxHeight()
                            .size(24.dp),
                        colorFilter = ColorFilter.tint(searchQueryGray)
                    )

                },

                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),


                )
        }
    }

    LaunchedEffect(key1 = true, block = {
        delay(150)
        expanded.value = true
    })

}