package com.shaun.spotonmusic.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.read
import com.shaun.spotonmusic.repository.HomeScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    context: SpotOnApplication,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    var accessToken = mutableStateOf("")

    private lateinit var repo: HomeScreenRepository
    var albums = MutableLiveData<Album>()

    var moodAlbum = MutableLiveData<PlaylistsPager>()
    var partyAlbum = MutableLiveData<PlaylistsPager>()
    var featuredPlaylists = MutableLiveData<FeaturedPlaylists>()
    var favouriteArtist = MutableLiveData<Pager<Album>>()
    var favouriteArtistImage = MutableLiveData<String>()
    var tokenExpired = MutableLiveData<Boolean>()
    var newReleases = MutableLiveData<NewReleases>()
    var getMyPlayList = MutableLiveData<Pager<PlaylistSimple>>()

    init {
        getAccessToken()

    }

    private fun setToken() {
        repo = HomeScreenRepository(accessToken.value)
        albums = repo.album

        tokenExpired = repo.tokenExpired

        favouriteArtistImage = repo.favouriteArtistUrl
        moodAlbum = repo.getCategoryPlaylist("mood")
        partyAlbum = repo.getCategoryPlaylist("party")
        featuredPlaylists = repo.getFeaturedPlaylist()
        favouriteArtist = repo.getAlbumsFromFavouriteArtists()
        newReleases = repo.getNewReleases()
        getMyPlayList=repo.getUserPlaylist()


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


}