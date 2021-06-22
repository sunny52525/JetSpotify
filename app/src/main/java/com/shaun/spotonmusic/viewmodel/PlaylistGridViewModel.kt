package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.repository.PlaylistGridRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.PlaylistsPager
import javax.inject.Inject


@HiltViewModel
class PlaylistGridViewModel @Inject constructor(
    datastoreManager: DatastoreManager,
    spotifyAppService: SpotifyAppService
) : ViewModel() {

    private var accessToken = datastoreManager.accessToken

    private val repository =
        PlaylistGridRepositoryImpl(accessToken = accessToken.toString(), spotifyAppService)


    private val categoryId = MutableLiveData("")
    var playlistList = MutableLiveData<PlaylistsPager>()


    fun setCategory(query: String) {
        categoryId.postValue(query)

        playlistList = repository.getCategoryPlaylist(query)
    }
}