package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.network.api.RetrofitEnqueue
import com.shaun.spotonmusic.network.api.RetrofitEnqueue.Companion.enqueue
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.network.model.ChangePlaybackBody
import com.shaun.spotonmusic.network.model.Devices
import kaaes.spotify.webapi.android.SpotifyApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaybackChangerRepository(accessToken: String, val spotifyAppService: SpotifyAppService) {
    private var api = SpotifyApi()
    private var spotify: kaaes.spotify.webapi.android.SpotifyService
    val devices = MutableLiveData<Devices>()

    init {

        api.setAccessToken(accessToken);
        spotify = api.service
    }

    private var header = "Authorization: Bearer $accessToken"

    fun getPlayers() {

        spotifyAppService.getDevices(header).enqueue {

            when (it) {
                is RetrofitEnqueue.Companion.Result.Success -> {
                    devices.postValue(it.response.body())

                    Log.d(TAG, "getPlayers: ${it.response.body()}")
                }
                is RetrofitEnqueue.Companion.Result.Failure -> {
                    Log.d(TAG, "getPlayers: ${it.error.message}")
                }
            }

        }
    }

    fun changePlayer(id: String) {
        Log.d(TAG, "changePlayer: ${ChangePlaybackBody(arrayListOf(id))}")
        spotifyAppService.setPlayer(body = ChangePlaybackBody(arrayListOf(id)), header)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    Log.d(TAG, "onResponse: Playback Changed")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d(TAG, "onFailure: Playback change failed")
                }

            })
    }

    companion object {
        private const val TAG = "PlaybackChangerReposito"
    }
}