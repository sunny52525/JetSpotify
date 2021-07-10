package com.shaun.spotonmusic.presentation.ui.components.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.presentation.ui.components.playlist.SpotifySongListItem
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.utils.dateToString
import com.shaun.spotonmusic.utils.getArtistName
import com.shaun.spotonmusic.utils.getImageUrl
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.TrackSimple
import java.util.*

@ExperimentalMaterialApi
@Composable
fun AlbumSongList(
    currentAlbum: Album?,
    colors: ArrayList<Color>?,
    onShuffleClicked: () -> Unit,
    onSongClicked: (String) -> Unit
) {


    currentAlbum?.let {
        LazyColumn {
            item {

                AlbumTopSection(
                    image = getImageUrl(currentAlbum.images.map { it.url }, 1),
                    title = currentAlbum.name,
                    owner = getArtistName(currentAlbum.artists),
                    year = currentAlbum.release_date.substring(0, 3),
                    colorShaded = colors,
                    shuffleClicked = {
                        onShuffleClicked()
                    }
                )

            }

            items(currentAlbum.tracks.items) {
                SpotifySongListItem(
                    album = it.name,
                    explicit = it.explicit,
                    singer = getArtistName(it.artists),
                    showImage = false,
                    onSongClicked = {
                        onSongClicked(it.uri)
                    }
                )

                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .background(spotifyDarkBlack)
                        .fillMaxWidth()
                )
            }

            item {
                DurationRow(
                    songCount = currentAlbum.tracks.total,
                    dateString = dateToString(currentAlbum.release_date),
                    durationInMs = getDurationCount(currentAlbum.tracks.items)
                )
                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                        .fillMaxWidth()
                        .background(spotifyDarkBlack)
                )
                ArtistRow(
                    image = getImageUrl(currentAlbum.images.map { it.url }, 2),
                    singer = currentAlbum.artists[0].name
                )

                CopyWrites(title = currentAlbum.copyrights[0].text)
            }


        }


    }

}

fun getDurationCount(items: List<TrackSimple>): Long {

    var count: Long = 0
    items.forEach {
        count += it.duration_ms
    }
    return count
}

