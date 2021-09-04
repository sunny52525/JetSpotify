package com.shaun.spotonmusic.presentation.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.presentation.ui.Heading
import com.shaun.spotonmusic.utils.getImageUrl


@Composable
fun SuggestionsRow(
    title: String, data: List<SpotOnMusicModel>?,
    size: Int = 170,
    cornerRadius: Int = 0,
    onCardClicked: (String) -> Unit,
) {


    if(data.isNullOrEmpty()){
        return
    }

    Column(Modifier.padding(top = 30.dp)) {


        Heading(title = title)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {


            itemsIndexed(data) { _, item ->

                SuggestionCard(
                    cornerRadius = cornerRadius,
                    imageUrl = getImageUrl(item.imageUrls, 1),
                    item.title,

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


//@Preview
@Composable
fun SuggestionCard(
    cornerRadius: Int = 0,
    imageUrl: String,
    title: String,
    size: Int,

    modifier: Modifier,

    ) {


    Column(
        modifier
            .padding(bottom = 10.dp, top = 10.dp)
            .width(size.dp)


    ) {
        Card(

            modifier = modifier
                .size((size).dp),
            shape = RoundedCornerShape(percent = cornerRadius)
        ) {


            Image(
                painter = rememberGlidePainter(request = imageUrl),
                contentDescription = "",


                )
        }
        Text(
            text = title,
            color = Color.Gray,
            textAlign = if (cornerRadius == 0) TextAlign.Left else TextAlign.Center,
            fontSize = 13.sp,
            modifier = Modifier.width(size.dp)
        )

    }


}
