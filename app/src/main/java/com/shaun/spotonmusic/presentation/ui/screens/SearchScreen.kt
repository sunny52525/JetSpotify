package com.shaun.spotonmusic.presentation.ui.screens

import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.presentation.ui.components.search.MainSearchBar
import com.shaun.spotonmusic.ui.theme.black


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SearchScreen() {

    val isVisible = remember {
        mutableStateOf(false)
    }

    SideEffect {
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            isVisible.value = true
        }, 100)
    }

    Column(
        Modifier
            .padding(top = 50.dp)
            .background(black)
            .fillMaxSize()

    ) {

        AnimatedVisibility(
            visible = isVisible.value,
            enter = slideInVertically(initialOffsetY = {
                100
            }),
        ) {
            MainSearchBar()

        }


    }
}