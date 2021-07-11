package com.shaun.spotonmusic.network.api

import com.shaun.spotonmusic.network.model.*
import kaaes.spotify.webapi.android.models.Playlist
import retrofit2.Call
import retrofit2.http.*


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
        @Path("playlist_id") playList_id: String?,
        @Query("ids") userId: String?,
        @Header("Authorization") authorization: String
    ): Call<BooleanArray>

    @PUT("v1/playlists/{playlist_id}/followers")
    fun followAPlaylist(
        @Path("playlist_id") playList_id: String,
        @Header("Authorization") authorization: String
    ): Call<Void>

    @DELETE("v1/playlists/{playlist_id}/followers")
    fun unfollowAPlaylist(
        @Path("playlist_id") playList_id: String,
        @Header("Authorization") authorization: String
    ): Call<Void>


    @GET("v1/me/tracks/contains")
    fun hasLikedSong(
        @Query("ids") id: String,
        @Header("Authorization") authorization: String

    ): Call<BooleanArray>


    @GET("/v1/playlists/{playlist_id}")
    suspend fun getOnePlayList(
        @Path("playlist_id") playList_id: String,
        @Query("market") market: String = "IN",
        @Header("Authorization") authorization: String
    )
            : Playlist

    @GET("/v1/me/playlists")
    suspend fun getMyPlaylist(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int = 50
    ): Playlists


    @GET("/v1/me/following")
    suspend fun getFollowedArtists(
        @Query("type") type: String = "artist",
        @Query("limit") limit: Int = 50,
        @Header("Authorization") authorization: String,
    ): ArtistsArray


    @GET("v1/me/albums")
    suspend fun getSavedAlbum(
        @Query("limit") limit: Int = 50,
        @Header("Authorization") authorization: String,
    ): SavedAlbums


    @GET("v1/me/tracks")
    fun getLikedSongs(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Header("Authorization") authorization: String
    ): Call<LikedSongs>


    @PUT("v1/me/following")
    fun followArtist(
        @Query("ids") ids: String,
        @Query("type") type: String = "artist",
        @Header("Authorization") authorization: String
    ): Call<Void>

    @DELETE("v1/me/following")
    fun unFollowArtist(
        @Query("ids") ids: String,
        @Query("type") type: String = "artist",
        @Header("Authorization") authorization: String
    ): Call<Void>


    @GET("v1/me/following/contains")
    fun follows(
        @Query("type") type: String = "artist",
        @Query("ids") ids: String,
        @Header("Authorization") authorization: String
    ): Call<BooleanArray>


    @GET("v1/me/player/devices")
    fun getDevices(@Header("Authorization") authorization: String): Call<Devices>


    @PUT("v1/me/player")
    fun setPlayer(@Body body:ChangePlaybackBody, @Header("Authorization") authorization: String):Call<Void>
}