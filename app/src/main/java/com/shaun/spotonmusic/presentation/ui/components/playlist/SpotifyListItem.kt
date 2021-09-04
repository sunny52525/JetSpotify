package com.shaun.spotonmusic.presentation.ui.components.playlist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.ui.theme.lightGreen
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack

@ExperimentalMaterialApi
@Preview
@Composable
fun SpotifySongListItem(
    album: String = "Test",
    explicit: Boolean = true,
    singer: String = "Lana Del Ray",
    imageUrl: String = "",
    showImage: Boolean = true,
    liked: Boolean = false,
    imageSize: Int = 55,
    showMore: Boolean = true,
    shape: Shape = RectangleShape,
    paddingStart:Int=8,
    onSongClicked: () -> Unit = {}
) {


    Row(
        modifier = Modifier
            .background(spotifyDarkBlack)
            .padding(start = paddingStart.dp, bottom = 5.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        Log.d("TAG", "SpotifySongListItem: $album")
                        onSongClicked()
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showImage) {
            Card(onClick = { onSongClicked() }, shape = shape) {
                Image(
                    painter = rememberGlidePainter(request = imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(imageSize.dp)
                        .padding(4.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        ) {
            Text(
                text = album,
                style = typography.h6.copy(fontSize = 16.sp),
                color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (explicit) {
                    ExplicitIcon()
                    Spacer(modifier = Modifier.width(5.dp))
                }
                Text(
                    text = singer,
                    style = typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }
        }

        if (liked)
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = lightGreen,
                modifier = Modifier
                    .padding(4.dp)
                    .size(20.dp),

                )


        if (showMore)
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.padding(4.dp)
            )
    }
}


@Composable
@Preview
fun ExplicitIcon() {


    Surface(
        Modifier
            .size(10.dp)
            .background(spotifyDarkBlack),
        shape = RoundedCornerShape(2.dp)

    ) {

        Text(
            text = "E",
            fontSize = 7.sp,
            modifier = Modifier
                .background(Color.Gray)
                .padding(start = 3.dp)
        )
    }
}