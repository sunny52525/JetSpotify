package com.shaun.spotonmusic.database.model

import kaaes.spotify.webapi.android.models.Image

data class LibraryModel(

    var items: ArrayList<LibraryItem>
)


data class LibraryItem(
    var title: String,
    var typeID: String,
    var type: String,
    var owner: String,
    var id: String,
    var imageUrl: MutableList<Image>,
    var recentlyPlayedTime: String,
    var addedAt: String,
    val creator: String
)

 class TYPE {
   companion object{
       const val PLAYLIST="playlist"
       const val ARTIST="artist"
       const val ALBUM="album"
   }
}