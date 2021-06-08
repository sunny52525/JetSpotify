package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.network.api.RetrofitEnqueue.Companion.Result
import com.shaun.spotonmusic.network.api.RetrofitEnqueue.Companion.enqueue
import com.shaun.spotonmusic.network.api.SpotifyAppService
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.Playlist
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import retrofit2.Call
import javax.inject.Inject

class MusicDetailRepositoryImpl @Inject constructor(
    private val accessToken: String,
    private val retrofit: SpotifyAppService
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

    fun getPlaylistAsync(playlistId: String): MutableLiveData<Playlist?> {

        val result = MutableLiveData<Playlist?>()

        retrofit.getAPlayList(playList_id = playlistId, "IN", "Authorization: Bearer $accessToken")
            .enqueue {
                when (it) {
                    is Result.Success -> {
                        Log.d(TAG, "getPlaylistAsync: ${it.response.body()}")
                        result.postValue(it.response.body())
                    }
                    is Result.Failure -> {
                        Log.d(TAG, "getRecentlyPlayed: ${it.error}")
                    }
                }
            }

        return result

    }


    fun hasLikedThisSong(id: String): MutableLiveData<Boolean> {

        val result = MutableLiveData<Boolean>()
        spotify.containsMySavedTracks(id, object : Callback<BooleanArray> {
            override fun success(t: BooleanArray?, response: Response?) {


                result.postValue(t?.get(0))
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: $error")
            }

        })
        return result
    }


    fun followsPlayList(playList: String, userId: String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>(false)

        val call = retrofit.followsPlaylist(
            playList,
            userId = userId,
            "Authorization: Bearer $accessToken"
        )

        call.enqueue {
            when (it) {
                is Result.Success -> {
                    Log.d(TAG, "followsPlayList: ${it.response.body()?.get(0)}")
                    result.postValue(it.response.body()?.get(0))
                }
                is Result.Failure -> {

                    Log.d(TAG, "followsPlayList: ${it.error}")
                }
            }
        }

        return result
    }


    fun followPlaylist(playlistId: String) {
        retrofit.followAPlaylist(playList_id = playlistId, "Authorization: Bearer $accessToken")
            .enqueue(object : retrofit2.Callback<Void> {
                override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {

                    Log.d(TAG, "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }

            })
    }

    fun unfollowAPlaylist(playlistId: String) {

        retrofit.unfollowAPlaylist(playList_id = playlistId, "Authorization: Bearer $accessToken")
            .enqueue(object : retrofit2.Callback<Void> {
                override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {

                    Log.d(TAG, "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }

            })

    }

    companion object {
        private const val TAG = "AlbumDetailRepos"
    }

}