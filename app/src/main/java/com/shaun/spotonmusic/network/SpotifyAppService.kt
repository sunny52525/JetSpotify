package com.shaun.spotonmusic.network

import android.database.Observable
import com.shaun.spotonmusic.model.RecentlyPlayed
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


}