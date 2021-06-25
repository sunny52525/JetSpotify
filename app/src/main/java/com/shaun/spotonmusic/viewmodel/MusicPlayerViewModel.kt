package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotify.android.appremote.api.SpotifyAppRemote

class MusicPlayerViewModel : ViewModel() {
    var spotifyRemote = MutableLiveData<SpotifyAppRemote>()


    fun setSpotifyRemote(spotifyAppRemote: SpotifyAppRemote?) {
        this.spotifyRemote.postValue(spotifyAppRemote)
    }
}