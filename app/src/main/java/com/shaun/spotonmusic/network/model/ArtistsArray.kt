package com.shaun.spotonmusic.network.model

import kaaes.spotify.webapi.android.models.Artist

data class ArtistsArray(
    val artists: ArtistItems= ArtistItems(listOf()),
)
data class ArtistItems(
    val items: List<Artist>,
)