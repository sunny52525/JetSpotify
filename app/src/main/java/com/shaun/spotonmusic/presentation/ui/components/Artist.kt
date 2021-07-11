package com.shaun.spotonmusic.presentation.ui.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.presentation.ui.components.playlist.SpotifySongListItem
import com.shaun.spotonmusic.ui.theme.green
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.utils.AppConstants


@ExperimentalMaterialApi
@Composable
fun SongListItemWithNumber(
    number: Int = 1,
    title: String,
    author: String,
    isExplicit: Boolean,
    image: String,
    onSongClicked:()->Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(spotifyDarkBlack)
            .clickable {
                Log.d("TAG", "SongListItemWithNumber: $author")
                onSongClicked()
            }
    ) {
        Text(
            text = "$number",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(30.dp),
            color = Color.White,

            )
        SpotifySongListItem(album = title, singer = author, explicit = isExplicit, imageUrl = image,onSongClicked = {
            Log.d("TAG", "SongListItemWithNumber: $author")
            onSongClicked()
        })

    }
}


@Composable
fun TopSpace(
    artistName: String,
    followers: Int,
    isFollowing: Boolean,
    imageUrl: String,
    onFollowClicked: () -> Unit
) {

    Box(modifier = Modifier.height(320.dp)) {
        Image(
            painter = rememberGlidePainter(request = imageUrl),
            contentDescription = "Artist",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            Modifier
                .height(320.dp)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = artistName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp
            )

            Text(
                text = "$followers Followers",
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(
                        alpha = 0.1f,
                        brush = Brush.verticalGradient(listOf(Color.Gray, Color.Gray))
                    )
            )
            Button(
                onClick = { onFollowClicked() },
                border = BorderStroke(1.dp, Color.White),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Unspecified,
                    contentColor = Color.Red
                ),
                modifier = Modifier.animateContentSize()

            ) {
                Text(
                    text = if (isFollowing) "Following" else "Follow", color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


@Composable
fun LikedSongCount(
    imageUrl: String = AppConstants.DUMMY_IMAGE,
    artistName: String = "Lana Del Ray",
    likeCount: Int = 42
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(spotifyDarkBlack)
    ) {

        Card(
            modifier = Modifier
                .size(40.dp),
            shape = CircleShape,

            ) {
            Image(
                painter = rememberGlidePainter(request = imageUrl), contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.size(20.dp),
                    backgroundColor = green
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "",
                        modifier = Modifier.size(5.dp),
                        tint = Color.White
                    )

                }
            }

        }

        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)

        ) {
            Text(
                text = artistName,
                style = MaterialTheme.typography.h6.copy(fontSize = 16.sp),
                color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {


                Text(
                    text = "$likeCount songs by $artistName",
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }
        }


    }

}
