package com.shaun.spotonmusic.presentation.ui.components.albumcomponents

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.ui.theme.black

@Composable
fun BoxTopSection(
    album: String, listState: LazyListState, surfaceGradient: ArrayList<Color>? = arrayListOf(
        black, black
    ), imageUrl: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .background(
                Brush.linearGradient(
                    surfaceGradient!!.toList()
                )
            )
            .fillMaxWidth()
            .padding(top = 80.dp)
    ) {

        val dynamicValue by remember {
            derivedStateOf {
                if (listState.firstVisibleItemIndex > 1)
                    250
                else
                    listState.firstVisibleItemScrollOffset
            }
        }


        val animateImageSize =
            animateDpAsState(((200 - dynamicValue / 10).coerceAtLeast(100)).dp).value
        Image(
            painter = rememberGlidePainter(request = imageUrl),
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
