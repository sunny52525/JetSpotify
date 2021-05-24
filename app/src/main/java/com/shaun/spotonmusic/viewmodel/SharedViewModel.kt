package com.shaun.spotonmusic.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.getBitmapFromURL
import com.shaun.spotonmusic.model.RecentlyPlayed
import com.shaun.spotonmusic.network.SpotifyAppService
import com.shaun.spotonmusic.presentation.ui.navigation.Routes
import com.shaun.spotonmusic.read
import com.shaun.spotonmusic.repository.HomeScreenRepositoryImpl
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.green
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
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
    var charts = MutableLiveData<List<Playlist?>>()

    var firstFavouriteArtistRecommendations = MutableLiveData<Recommendations>()
    var secondFavouriteArtistRecommendations = MutableLiveData<Recommendations>()

    var categoriesPager = MutableLiveData<CategoriesPager>()

    var searchGridColors=MutableLiveData<List<Color?>>()


    init {
        getAccessToken()

    }

    private fun setToken() {
        repo = HomeScreenRepositoryImpl(accessToken.value.toString(), retrofit)
        albums = repo.album


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

    private fun getAPlaylist(id: String): Response<Playlist> {
        return repo.getPlayList(id)
    }


    private fun getCharts() {

        GlobalScope.launch {

            val chartsIDs = listOf(
                "37i9dQZEVXbMDoHDwVN2tF",
                "37i9dQZEVXbLiRSasKsNU9",
                "37i9dQZEVXbLZ52XmnySJg",
                "37i9dQZF1DX0ieekvzt1Ic",
                "37i9dQZEVXbNG2KDcFcKOF",
                "37i9dQZF1DXcBWIGoYBM5M",
                "37i9dQZF1DWUa8ZRTfalHk",
                "37i9dQZEVXbNG2KDcFcKOF"
            )
            var responses = listOf<Response<Playlist>>()
            try {
                responses = chartsIDs.map {
                    val res = getAPlaylist(it)
                    res
                }
            } catch (e: Exception) {
                Log.d("TAG", "getCharts: $e")
            }

            withContext(Dispatchers.Main) {
                val response = responses.map {
                    it.body()
                }
                charts.postValue(response)


            }
        }
    }


    fun getColorFromSwatch(imageUrl: List<String>)  {

        if (imageUrl.isEmpty()) {
            searchGridColors.postValue(arrayListOf(black))
        } else {
            GlobalScope.launch {

                val bitmapArray = imageUrl.map {
                    getBitmapFromURL(it)
                }

                withContext(Dispatchers.Main) {

                    val colorArray: List<Color?> = bitmapArray.map { bitmap ->
                        if (bitmap != null && !bitmap.isRecycled) {
                            val palette: Palette = Palette.from(bitmap).generate()
                            val dominant = palette.dominantSwatch?.rgb?.let { color ->
                                arrayListOf(color.red, color.green, color.blue)

                            }
                            val composeColor =
                                dominant?.get(0)?.let { it1 ->
                                    Color(
                                        red = it1,
                                        green = dominant[1],
                                        blue = dominant[2]
                                    )
                                }

                            Log.d(TAG, "getColorFromSwatch: $composeColor")
                            composeColor

                        } else {
                            green
                        }


                    }
                    searchGridColors.postValue(colorArray)



                }
            }
        }


    }

}


private const val TAG = "SharedViewModel"