package com.shaun.spotonmusic.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotify.android.appremote.api.SpotifyAppRemote

class MusicPlayerViewModel : ViewModel() {
    var spotifyRemote = MutableLiveData<SpotifyAppRemote>()


    val trackName = MutableLiveData<String>()
    val singerName = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()
    val isPlaying = MutableLiveData(false)
    val seekState = MutableLiveData(0.0f)


    fun setSpotifyRemote(spotifyAppRemote: SpotifyAppRemote?) {
        this.spotifyRemote.postValue(spotifyAppRemote)
    }

    fun setPlayerDetails(songName: String, singerName: String, imageUrl: String) {

         this.trackName.postValue(songName)
        this.singerName.postValue(singerName)
        this.imageUrl.postValue("https://i.scdn.co/image/${imageUrl.split(':')[2]}")
    }


    fun updateSeekState(newPosition:Float){
        seekState.postValue(newPosition)
    }
    companion object {
        private const val TAG = "MusicPlayerViewModel"
    }
}