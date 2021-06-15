package com.shaun.spotonmusic.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.ui.theme.black

@Composable
fun Splash() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize(1f)
            .background(black)
    ) {


        Image(
            painter = painterResource(id = R.drawable.ic_spotify_logo),
            contentDescription = "Spotify Logo",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)


        )
    }


}

