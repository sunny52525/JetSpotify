package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.repository.MusicDetailRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.Playlist
import javax.inject.Inject


@HiltViewModel
class MusicDetail @Inject constructor(
    datastoreManager: DatastoreManager,
    spotifyAppService: SpotifyAppService
) : ViewModel() {
    private var accessToken = datastoreManager.accessToken

    private var repository: MusicDetailRepositoryImpl =
        MusicDetailRepositoryImpl(accessToken = accessToken.toString(), spotifyAppService)

    var tokenExpired = MutableLiveData<Boolean>()

    var albumDetail = MutableLiveData<Album>()


    fun getAlbum(id: String): MutableLiveData<Album> {

        if (id.isEmpty())
            return MutableLiveData(Album())

        return repository.getAlbum(id)
    }

    fun getPlaylist(id: String): MutableLiveData<Playlist> {
        return repository.getPlaylistAsync(id)
    }
}