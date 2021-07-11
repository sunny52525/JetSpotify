package com.shaun.spotonmusic.network.model

import kaaes.spotify.webapi.android.models.PlaylistSimple

data class Playlists(
    val items: List<PlaylistSimple>,
)

