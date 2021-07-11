package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.network.api.RetrofitEnqueue
import com.shaun.spotonmusic.network.api.RetrofitEnqueue.Companion.enqueue
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.network.model.ArtistsArray
import com.shaun.spotonmusic.network.model.LikedSongs
import com.shaun.spotonmusic.network.model.Playlists
import com.shaun.spotonmusic.network.model.SavedAlbums
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.*
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val accessToken: String,
    private val retrofit: SpotifyAppService
) {
    private var api = SpotifyApi()
    private var spotify: kaaes.spotify.webapi.android.SpotifyService
    private val auth = "Authorization: Bearer $accessToken"
    val tokenExpired = MutableLiveData(false)

    init {

        api.setAccessToken(accessToken);
        spotify = api.service

    }


    suspend fun getSavedPlaylistSynchronously(): Playlists =
        retrofit.getMyPlaylist(authorization = auth)

    fun getSavedPlaylist(): MutableLiveData<Pager<PlaylistSimple>?> {

        val result = MutableLiveData<Pager<PlaylistSimple>?>()

        spotify.getMyPlaylists(mapOf("limit" to 50), object : Callback<Pager<PlaylistSimple>> {
            override fun success(t: Pager<PlaylistSimple>?, response: Response?) {

                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {

                Log.d(TAG, "failure:$error ")
            }

        })
        return result
    }

    suspend fun getFollowedArtistsSynchronously(): ArtistsArray = retrofit.getFollowedArtists(
        authorization = auth
    )


    fun getFollowedArtists(): MutableLiveData<ArtistsCursorPager?> {
        val result = MutableLiveData<ArtistsCursorPager?>()
        spotify.getFollowedArtists(object : Callback<ArtistsCursorPager> {
            override fun success(t: ArtistsCursorPager?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: $error")
            }


        })
        return result
    }


    suspend fun getSavedAlbumSynchronously(): SavedAlbums =
        retrofit.getSavedAlbum(authorization = auth)

    fun getSavedAlbum(): MutableLiveData<Pager<SavedAlbum>?> {

        val result = MutableLiveData<Pager<SavedAlbum>?>()

        spotify.getMySavedAlbums(mapOf("limit" to 50), object : Callback<Pager<SavedAlbum>> {
            override fun success(t: Pager<SavedAlbum>?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: $error")
            }

        })

        return result
    }


    fun getUserDetails(): MutableLiveData<UserPrivate> {

        val result = MutableLiveData<UserPrivate>()
        spotify.getMe(object : Callback<UserPrivate> {
            override fun success(t: UserPrivate?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: $error")
                tokenExpired.postValue(true)
            }

        })
        return result
    }

    fun getLikedSongs(): MutableLiveData<LikedSongs> {

        val result = MutableLiveData<LikedSongs>()

        retrofit.getLikedSongs(authorization = auth).enqueue {
            when (it) {

                is RetrofitEnqueue.Companion.Result.Success -> {
                    result.postValue(it.response.body())
                }
                is RetrofitEnqueue.Companion.Result.Failure -> {
                    Log.d(TAG, "getLikedSongs: ${it.error.message}")
                }
            }
        }
        return result
    }


    companion object {
        private const val TAG = "LibraryRepositoryImpl"
    }
}