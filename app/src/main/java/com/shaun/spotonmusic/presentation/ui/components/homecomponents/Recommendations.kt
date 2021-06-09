package com.shaun.spotonmusic.presentation.ui.components.homecomponents

import androidx.compose.foundation.layout.Column
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

        LazyRow {
            recommendations?.let {
                it.forEachIndexed { index, its ->
                    item {
                        CustomizedSuggestionCard(
                            album = Pair(its.imageUrls[0], its.title),
                            paddingValues = if (index == 0) 20 else 10, onCardClick = {

                                onCardClicked(its.id)

                            })
                    }
                }
            }

        }


    }
}