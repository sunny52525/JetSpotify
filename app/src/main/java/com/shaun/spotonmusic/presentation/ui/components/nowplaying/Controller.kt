package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.R


@ExperimentalMaterialApi
@Preview
@Composable
fun Controller() {

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(painter = painterResource(id = R.drawable.ic_like), contentDescription = "")
        Image(painter = painterResource(id = R.drawable.ic_previous), contentDescription = "")
        Card(onClick = { /*TODO*/ }, backgroundColor = Color.White, shape = CircleShape,

            modifier = Modifier.padding(15.dp)
            ) {
            Image(painter = painterResource(id = R.drawable.ic_play), contentDescription = "")
        }
        Image(painter = painterResource(id = R.drawable.ic_next), contentDescription = "")
        Image(painter = painterResource(id = R.drawable.ic_negativ), contentDescription = "")
    }
}