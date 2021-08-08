package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.ui.theme.green


@ExperimentalMaterialApi

@Composable
fun Controller(
    repeatMode: Int?,
    isPaused: Boolean,
    onLiked: () -> Unit,
    onNext: () -> Unit,
    likesThisSong: Boolean,
    onHeartClick: () -> Unit,
    onPrevious: () -> Unit,
    onPause: () -> Unit
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        Image(
            painter = getIcon(mode = repeatMode), contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onLiked()
                }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_previous), contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onPrevious()
                }
        )
        Card(
            onClick = { }, backgroundColor = Color.White, shape = CircleShape,

            ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        onPause()
                    }

            ) {
                Image(
                    painter = if (!isPaused) painterResource(id = R.drawable.ic_play) else painterResource(
                        id = R.drawable.ic_pause
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onPause() }
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.ic_next), contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onNext()
                }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_like),
            colorFilter = if (likesThisSong) ColorFilter.tint(green) else ColorFilter.tint(
                Color.Gray
            ),
            contentDescription = null,
            modifier = Modifier.clickable {
                onHeartClick()
            }
        )
    }
}


@Composable
fun getIcon(mode: Int?): Painter {
    if (mode == 0)
        return painterResource(id = R.drawable.ic_repeat_off)
    if (mode == 1)
        return painterResource(id = R.drawable.ic_repeat_one)

    return painterResource(id = R.drawable.ic_repeat_on)

}