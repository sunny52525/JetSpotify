package com.shaun.spotonmusic.presentation.ui.components.routeScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.presentation.ui.components.GreetingCard
import com.shaun.spotonmusic.presentation.ui.components.RecentHeardBlock
import com.shaun.spotonmusic.presentation.ui.components.SuggestionsRow


@Preview
@Composable
fun Home() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 50.dp),
    ) {
        LazyColumn {
            item {

                GreetingCard(onSettingClicked = { /*TODO*/ }, onHistoryClicked = {/*TODO*/ })
            }
            item {

                RecentHeardBlock()
            }


            repeat(10) {

                item {
                    SuggestionsRow()
                }
            }
        }

    }
}