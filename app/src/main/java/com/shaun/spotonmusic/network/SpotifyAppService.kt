package com.shaun.spotonmusic.network

import com.shaun.spotonmusic.model.RecentlyPlayed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface SpotifyAppService {



    @GET("/v1/me/player/recently-played")
    fun getRecentlyPlayed(@Query("q") limit: Int, @Header("Authorization") authorization: String)
            : Call<RecentlyPlayed>
}