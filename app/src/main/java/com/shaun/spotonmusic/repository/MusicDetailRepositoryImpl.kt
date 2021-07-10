package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.network.api.RetrofitEnqueue.Companion.Result
import com.shaun.spotonmusic.network.api.RetrofitEnqueue.Companion.enqueue
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.utils.TypeConverters.toSpotOnMusicModel
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.*
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import retrofit2.Call

class MusicDetailRepositoryImpl(
    private val accessToken: String,
    private val retrofit: SpotifyAppService
) {
    private var api = SpotifyApi()
    private var spotify: kaaes.spotify.webapi.android.SpotifyService

    init {

        api.setAccessToken(accessToken);
        spotify = api.service
    }

    var header = "Authorization: Bearer $accessToken"

    fun getAlbum(id: String): MutableLiveData<Album> {
        val result = MutableLiveData<Album>()
        spotify.getAlbum(id, object : Callback<Album> {
            override fun success(t: Album?, response: retrofit.client.Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: getAlbum  $error")
            }

        })
        return result
    }

    fun getPlaylistAsync(playlistId: String): MutableLiveData<Playlist?> {

        val result = MutableLiveData<Playlist?>()

        retrofit.getAPlayList(playList_id = playlistId, "IN", "Authorization: Bearer $accessToken")
            .enqueue {
                when (it) {
                    is Result.Success -> {
                        Log.d(TAG, "getPlaylistAsync: ${it.response.body()}")
                        result.postValue(it.response.body())
                    }
                    is Result.Failure -> {
                        Log.d(TAG, "getRecentlyPlayed: ${it.error}")
                    }
                }
            }

        return result

    }


    fun hasLikedThisSong(id: String): MutableLiveData<Boolean> {

        val result = MutableLiveData<Boolean>()

        retrofit.hasLikedSong(id, header).enqueue(object : retrofit2.Callback<BooleanArray> {
            override fun onResponse(
                call: Call<BooleanArray>,
                response: retrofit2.Response<BooleanArray>
            ) {
                if (response.raw().cacheResponse != null) {
                    Log.d(TAG, "onResponse: ${response.raw().cacheResponse}")
                    Log.e("Network", "response came from cache");
                }

                if (response.raw().networkResponse != null) {
                    Log.e("Network", "response came from server");
                }

                result.postValue(response.body()?.get(0))
            }

            override fun onFailure(call: Call<BooleanArray>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
        return result
    }


    fun followsPlayList(playList: String?, userId: String?): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>(false)

        val call = retrofit.followsPlaylist(
            playList,
            userId = userId,
            "Authorization: Bearer $accessToken"
        )

        call.enqueue {
            when (it) {
                is Result.Success -> {
                    Log.d(TAG, "followsPlayList: ${it.response.body()?.get(0)}")
                    result.postValue(it.response.body()?.get(0))
                }
                is Result.Failure -> {

                    Log.d(TAG, "followsPlayList: ${it.error}")
                }
            }
        }

        return result
    }


    fun followPlaylist(playlistId: String, onFollowed: () -> Unit) {
        Log.d(TAG, "followPlaylist: $playlistId")
        retrofit.followAPlaylist(
            playList_id = playlistId,
            "Authorization: Bearer $accessToken"
        ).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {

                onFollowed()

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

                Log.d(TAG, "onFailure: ${t.message}")
            }

        })

    }

    fun unfollowAPlaylist(playlistId: String, onUnFollowed: () -> Unit) {

        retrofit.unfollowAPlaylist(
            playList_id = playlistId,
            "Authorization: Bearer $accessToken"
        ).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {

                onUnFollowed()

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

                Log.d(TAG, "onFailure: ${t.message}")
            }

        })


    }

    fun followsAlbum(albumId: String): MutableLiveData<Boolean> {

        val result = MutableLiveData(false)
        spotify.containsMySavedAlbums(albumId, object : Callback<BooleanArray> {
            override fun success(t: BooleanArray?, response: Response?) {
                result.postValue(t?.get(0))

            }

            override fun failure(error: RetrofitError?) {

            }

        })
        return result
    }

    fun getAAlbum(albumId: String): MutableLiveData<Album?> {

        val result = MutableLiveData<Album?>()
        spotify.getAlbum(albumId, object : Callback<Album> {
            override fun success(t: Album?, response: Response?) {

                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {

                Log.d(TAG, "failure: get Album ${error}")
            }

        })
        return result
    }


    fun getArtist(id: String): MutableLiveData<Artist?> {

        val result = MutableLiveData<Artist?>()
        spotify.getArtist(id, object : Callback<Artist> {
            override fun success(t: Artist?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: $error")
            }

        })
        return result
    }

    fun getArtistTopTrack(id: String): MutableLiveData<Tracks?> {

        val result = MutableLiveData<Tracks?>()
        spotify.getArtistTopTrack(id, "IN", object : Callback<Tracks> {
            override fun success(t: Tracks?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: ${error?.message}")
            }

        })
        return result
    }


    fun getArtistTopAlbums(id: String): MutableLiveData<Pager<Album>> {

        val result = MutableLiveData<Pager<Album>>()

        spotify.getArtistAlbums(id, object : Callback<Pager<Album>> {
            override fun success(t: Pager<Album>?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: ${error?.message}")
            }

        })
        return result
    }

    fun getRelatedArtist(id: String): MutableLiveData<List<SpotOnMusicModel>> {
        val result = MutableLiveData<List<SpotOnMusicModel>>()
        spotify.getRelatedArtists(id, object : Callback<Artists> {
            override fun success(t: Artists?, response: Response?) {
                result.postValue(t?.toSpotOnMusicModel())
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: ${error?.message}")
            }

        })
        return result
    }


    fun getAppearsOn(id: String): MutableLiveData<List<SpotOnMusicModel>> {

        val result = MutableLiveData<List<SpotOnMusicModel>>()

        spotify.getArtistAlbums(
            id,
            mapOf("include_groups" to "appears_on"),
            object : Callback<Pager<Album>> {
                override fun success(t: Pager<Album>?, response: Response?) {
                    result.postValue(t?.toSpotOnMusicModel())
                }

                override fun failure(error: RetrofitError?) {
                    Log.d(TAG, "failure: ${error?.message}")
                }

            })
        return result
    }


    fun followArtist(id: String, onFollowed: () -> Unit) {

        retrofit.followArtist(ids = id, authorization = header)
            .enqueue {
                when (it) {
                    is Result.Success -> {
                        Log.d(TAG, "followArtist: Success")
                        onFollowed()

                    }
                    is Result.Failure -> {
                        Log.d(TAG, "followArtist: ${it.error.message}")
                    }
                }
            }
    }

    fun unFollowArtist(id: String, onUnFollowed: () -> Unit) {

        retrofit.unFollowArtist(ids = id, authorization = header)
            .enqueue {
                when (it) {
                    is Result.Success -> {
                        Log.d(TAG, "unfollowArtist: Success")
                        onUnFollowed()

                    }
                    is Result.Failure -> {
                        Log.d(TAG, "unfollowArtist: ${it.error.message}")
                    }
                }
            }
    }


    fun followsArtist(id: String): MutableLiveData<BooleanArray> {


        val result = MutableLiveData<BooleanArray>()

        retrofit.follows(ids = id, authorization = header).enqueue {
            when (it) {
                is Result.Success -> {
                    result.postValue(it.response.body())

                }
                is Result.Failure -> {
                    Log.d(TAG, "followsArtist: ${it.error.message}")
                }
            }
        }

        return result

    }

    companion object {
        private const val TAG = "AlbumDetailRepos"
    }

}