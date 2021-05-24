package com.shaun.spotonmusic.presentation.ui.components.homeComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kaaes.spotify.webapi.android.models.Artist
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.Recommendations

@Composable
fun RecommendationsRow(
    title: String,
    recommendations: Recommendations?,
    image: String,
    artistName: Pager<Artist>,
    index: Int
) {

    if(recommendations?.tracks==null)
        return
    Column(Modifier.padding(top = 30.dp)) {


        CustomizedHeading(image, title, artistName.items?.get(index = index)?.name)

        LazyRow {
            recommendations?.tracks?.let {
                it.forEachIndexed { index, its ->
                    item {
                        CustomizedSuggestionCard(
                            album = Pair(its.album.images[0].url, its.album.name),
                            paddingValues = if (index == 0) 20 else 10
                        )
                    }
                }
            }

        }


    }
}