package com.shaun.spotonmusic.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.repository.MusicDetailRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.Album
import javax.inject.Inject


@HiltViewModel
class MusicDetail @Inject constructor(
    datastoreManager: DatastoreManager,
    spotifyAppService: SpotifyAppService
) : ViewModel() {
    private var accessToken = datastoreManager.accessToken

    private var repository: MusicDetailRepositoryImpl =
        MusicDetailRepositoryImpl(accessToken = accessToken.toString(), spotifyAppService)


    var id = MutableLiveData("")
    var playList = Transformations.switchMap(id) {
        repository.getPlaylistAsync(it)
    }

    var userId = MutableLiveData("")

    var follows = MutableLiveData(false)


    fun getAlbum(id: String): MutableLiveData<Album> {

        if (id.isEmpty())
            return MutableLiveData(Album())


        return repository.getAlbum(id)
    }


    fun newPlaylist(playlistId: String) {
        Log.d(TAG, "newPlaylist: Called")


        if (id.value == playlistId)
            return

        id.value = playlistId

    }


    fun hasLikedThisSong(songId: String): MutableLiveData<Boolean> {
        return repository.hasLikedThisSong(songId)
    }

    fun followsPlaylist(playlistId: String, userId: String): MutableLiveData<Boolean> {


        Log.d(TAG, "followsPlaylist: ${follows.value}")
        if (this.id.value == playlistId && userId == this.userId.value)
            return follows

        this.userId.postValue(userId)
        follows = repository.followsPlayList(playList = playlistId, userId = userId)

        return follows
    }


    fun followAPlaylist(playlistId: String) {
        repository.followPlaylist(playlistId = playlistId)
    }

    fun unFollowPlaylist(playlistId: String) {

        repository.unfollowAPlaylist(playlistId)
    }

    companion object {
        private const val TAG = "MusicDetail"
    }
}