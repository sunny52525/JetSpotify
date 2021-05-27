package com.shaun.spotonmusic.network.model

import com.google.gson.annotations.SerializedName
import kaaes.spotify.webapi.android.models.Artist
import kaaes.spotify.webapi.android.models.Image

data class RecentlyPlayed(

    @SerializedName("items")
    val items: ArrayList<RecentlyPlayedItem> = arrayListOf()

)


data class RecentlyPlayedItem(
    val track: RecentTrack
)

data class RecentTrack(
    val album: SpotifyAlbum,
    val artist: Artist,
    val href: String,
    val id: String,
    val name: String,
    val trackNumber: Int,
    val type: String

)

data class SpotifyAlbum(
    val album_type: String,
    val artist: Artist,
    val href: String,
    val id: String,
    val images: ArrayList<Image>,
    val name:String

)