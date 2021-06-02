package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.Album
import retrofit.Callback
import retrofit.RetrofitError
import javax.inject.Inject

class AlbumDetailRepositoryImpl @Inject constructor(
    private val accessToken: String,
//    private val retrofit: SpotifyAppService
) {
    private var api = SpotifyApi()
    private var spotify: kaaes.spotify.webapi.android.SpotifyService

    init {

        api.setAccessToken(accessToken);
        spotify = api.service
    }


    fun getAlbum(id: String): MutableLiveData<Album> {
        val result = MutableLiveData<Album>()
        spotify.getAlbum(id, object : Callback<Album> {
            override fun success(t: Album?, response: retrofit.client.Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: getAlbum  $error")
            }

        })
        return result
    }


    companion object {
        private const val TAG = "AlbumDetailRepos"
    }

}