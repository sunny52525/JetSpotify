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
import kaaes.spotify.webapi.android.models.Playlist
import javax.inject.Inject


@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    datastoreManager: DatastoreManager,
    spotifyAppService: SpotifyAppService
) : ViewModel() {
    private var accessToken = datastoreManager.accessToken

    private var repository: MusicDetailRepositoryImpl =
        MusicDetailRepositoryImpl(accessToken = accessToken.toString(), spotifyAppService)


    var id = MutableLiveData("")
    var playList = MutableLiveData(Playlist())

    var userId = MutableLiveData("")

    var follows = MutableLiveData(false)

    val likedSongs=repository.likedSongs

    fun setUserId(id: String, userId: String) {
        this.userId.postValue(userId)
        this.id.postValue(id)
        follows = repository.followsPlayList(userId = userId, playList = id)
        playList = repository.getPlaylistAsync(id)


    }

    fun getAlbum(id: String): MutableLiveData<Album> {

        if (id.isEmpty())
            return MutableLiveData(Album())
        return repository.getAlbum(id)
    }


    fun newPlaylist(playlistId: String) {
        Log.d(TAG, "newPlaylist: Called")

        id.value = playlistId

    }






    fun followAPlaylist(playlistId: String, onFollowed: () -> Unit) {
        repository.followPlaylist(playlistId = playlistId, onFollowed = {
            onFollowed()
        })
    }

    fun unFollowPlaylist(playlistId: String, onUnFollowed: () -> Unit) {

        return repository.unfollowAPlaylist(playlistId, onUnFollowed = {
            onUnFollowed()
        })
    }

    companion object {
        private const val TAG = "MusicDetail"
    }
}