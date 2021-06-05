package com.shaun.spotonmusic.database.model

import kaaes.spotify.webapi.android.models.Image

data class SuggestionModel(
    var imageUrls: List<Image>,
    var type: String,
    var title: String,
    val id: String
)