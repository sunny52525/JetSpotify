package com.shaun.spotonmusic.presentation.ui.components.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun SearchHeading() {

    Column(
        Modifier
            .padding(top = 80.dp, start = 10.dp)
            .fillMaxWidth()
            .height(30.dp)
    ) {

        Text(
            text = "Search",
            color = Color.White,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
    }
}