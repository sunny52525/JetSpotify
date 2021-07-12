package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.network.api.SpotifyAppService
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.PlaylistsPager
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import javax.inject.Inject

class PlaylistGridRepositoryImpl @Inject constructor(
    private val accessToken: String,
    private val retrofit: SpotifyAppService
) {
    private var api = SpotifyApi()
    private var spotify: kaaes.spotify.webapi.android.SpotifyService

    init {

        api.setAccessToken(accessToken);
        spotify = api.service
    }


    fun getAlbum(albumID: String): MutableLiveData<Album?> {

        val result = MutableLiveData<Album?>()
        spotify.getAlbum(albumID, object : Callback<Album> {
            override fun success(t: Album?, response: Response?) {
                Log.d(TAG, "onResponse: ${response?.body}")
                Log.d(TAG, "success: ${t!!.artists[0].name}")

                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "onFailure: ${error?.message}")
//                tokenExpired.postValue(true)

            }

        })

        return result
    }

    fun getCategoryPlaylist(category: String): MutableLiveData<PlaylistsPager> {

        val result = MutableLiveData<PlaylistsPager>()
        spotify.getPlaylistsForCategory(
            category,
            mapOf("limit" to 50),
            object : Callback<PlaylistsPager> {
                override fun success(t: PlaylistsPager?, response: Response?) {

                    val v = t?.playlists?.items?.size

                    Log.d(TAG, "success: $response")
                    Log.d(TAG, "success: Playlist $v")
                    result.postValue(t)
                }

                override fun failure(error: RetrofitError?) {
                    Log.d(TAG, "onFailure: ${error?.message}")
                }

            })
        return result
    }

    companion object {
        private const val TAG = "PlaylistGridRepositoryI"
    }


}