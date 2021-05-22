package com.shaun.spotonmusic.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.model.RecentlyPlayed
import com.shaun.spotonmusic.network.SpotifyAppService
import com.shaun.spotonmusic.presentation.ui.navigation.Routes
import com.shaun.spotonmusic.read
import com.shaun.spotonmusic.repository.HomeScreenRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kaaes.spotify.webapi.retrofit.v2.SpotifyService
import kaaes.spotify.webapi.android.models.*
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    context: SpotOnApplication,
    private val dataStore: DataStore<Preferences>,
    private val retrofit: SpotifyAppService
) : ViewModel() {

    private val _currentScreen = MutableLiveData<Routes>(Routes.Home)
    val currentScreen: LiveData<Routes> = _currentScreen
    var accessToken = MutableLiveData("")

    private lateinit var repo: HomeScreenRepositoryImpl
    var albums = MutableLiveData<Album>()

    var moodAlbum = MutableLiveData<PlaylistsPager>()
    var partyAlbum = MutableLiveData<PlaylistsPager>()
    var featuredPlaylists = MutableLiveData<FeaturedPlaylists>()
    var favouriteArtist = MutableLiveData<Pager<Album>>()
    var favouriteArtistImage = MutableLiveData<String>()
    var secondFavouriteArtistImage = MutableLiveData<String>()
    var tokenExpired = MutableLiveData<Boolean>()
    var newReleases = MutableLiveData<NewReleases>()
    var getMyPlayList = MutableLiveData<Pager<PlaylistSimple>>()
    var recentlyPlayed = MutableLiveData<RecentlyPlayed>()
    var secondFavouriteArtist = MutableLiveData<Pager<Album>>()

    var favouriteArtists = MutableLiveData<Pager<Artist>>()


    init {
        getAccessToken()

    }

    private fun setToken() {
        repo = HomeScreenRepositoryImpl(accessToken.value.toString(), retrofit)
        albums = repo.album

        tokenExpired = repo.tokenExpired

        favouriteArtist = repo.getAlbumsFromFavouriteArtists(0)
        secondFavouriteArtist = repo.getAlbumsFromFavouriteArtists(2)
        favouriteArtistImage = repo.favouriteArtistUrl
        secondFavouriteArtistImage = repo.secondFavouriteArtistUrl
        moodAlbum = repo.getCategoryPlaylist("mood")
        partyAlbum = repo.getCategoryPlaylist("party")
        featuredPlaylists = repo.getFeaturedPlaylist()
        newReleases = repo.getNewReleases()
        getMyPlayList = repo.getUserPlaylist()
        recentlyPlayed = repo.getRecentlyPlayed()
        favouriteArtists = repo.favouriteArtists


    }

    fun getAccessToken() {

        viewModelScope.launch {
            accessToken.value = read(dataStore, "accesstoken")
            setToken()


        }
    }

    fun getAlbum(albumId: String) {
        repo.getAlbum(albumID = albumId)
    }

    fun setCurrentScreen(routes: Routes) {
        _currentScreen.value = routes
    }

    fun getArtistAlbums(artistId: String): MutableLiveData<Pager<Album>> {
        return repo.getAlbumsOfArtist(artistId = artistId)
    }

    fun getAPlaylist(id: String): Response<Playlist> {
        return repo.getPlayList(id)
    }

}