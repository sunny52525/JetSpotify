package com.shaun.spotonmusic.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.github.kaaes.spotify.webapi.core.models.RecentlyPlayedTrack
import kaaes.spotify.webapi.android.models.*

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
    val images: ArrayList<Image>

)