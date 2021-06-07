package com.shaun.spotonmusic.presentation.ui.components.albumcomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.glide.rememberGlidePainter
import com.shaun.spotonmusic.ui.theme.lightGreen
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.viewmodel.MusicDetail
import kaaes.spotify.webapi.android.models.ArtistSimple


@Preview(showBackground = true)
@Composable
fun SpotifySongListItem(
    album: String = "Test",
    viewModel: MusicDetail = viewModel(),
    trackId: Boolean = true,
    explicit: Boolean = true,
    singer: MutableList<ArtistSimple> = mutableListOf(),
    imageUrl: String = ""
) {


    Row(
        modifier = Modifier
            .background(spotifyDarkBlack)
            .padding(start = 8.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberGlidePainter(request = imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(55.dp)
                .padding(4.dp)
        )
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
                    text = getArtistName(singer),
                    style = typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }
        }

        if (trackId)
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = lightGreen,
                modifier = Modifier
                    .padding(4.dp)
                    .size(20.dp),

                )

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.padding(4.dp)
        )
    }
}

fun getArtistName(artist: MutableList<ArtistSimple>): String {

    val list = artist.map {
        it.name
    }


    var artistName = ""

    list.forEachIndexed { index, s ->

        artistName += s

        if (index != list.size - 1)
            artistName += ","
    }
    return artistName

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