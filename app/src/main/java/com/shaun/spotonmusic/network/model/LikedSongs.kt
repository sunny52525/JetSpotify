package com.shaun.spotonmusic.network.model

import kaaes.spotify.webapi.android.models.Track

data class LikedSongsTrack(
    val track: Track,
    val added_at: String,
)

data class LikedSongs(
    val items: ArrayList<LikedSongsTrack>,
)

