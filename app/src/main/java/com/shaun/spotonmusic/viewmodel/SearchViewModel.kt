package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.repository.SearchRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val retrofit: SpotifyAppService,
    datastoreManager: DatastoreManager,
) : ViewModel() {

    private var accessToken = datastoreManager.accessToken
    private val repository = SearchRepositoryImpl(accessToken.toString(), retrofit)


    private val query = MutableLiveData("")

    val artists = Transformations.switchMap(query) {
        repository.searchArtists(it)
    }

    val albums = Transformations.switchMap(query) {
        repository.searchAlbums(it)
    }
    val playlists = Transformations.switchMap(query) {
        repository.searchPlaylists(it)
    }
    val tracks = Transformations.switchMap(query) {
        repository.searchTracks(it)
    }

    fun searchQuery(query: String) {
        this.query.postValue(query)
    }

}