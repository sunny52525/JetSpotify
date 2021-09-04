package com.shaun.spotonmusic.presentation.ui.components.playlist

import android.util.Log
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.green
import kaaes.spotify.webapi.android.models.Playlist
import java.util.*

@Composable
fun BoxTopSection(
    album: String, listState: LazyListState, surfaceGradient: ArrayList<Color>? = arrayListOf(
        black, black
    ), imageUrl: String, currentAlbum: Playlist,
    isFollowing: Boolean
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .background(
                Brush.verticalGradient(
                    surfaceGradient!!.toList(),
                    endY = 500f
                )
            )
            .fillMaxWidth()
            .padding(top = 100.dp)

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
            animateDpAsState(((230 - dynamicValue / 10).coerceAtLeast(100)).dp).value
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

        IsFollowing(isFollowing)

        Text(
            text = "BY ${currentAlbum.owner.display_name.uppercase(Locale.ROOT)} • ${
                getFollowerCount(
                    currentAlbum.followers.total
                )
            }",
            modifier = Modifier.padding(4.dp),
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.height(65.dp))
    }
}


@Composable
fun IsFollowing(sFollowing: Boolean) {


    val title = if (sFollowing) "FOLLOWING" else "FOLLOW"
    val borderColor = if (sFollowing) green else Color.Gray


    Text(
        text = title,
        color = MaterialTheme.colors.onSurface,
        style = typography.h6.copy(fontSize = 10.sp),
        modifier = Modifier
            .padding(2.dp)
            .border(
                border = BorderStroke(2.dp, borderColor),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 6.dp, horizontal = 24.dp)
            .animateContentSize()

    )


}

fun getFollowerCount(count: Int): String {
    if (count == 0)
        return ""
    return "$count FOLLOWERS"
}