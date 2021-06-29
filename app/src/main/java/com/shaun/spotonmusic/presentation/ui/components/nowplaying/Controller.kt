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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.R


@ExperimentalMaterialApi

@Composable
fun Controller(
    isPaused: Boolean,
    onLiked: () -> Unit,
    onNext: () -> Unit,
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
            painter = painterResource(id = R.drawable.ic_like), contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_previous), contentDescription = "",
            modifier = Modifier.size(30.dp)
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
            modifier = Modifier.size(30.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_negativ), contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
    }
}