package com.shaun.spotonmusic.presentation.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.database.model.SpotOnMusicModel

@Composable
fun RecommendationsRow(
    title: String,
    recommendations: List<SpotOnMusicModel>,
    image: String,
    artistName: List<SpotOnMusicModel>,
    index: Int,
    onCardClicked: (String) -> Unit
) {

    if (artistName.isEmpty())
        return

    Column(Modifier.padding(top = 30.dp)) {


        CustomizedHeading(image, title, artistName[index].title)

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            recommendations.let {
                it.forEachIndexed { _, its ->
                    item {
                        CustomizedSuggestionCard(
                            album = Pair(its.imageUrls[0], its.title),
                            onCardClick = {

                                onCardClicked(its.id)

                            })
                    }
                }
            }

        }


    }
}