package com.shaun.spotonmusic.database.model

data class SpotOnMusicModel(
    var imageUrls: List<String>,
    var type: String,
    var title: String,
    val id: String
)