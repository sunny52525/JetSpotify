package com.shaun.spotonmusic.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.navigation.BottomNavRoutes
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.repository.HomeScreenRepositoryImpl
import com.shaun.spotonmusic.utils.TypeConverters.toSpotOnMusicModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.Playlist
import kaaes.spotify.webapi.android.models.UserPrivate
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val retrofit: SpotifyAppService,
    private val datastoreManager: DatastoreManager,
    private val hasInternetConnection: Boolean
) : ViewModel() {

    private val _currentScreen = MutableLiveData<BottomNavRoutes>(BottomNavRoutes.Home)
    val currentScreen: LiveData<BottomNavRoutes> = _currentScreen

    private lateinit var repo: HomeScreenRepositoryImpl
    private var albums = MutableLiveData<Album>()

    var moodAlbum = MutableLiveData<List<SpotOnMusicModel>>()
    var partyAlbum = MutableLiveData<List<SpotOnMusicModel>>()
    var featuredPlaylists = MutableLiveData<List<SpotOnMusicModel>>()
    var favouriteArtist = MutableLiveData<Pager<Album>>()
    var favouriteArtistImage = MutableLiveData<String>()
    var secondFavouriteArtistImage = MutableLiveData<String>()
    var tokenExpired = MutableLiveData<Boolean>()
    var newReleases = MutableLiveData<List<SpotOnMusicModel>>()
    var getMyPlayList = MutableLiveData<List<SpotOnMusicModel>>()
    var recentlyPlayed = MutableLiveData<List<SpotOnMusicModel>>()
    var secondFavouriteArtist = MutableLiveData<Pager<Album>>()

    var favouriteArtists =
        MutableLiveData<List<SpotOnMusicModel>>()
    var charts = MutableLiveData<List<SpotOnMusicModel>>()

    var firstFavouriteArtistRecommendations = MutableLiveData<List<SpotOnMusicModel>>()
    var secondFavouriteArtistRecommendations = MutableLiveData<List<SpotOnMusicModel>>()

    var categoriesPager = MutableLiveData<List<SpotOnMusicModel>>()


    var myDetails = MutableLiveData<UserPrivate>()

    init {
        getAccessToken()

    }


    fun setToken() {
        repo = HomeScreenRepositoryImpl(datastoreManager.accessToken.toString(), retrofit)
        albums = repo.album
        myDetails = repo.getUserDetail()

        getCharts()

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
        firstFavouriteArtistRecommendations = repo.firstArtistRecommendations
        secondFavouriteArtistRecommendations = repo.secondArtistRecommendations
        categoriesPager = repo.getBrowse()


    }

    fun getAccessToken() {
        setToken()
    }


    fun getAlbum(albumId: String) {
        repo.getAlbum(albumID = albumId)
    }

    fun setCurrentScreen(routes: BottomNavRoutes) {
        _currentScreen.value = routes
    }

    fun getArtistAlbums(artistId: String): MutableLiveData<List<SpotOnMusicModel>> {
        return repo.getAlbumsOfArtist(artistId = artistId)
    }

    private fun getAPlaylist(id: String): Response<Playlist> {
        return repo.getPlayList(id)
    }


    private fun getCharts() {


        Log.d(TAG, "getCharts: $hasInternetConnection")

        viewModelScope.launch {

            val chartsIDs = listOf(
                "37i9dQZEVXbMDoHDwVN2tF", //top 50 global
                "37i9dQZEVXbLiRSasKsNU9", //viral 50 global
                "37i9dQZEVXbLZ52XmnySJg", //top 50 india
                "37i9dQZF1DX0ieekvzt1Ic", //hot hits india
                "37i9dQZEVXbNG2KDcFcKOF", //top songs global
                "37i9dQZF1DXcBWIGoYBM5M", //today's top hit
                "37i9dQZF1DWUa8ZRTfalHk", //pop rising
                "37i9dQZEVXbNG2KDcFcKOF" //top songs global (weekly)
            )
            var responses = listOf<Deferred<Playlist>>()
            try {
                responses = chartsIDs.map {
                    async {
                        repo.getAPlaylist(it)
                    }
                }
            } catch (e: Exception) {
                Log.d("TAG", "getCharts: $e")
            }

            val response = responses.map {
                it.await()
            }
            charts.postValue(response.toSpotOnMusicModel())

        }

    }

    companion object {
        private const val TAG = "SharedViewModel"
    }

}
