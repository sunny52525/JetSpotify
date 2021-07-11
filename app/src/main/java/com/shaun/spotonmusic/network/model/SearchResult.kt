package com.shaun.spotonmusic.network.model

import kaaes.spotify.webapi.android.models.AlbumsPager
import kaaes.spotify.webapi.android.models.ArtistsPager
import kaaes.spotify.webapi.android.models.PlaylistsPager
import kaaes.spotify.webapi.android.models.TracksPager

data class SearchResult(
    val albums:AlbumsPager,
    val artists:ArtistsPager,
    val tracks:TracksPager,
    val playlists:PlaylistsPager,
)
