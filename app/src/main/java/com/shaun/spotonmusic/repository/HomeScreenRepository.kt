package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import io.github.kaaes.spotify.webapi.retrofit.v2.SpotifyService
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.PlaylistsPager
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response


class HomeScreenRepository(private val mSpotifyApi: SpotifyService, val accessToken: String) {
    private var api = SpotifyApi()
    private var spotify: kaaes.spotify.webapi.android.SpotifyService
     var tokenExpired = MutableLiveData<Boolean>()

    var album = MutableLiveData<Album>()
    var categoryPlaylistPager = MutableLiveData<PlaylistsPager>()

    init {

        api.setAccessToken(accessToken);
        spotify = api.service

    }

    fun getAlbum(albumID: String) {

        Log.d(TAG, "getRecommendations: $accessToken")

        spotify.getAlbum(albumID, object : Callback<Album> {
            override fun success(t: Album?, response: retrofit.client.Response?) {
                Log.d(TAG, "onResponse: ${response?.body}")
                Log.d(TAG, "success: ${t!!.artists[0].name}")

                album.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "onFailure: ${error?.message}")
                tokenExpired.postValue(true)

            }

        })

    }


    fun getCategoryPlaylist(category: String) {
        val map = mapOf("country" to "IN", "limit" to 10)
        spotify.getPlaylistsForCategory(category, map, object : Callback<PlaylistsPager> {
            override fun success(t: PlaylistsPager?, response: Response?) {

                Log.d(TAG, "success: ${t!!.playlists.items[0].name}")
                categoryPlaylistPager.postValue(t)
            }

            override fun failure(error: RetrofitError?) {

                Log.d(TAG, "onFailure: ${error?.message}")
            }

        })
    }

    companion object {
        private const val TAG = "HomeScreenRepository"
    }


}