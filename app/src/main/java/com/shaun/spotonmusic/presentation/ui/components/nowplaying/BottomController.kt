package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.green
import com.shaun.spotonmusic.ui.theme.spotifyGray
import com.spotify.android.appremote.api.SpotifyAppRemote


@Composable
fun BottomTrackController(
    trackName: String,
    seekState: Float,
    imageUrl: String,
    nowPlayingClicked: () -> Unit,
    artistName: String,
    isPlaying: Boolean,
    spotifyAppRemote: SpotifyAppRemote?,
    onChangePlayerClicked: () -> Unit,
    hasLiked: Boolean,
    onLikeClicked:()->Unit
) {

    //Player
    if (trackName.isNotEmpty()) {
        Column(
            Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(spotifyGray)
        ) {

            Row(
                Modifier
                    .fillMaxHeight()
                    .background(Color.White)
                    .fillMaxWidth(seekState)
                    .animateContentSize()
            ) {

            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(spotifyGray)
        ) {

            Image(
                painter = rememberGlidePainter(request = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(60.dp),
            )

            Column(
                Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        nowPlayingClicked()
                    }
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = trackName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1
                )
                Text(
                    text = artistName,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                )
            }
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .width(150.dp)
                        .padding(end = 10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cast),
                        contentDescription = "Cast",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                onChangePlayerClicked()
                            },
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_like),
                        contentDescription = "Like",
                        modifier = Modifier.size(32.dp).clickable {
                                   onLikeClicked()
                        },
                        colorFilter = if (hasLiked) ColorFilter.tint(green) else ColorFilter.tint(
                            Color.Gray)
                    )
                    Image(
                        painter = if (isPlaying)
                            painterResource(id = R.drawable.ic_pause)
                        else painterResource(
                            id = R.drawable.ic_play
                        ),
                        contentDescription = "Like",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                if (isPlaying)
                                    spotifyAppRemote?.playerApi?.pause()
                                else
                                    spotifyAppRemote?.playerApi?.resume()

                            },
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                }

            }
        }
        Spacer(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(black)
        )

    }
}
