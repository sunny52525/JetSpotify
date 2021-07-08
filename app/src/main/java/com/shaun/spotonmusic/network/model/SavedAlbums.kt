package com.shaun.spotonmusic.network.model

import kaaes.spotify.webapi.android.models.SavedAlbum

data class SavedAlbums(
    val items: List<SavedAlbum>,
)
