package com.shaun.spotonmusic.presentation.ui.components.albumComponents

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.ui.theme.blue
import com.shaun.spotonmusic.ui.theme.green

@Composable
fun BoxTopSection(album: String, listState: LazyListState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(
            Brush.linearGradient(
                listOf(blue, green)
            )
        ).fillMaxWidth().padding(top = 80.dp)
    ) {
//        Spacer(
//            modifier = Modifier
//                .height(100.dp)
//                .fillMaxWidth()
//        )
        //animate as scroll value increase but not fast so divide by random number 50
        val dynamicValue by remember {
            derivedStateOf {
                if (listState.firstVisibleItemIndex > 1)
                    250
                else
                    listState.firstVisibleItemScrollOffset
            }
        }


        val animateImageSize = animateDpAsState(((200 - dynamicValue / 10).coerceAtLeast(100)).dp).value
        Image(
            painter = painterResource(id = R.drawable.spotify_liked),
            contentDescription = null,
            modifier = Modifier
                .size(animateImageSize)
                .padding(8.dp)
        )
        Text(
            text = album,
            style = typography.h5.copy(fontWeight = FontWeight.ExtraBold),
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colors.onSurface
        )
        Text(
            text = "FOLLOWING",
            color = MaterialTheme.colors.onSurface,
            style = typography.h6.copy(fontSize = 12.sp),
            modifier = Modifier
                .padding(4.dp)
                .border(
                    border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 4.dp, horizontal = 24.dp)
        )
        Text(
            text = album,
            style = typography.subtitle2,
            modifier = Modifier.padding(4.dp)
        )
    }
}
