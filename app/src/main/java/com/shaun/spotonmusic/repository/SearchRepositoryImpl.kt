package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.utils.TypeConverters.toSpotOnMusicModel
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.AlbumsPager
import kaaes.spotify.webapi.android.models.ArtistsPager
import kaaes.spotify.webapi.android.models.PlaylistsPager
import kaaes.spotify.webapi.android.models.TracksPager
import retrofit.RetrofitError
import retrofit.client.Response

class SearchRepositoryImpl(
    private val accessToken: String,
    private val retrofit: SpotifyAppService
) {

    private var api = SpotifyApi()
    private var spotify: kaaes.spotify.webapi.android.SpotifyService
    var header = "Authorization: Bearer $accessToken"

    init {

        api.setAccessToken(accessToken);
        spotify = api.service
    }


    fun searchAlbums(query: String): MutableLiveData<AlbumsPager?> {

        val result = MutableLiveData<AlbumsPager?>()
        spotify.searchAlbums(query, object : retrofit.Callback<AlbumsPager> {
            override fun success(t: AlbumsPager?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: ${error?.message}")
            }

        })
        return result

    }

    fun searchPlaylists(query: String): MutableLiveData<List<SpotOnMusicModel>?> {

        val result = MutableLiveData<List<SpotOnMusicModel>?>()
        spotify.searchPlaylists(query, object : retrofit.Callback<PlaylistsPager> {
            override fun success(t: PlaylistsPager?, response: Response?) {
                result.postValue(t?.toSpotOnMusicModel())
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: ${error?.message}")
            }

        })
        return result

    }

    fun searchArtists(query: String): MutableLiveData<List<SpotOnMusicModel>?> {

        val result = MutableLiveData<List<SpotOnMusicModel>?>()
        spotify.searchArtists(query, object : retrofit.Callback<ArtistsPager> {
            override fun success(t: ArtistsPager?, response: Response?) {
                result.postValue(t?.toSpotOnMusicModel())
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: ${error?.message}")
            }

        })
        return result

    }

    fun searchTracks(query: String): MutableLiveData<TracksPager?> {

        val result = MutableLiveData<TracksPager?>()
        spotify.searchTracks(query, object : retrofit.Callback<TracksPager> {
            override fun success(t: TracksPager?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: ${error?.message}")
            }

        })
        return result

    }

    companion object {
        private const val TAG = "SearchRepositoryImpl"
    }


}