package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.network.model.Devices
import com.shaun.spotonmusic.repository.PlaybackChangerRepository
import com.spotify.android.appremote.api.SpotifyAppRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    datastoreManager: DatastoreManager,
    spotifyAppService: SpotifyAppService
) : ViewModel() {
    private var accessToken = datastoreManager.accessToken


    private var repository: PlaybackChangerRepository =
        PlaybackChangerRepository(accessToken = accessToken.toString(), spotifyAppService)


    var spotifyRemote = MutableLiveData<SpotifyAppRemote>()


    val trackName = MutableLiveData<String>()
    val singerName = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()
    val isPlaying = MutableLiveData(false)
    val seekState = MutableLiveData(0.0f)

    val repeatMode = MutableLiveData(0)
    val isCollapsed = MutableLiveData(true)


    val volume = MutableLiveData(0f)
    private var _devices = MutableLiveData<Devices>()

    var trackDuration = MutableLiveData<Long>(0)
    val devices get() = _devices

    init {
        _devices = repository.devices
    }


    fun updateDevices() {
        repository.getPlayers()
    }

    fun changePlayer(id: String) {
        repository.changePlayer(id)
    }

    fun setSpotifyRemote(spotifyAppRemote: SpotifyAppRemote?) {
        this.spotifyRemote.postValue(spotifyAppRemote)
    }

    fun setPlayerDetails(songName: String, singerName: String, imageUrl: String) {

        this.trackName.postValue(songName)
        this.singerName.postValue(singerName)
        this.imageUrl.postValue("https://i.scdn.co/image/${imageUrl.split(':')[2]}")
    }


    fun updateSeekState(newPosition: Float) {
        seekState.postValue(newPosition)
    }

    fun seekTo(newPosition: Float) {

        val position = (newPosition * (trackDuration.value ?: 0)).toLong()

        spotifyRemote.value?.playerApi?.seekTo(position)
    }

    fun updateRepeatMode(repeatMode: Int) {
        this.repeatMode.postValue(repeatMode)
    }


}