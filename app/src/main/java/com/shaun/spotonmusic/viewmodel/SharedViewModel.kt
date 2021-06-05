package com.shaun.spotonmusic.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.network.model.RecentlyPlayed
import com.shaun.spotonmusic.presentation.ui.navigation.BottomNavRoutes
import com.shaun.spotonmusic.repository.HomeScreenRepositoryImpl
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
    private val retrofit: SpotifyAppService,

    private val datastoreManager: DatastoreManager
) : ViewModel() {

    private val _currentScreen = MutableLiveData<BottomNavRoutes>(BottomNavRoutes.Home)
    val currentScreen: LiveData<BottomNavRoutes> = _currentScreen
    private var accessToken =datastoreManager.accessToken
    private lateinit var repo: HomeScreenRepositoryImpl
    private var albums = MutableLiveData<Album>()

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
    var charts = MutableLiveData<List<Playlist>>()

    var firstFavouriteArtistRecommendations = MutableLiveData<Recommendations>()
    var secondFavouriteArtistRecommendations = MutableLiveData<Recommendations>()

    var categoriesPager = MutableLiveData<CategoriesPager>()


    init {
        getAccessToken()

    }

    private fun setToken() {
        repo = HomeScreenRepositoryImpl(accessToken.toString(), retrofit)
        albums = repo.album


        Log.d(TAG, "setToken: $accessToken")
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

//        val value = datastoreManager.accessToken
//
//        Log.d(TAG, "getAccessToken: $value")
//        accessToken.postValue(value)
        setToken()
    }

    fun getAlbum(albumId: String) {
        repo.getAlbum(albumID = albumId)
    }

    fun setCurrentScreen(routes: BottomNavRoutes) {
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
                "37i9dQZEVXbMDoHDwVN2tF", //top 50 global
                "37i9dQZEVXbLiRSasKsNU9", //viral 50 global
                "37i9dQZEVXbLZ52XmnySJg", //top 50 india
                "37i9dQZF1DX0ieekvzt1Ic", //hot hits india
                "37i9dQZEVXbNG2KDcFcKOF", //top songs global
                "37i9dQZF1DXcBWIGoYBM5M", //today's top hit
                "37i9dQZF1DWUa8ZRTfalHk", //pop rising
                "37i9dQZEVXbNG2KDcFcKOF" //top songs global (weekly)
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
                    it.body()!!
                }
                charts.postValue(response)


            }
        }
    }


//    fun getColorFromSwatch(imageUrl: List<String>) {
//
//        if (imageUrl.isEmpty()) {
//            searchGridColors.postValue(arrayListOf(black))
//        } else {
//            GlobalScope.launch {
//
//                val bitmapArray = imageUrl.map {
//                    getBitmapFromURL(it)
//                }
//
//                withContext(Dispatchers.Main) {
//
//                    val colorArray: List<Color?> = bitmapArray.map { bitmap ->
//                        if (bitmap != null && !bitmap.isRecycled) {
//                            val palette: Palette = Palette.from(bitmap).generate()
//                            val dominant = palette.dominantSwatch?.rgb?.let { color ->
//                                arrayListOf(color.red, color.green, color.blue)
//
//                            }
//                            val composeColor: Color? =
//                                dominant?.get(0)?.let { it1 ->
//
//
//                                    Color(
//                                        red = it1,
//                                        green = dominant[1],
//                                        blue = dominant[2],
//                                    )
//                                }
//
//                            composeColor
//
//                        } else {
//                            green
//                        }
//
//
//                    }
//
//                    Log.d(TAG, "getColorFromSwatch: ${colorArray.toPrint()}")
//                    searchGridColors.postValue(colorArray)
//
//
//                }
//            }
//        }
//
//
//    }
//
//}

//
//fun List<Color?>.toPrint(): String {
//    val stringBuilder = StringBuilder()
//    this.forEach {
//        it?.let {
//            val string = """
//            Color(red=${it.red}f,blue=${it.blue}f,green=${it.green}f),
//        """.trimIndent()
//            stringBuilder.append(string)
//        }
//
//    }
//    return stringBuilder.toString()
}

private const val TAG = "SharedViewModel"