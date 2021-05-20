package com.shaun.spotonmusic.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.read
import com.shaun.spotonmusic.repository.HomeScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kaaes.spotify.webapi.retrofit.v2.Spotify
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.CategoriesPager
import kaaes.spotify.webapi.android.models.PlaylistsPager
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(context: SpotOnApplication, private val dataStore: DataStore<Preferences>) : ViewModel() {


    private lateinit var spotifyService: io.github.kaaes.spotify.webapi.retrofit.v2.SpotifyService
    var accessToken = mutableStateOf("")

//    private var dataStore: DataStore<Preferences> = context.createDataStore(name = "accesstoken")
    private lateinit var repo: HomeScreenRepository
    var albums = MutableLiveData<Album>()

    //    var tokenExpired = MutableLiveData<Boolean>()
    var categoryPlaylistsPager = MutableLiveData<PlaylistsPager>()

    init {
        getAccessToken()

    }

    private fun setToken() {
        spotifyService = Spotify.createAuthenticatedService(accessToken.toString())
        repo = HomeScreenRepository(spotifyService, accessToken.value)
        albums = repo.album
        categoryPlaylistsPager=repo.categoryPlaylistPager

//        tokenExpired = repo.tokenExpired

    }

    private fun getAccessToken() {

        viewModelScope.launch {
            accessToken.value = read(dataStore, "accesstoken")
            setToken()


        }
    }

    fun getAlbum(albumId: String) {
        repo.getAlbum(albumID = albumId)
    }

    fun categoryPlaylist(category: String) {
        repo.getCategoryPlaylist(category = category)
    }


}