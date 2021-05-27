package com.shaun.spotonmusic.presentation.ui.components.homeComponents

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.network.model.RecentlyPlayed

@Composable
fun RecentlyPlayedRow(title: String?, recentlyPlayed: RecentlyPlayed?) {


    Column(Modifier.padding(top = 30.dp)) {


        Text(
            text = title.toString(), textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            fontSize = 23.sp,
            color = Color.White
        )
        LazyRow {


            recentlyPlayed?.items.let {
                it?.forEachIndexed { index, item ->
                    try {
                        item {
                            SuggestionCard(
                                0,
                                imageUrl = item.track.album.images[0].url,
                                item.track.name,
                                paddingValues = if (index == 0) 20 else 10
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("TAG", "RecentlyPlayedRow: No Item")
                    }
                }
            }
        }

    }
}
