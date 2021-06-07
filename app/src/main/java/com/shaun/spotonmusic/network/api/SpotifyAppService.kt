package com.shaun.spotonmusic.network.api

import com.shaun.spotonmusic.network.model.RecentlyPlayed
import kaaes.spotify.webapi.android.models.Playlist
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface SpotifyAppService {


    @GET("/v1/me/player/recently-played")
    fun getRecentlyPlayed(@Query("q") limit: Int, @Header("Authorization") authorization: String)
            : Call<RecentlyPlayed>

    @GET("/v1/playlists/{playlist_id}")
    fun getAPlayList(
        @Path("playlist_id") playList_id: String,
        @Query("market") market: String = "IN",
        @Header("Authorization") authorization: String
    )
            : Call<Playlist>

    @GET("v1/playlists/{playlist_id}/followers/contains")
    fun followsPlaylist(
        @Path("playlist_id") playList_id: String,
        @Query("ids") userId: String,
        @Header("Authorization") authorization: String
    ): Call<BooleanArray>


//    @GET("/v1/browse/categories/{category_id}/playlists")
//    fun getCategoryPlaylist(
//        @Path("category_id ") category_id:String,
//
//    )

}