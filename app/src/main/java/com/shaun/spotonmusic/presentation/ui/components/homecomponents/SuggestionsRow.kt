package com.shaun.spotonmusic.presentation.ui.components.homecomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.utils.getImageUrl

private const val TAG = "SuggestionsRow"


@Composable
fun SuggestionsRow(
    title: String, data: List<SpotOnMusicModel>,
    size: Int = 170,
    onCardClicked: (String) -> Unit
) {


    Column(Modifier.padding(top = 30.dp)) {


        Text(
            text = title, textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            fontSize = 23.sp,
            color = Color.White

        )
        LazyRow() {


            data.let {

                itemsIndexed(it) { index, item ->

                    SuggestionCard(
                        0,
                        imageUrl = getImageUrl(item.imageUrls, 1),
                        item.title,
                        paddingValues = if (index == 0) 20 else 10,
                        size = size,
                        modifier = Modifier
                            .clickable {
                                onCardClicked(item.id)
                            }


                    )
                }


            }

        }
    }
}


//@Preview
@Composable
fun SuggestionCard(
    cornerRadius: Int = 0,
    imageUrl: String,
    title: String,
    size: Int,
    paddingValues: Int,
    modifier: Modifier,

    ) {


    Column(
        modifier
            .padding(bottom = 10.dp, top = 10.dp, start = paddingValues.dp)
            .width(size.dp)


    ) {
        Box(

            modifier = modifier
                .size((size).dp), contentAlignment = Alignment.Center
        ) {


            Image(
                painter = rememberGlidePainter(request = imageUrl),
                contentDescription = "",


                )
        }
        Text(
            text = title,
            color = Color.Gray,
            textAlign = TextAlign.Left,
            fontSize = 13.sp,
            modifier = Modifier.width(size.dp)
        )

    }


}
