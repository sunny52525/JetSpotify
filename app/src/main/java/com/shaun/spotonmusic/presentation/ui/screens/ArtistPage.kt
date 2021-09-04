package com.shaun.spotonmusic.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.network.model.LikedSongsTrack
import com.shaun.spotonmusic.presentation.ui.components.LikedSongCount
import com.shaun.spotonmusic.presentation.ui.components.Progress
import com.shaun.spotonmusic.presentation.ui.components.SongListItemWithNumber
import com.shaun.spotonmusic.presentation.ui.components.TopSpace
import com.shaun.spotonmusic.presentation.ui.components.album.TopBar
import com.shaun.spotonmusic.presentation.ui.components.home.SuggestionsRow
import com.shaun.spotonmusic.presentation.ui.components.playlist.SpotifySongListItem
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack
import com.shaun.spotonmusic.utils.getArtistName
import com.shaun.spotonmusic.utils.getImageUrl
import com.shaun.spotonmusic.utils.toListString
import com.shaun.spotonmusic.viewmodel.ArtistDetailViewModel

@ExperimentalMaterialApi
@Composable
fun ArtistPage(

    artistDetailViewModel: ArtistDetailViewModel,
    onAlbumClicked: (String) -> Unit,
    onSongClicked: (String) -> Unit,
    items: ArrayList<LikedSongsTrack>?,

    updatePlaylist: () -> Unit,
    onArtistClicked: (String) -> Unit,
) {
    val artist by artistDetailViewModel.artist.observeAsState()
    var likeCount by remember {
        mutableStateOf(0)
    }

    val follows by artistDetailViewModel.followed.observeAsState()

    SideEffect {

        likeCount = items?.count {
            it.track.artists[0].id == artist?.id
        }?:0
    }

    val topArtistTracks by artistDetailViewModel.artistTopTracks.observeAsState()

    val relatedArtist by artistDetailViewModel.relatedArtist.observeAsState()
    val appearsOn by artistDetailViewModel.getAppearsOn.observeAsState()

    val topAlbums by artistDetailViewModel.topAlbums.observeAsState()

    if (artist?.id.isNullOrBlank()) {

        Progress()
    } else {
        artist?.let {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(spotifyDarkBlack)
            ) {
                TopBar(title = it.name)

                LazyColumn {
                    item {
                        TopSpace(
                            artistName = it.name,
                            followers = it.followers.total,
                            isFollowing = follows?.get(0) ?: false,
                            imageUrl = getImageUrl(it.images.map { it.url }, 2)
                        ) {

                            if (follows?.get(0) == false)
                                artistDetailViewModel.followArtist(it.id, onFollowed = {
                                    updatePlaylist()
                                })
                            else
                                artistDetailViewModel.unFollowArtist(it.id, onUnFollowed = {
                                    updatePlaylist()
                                })
                        }
                    }
                    likeCount.let {
                        if (it > 0)
                            item {
                                LikedSongCount(
                                    imageUrl = getImageUrl(artist?.images?.toListString(), 0),
                                    likeCount = it,
                                    artistName = artist?.name?:""
                                )
                            }
                    }

                    topArtistTracks?.tracks?.forEachIndexed { index, track ->
                        track?.let { oneTrack ->
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                SongListItemWithNumber(
                                    number = index + 1,
                                    title = oneTrack.name,
                                    author = getArtistName(oneTrack.artists),
                                    isExplicit = oneTrack.explicit,
                                    image = getImageUrl(oneTrack.album.images.toListString(), 0)
                                ) {
                                    onSongClicked(oneTrack.uri)
                                }
                            }
                        }
                    }


                    item {
                        Text(
                            text = "Popular Releases",
                            color = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(20.dp),
                            fontWeight = FontWeight.Bold
                        )
                        topAlbums?.items?.subList(0, 5)?.forEach {

                            SpotifySongListItem(
                                imageUrl = getImageUrl(it.images.toListString(), 0),
                                album = it.name,
                                imageSize = 80,
                                singer = it.release_date,
                                explicit = false,
                                showMore = false,
                                onSongClicked = {
                                    onAlbumClicked(it.id)
                                }
                            )

                        }
                    }
                    item {

                        relatedArtist?.let { it1 ->
                            SuggestionsRow(
                                title = "Fans also like",
                                data = it1,
                                cornerRadius = 50
                            ) {
                                onArtistClicked(it)

                            }
                        }

                    }
                    item {

                        appearsOn?.let { it1 ->
                            SuggestionsRow(
                                title = "Appears On",
                                data = it1
                            ) {
                                onAlbumClicked(it)

                            }
                        }

                    }
                }


            }
        }
    }
}