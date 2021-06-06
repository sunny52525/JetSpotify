package com.shaun.spotonmusic.database.model

import kaaes.spotify.webapi.android.models.Image

data class SpotOnMusicModel(
    var imageUrls: List<String>,
    var type: String,
    var title: String,
    val id: String
)