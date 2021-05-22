package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.model.RecentlyPlayed
import com.shaun.spotonmusic.network.SpotifyAppService
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.*
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import retrofit2.Call
import javax.inject.Inject


class HomeScreenRepositoryImpl @Inject constructor(
    private val accessToken: String,
    private val retrofit: SpotifyAppService
) : HomeScreenRepository {
    private var api = SpotifyApi()
    private var spotify: kaaes.spotify.webapi.android.SpotifyService
    var tokenExpired = MutableLiveData<Boolean>()
    var map = mapOf(
        "country" to "IN",
        "limit" to 7,
        "locale" to "IN",
        "type" to "artists",
        "market" to "IN"
    )
    var album = MutableLiveData<Album>()

    val favouriteArtistUrl = MutableLiveData<String>()
    val secondFavouriteArtistUrl = MutableLiveData<String>()

    val favouriteArtists = MutableLiveData<Pager<Artist>>()

    val topTracks = MutableLiveData<Pager<Track>>()
    val seedsGenres = MutableLiveData<SeedsGenres>()

    val firstArtistRecommendations = MutableLiveData<Recommendations>()
    val secondArtistRecommendations = MutableLiveData<Recommendations>()

    init {

        api.setAccessToken(accessToken);
        spotify = api.service

    }

    override fun getAlbum(albumID: String) {

        Log.d(TAG, "getRecommendations: $accessToken")

        spotify.getAlbum(albumID, object : Callback<Album> {
            override fun success(t: Album?, response: retrofit.client.Response?) {
                Log.d(TAG, "onResponse: ${response?.body}")
                Log.d(TAG, "success: ${t!!.artists[0].name}")

                album.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "onFailure: ${error?.message}")
//                tokenExpired.postValue(true)

            }

        })

    }


    override fun getCategoryPlaylist(category: String): MutableLiveData<PlaylistsPager> {

        val result = MutableLiveData<PlaylistsPager>()
        spotify.getPlaylistsForCategory(category, map, object : Callback<PlaylistsPager> {
            override fun success(t: PlaylistsPager?, response: Response?) {

                Log.d(TAG, "success: ${t!!.playlists.items[0].name}")
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                tokenExpired.postValue(true)
                Log.d(TAG, "onFailure: ${error?.message}")
            }

        })
        return result
    }


    override fun getFeaturedPlaylist(): MutableLiveData<FeaturedPlaylists> {
        val result = MutableLiveData<FeaturedPlaylists>()
        spotify.getFeaturedPlaylists(map, object : Callback<FeaturedPlaylists> {
            override fun success(t: FeaturedPlaylists?, response: Response?) {
                result.postValue(t)

            }

            override fun failure(error: RetrofitError?) {
//                tokenExpired.postValue(true)
            }

        })
        return result
    }

    override fun getAlbumsFromFavouriteArtists(index: Int): MutableLiveData<Pager<Album>> {

        val result = MutableLiveData<Pager<Album>>()
        spotify.getTopArtists(mapOf("limit" to 10), object : Callback<Pager<Artist>> {
            override fun success(t: Pager<Artist>?, response: Response?) {

                favouriteArtists.postValue(t)
                val artistId = try {

                    t?.items?.get(index = index)?.id
                } catch (e: Exception) {
                    "Error"
                }

                val imageUrl = try {
                    t?.items?.get(index = index)?.images?.get(0)?.url
                } catch (e: Exception) {
                    "Error"
                }

                if (index == 0)
                    favouriteArtistUrl.postValue(imageUrl)
                else
                    secondFavouriteArtistUrl.postValue(imageUrl)


                getTopTracks(index, artistId)
                Log.d(TAG, "success: $imageUrl")

                Log.d(TAG, "artist: $artistId")
                spotify.getArtistAlbums(artistId, map, object : Callback<Pager<Album>> {
                    override fun success(t: Pager<Album>?, response: Response?) {
                        Log.d(TAG, "artist: ${t?.items?.get(0)?.name}")
                        result.postValue(t)
                    }

                    override fun failure(error: RetrofitError?) {
//                        tokenExpired.postValue(true)

                        Log.d(TAG, "onFailures: ${error?.message}")
                    }

                })

            }

            override fun failure(error: RetrofitError?) {
//                tokenExpired.postValue(true)

                Log.d(TAG, "onFailures: ${error?.message}")
            }

        })

        return result
    }

    private fun getTopTracks(index: Int, artistId: String?) {
        spotify.getTopTracks(mapOf("limit" to 10), object : Callback<Pager<Track>> {
            override fun success(t: Pager<Track>?, response: Response?) {
                topTracks.postValue(t)
                Log.d(TAG, "success: Top tracks ${t?.items?.get(0)?.album?.name}")
                getGenre(index, artistId, t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: ${error?.message}")
            }

        })
    }

    override fun getNewReleases(): MutableLiveData<NewReleases> {

        val result = MutableLiveData<NewReleases>()
        spotify.getNewReleases(mapOf("country" to "IN"), object : Callback<NewReleases> {
            override fun success(t: NewReleases?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {

                Log.d(TAG, "onFailures: ${error?.message}")
            }

        })
        return result

    }


    override fun getUserPlaylist(): MutableLiveData<Pager<PlaylistSimple>> {

        val result = MutableLiveData<Pager<PlaylistSimple>>()

        spotify.getMyPlaylists(mapOf("limit" to 10), object : Callback<Pager<PlaylistSimple>> {

            override fun failure(error: RetrofitError?) {

                Log.d(TAG, "onFailures: ${error?.message}")
            }

            override fun success(t: Pager<PlaylistSimple>?, response: Response?) {
                result.postValue(t)
            }

        })
        return result
    }


    override fun getRecentlyPlayed(): MutableLiveData<RecentlyPlayed> {

        val result = MutableLiveData<RecentlyPlayed>()

        val call = retrofit.getRecentlyPlayed(10, "Authorization: Bearer $accessToken")

        call.enqueue {

            when (it) {
                is Result.Success -> {
                    result.postValue(it.response.body())
                }
                is Result.Failure -> {
                    Log.d(TAG, "getRecentlyPlayed: ${it.error}")
                }
            }
        }


        return result

    }


    override fun getPlayList(playlistId: String): retrofit2.Response<Playlist> {
        return retrofit.getAPlayList(playlistId, "IN", "Authorization: Bearer $accessToken")
            .execute()
    }

    override fun getPlaylistAsync(playlistId: String): MutableLiveData<Playlist> {

        val result = MutableLiveData<Playlist>()

        retrofit.getAPlayList(playList_id = playlistId, "IN", "Authorization: Bearer $accessToken")
            .enqueue {
                when (it) {
                    is Result.Success -> {
                        result.postValue(it.response.body())
                    }
                    is Result.Failure -> {
                        Log.d(TAG, "getRecentlyPlayed: ${it.error}")
                    }
                }
            }

        return result

    }

    override fun getGenre(
        index: Int,
        artistId: String?,
        items: Pager<Track>?
    ) {

        spotify.getSeedsGenres(object : Callback<SeedsGenres> {
            override fun success(t: SeedsGenres?, response: Response?) {
                seedsGenres.postValue(t)
                Log.d(TAG, "success: generes ${t?.genres}")
                getRecommendations(index, artistId, items, t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "Seed: $error")
            }

        })

    }

    private fun getRecommendations(
        index: Int,
        artistId: String?,
        items: Pager<Track>?,
        t: SeedsGenres?
    ) {

        try {

            val recommendationMap = mapOf(
                "limit" to 7,
                "market" to "IN",
                "seed_artists" to artistId,
                "seed_genres" to t!!.genres[0] + "," + t.genres[1],
                "seed_tracks" to items!!.items[0].id + "," + items.items[0].id
            )

            spotify.getRecommendations(recommendationMap, object : Callback<Recommendations> {
                override fun success(t: Recommendations?, response: Response?) {

                    Log.d(TAG, "success rec ${t?.tracks?.get(0)?.album?.name}")

                    if (index == 0) {
                        firstArtistRecommendations.postValue(t)
                    } else {
                        secondArtistRecommendations.postValue(t)
                    }
                }

                override fun failure(error: RetrofitError?) {

                    Log.d(TAG, "recommendation: $error")
                }

            })


        } catch (e: Exception) {
            Log.d(TAG, "getRecommendations: $e")
        }

    }


    override fun getAlbumsOfArtist(artistId: String): MutableLiveData<Pager<Album>> {
        val result = MutableLiveData<Pager<Album>>()
        spotify.getArtistAlbums(artistId, map, object : Callback<Pager<Album>> {
            override fun success(t: Pager<Album>?, response: Response?) {
                Log.d(TAG, "artist: ${t?.items?.get(0)?.name}")
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
//                        tokenExpired.postValue(true)

                Log.d(TAG, "onFailures: ${error?.message}")
            }

        })
        return result
    }

    companion object {
        private const val TAG = "HomeScreenRepository"
    }


    sealed class Result<T> {
        data class Success<T>(val call: Call<T>, val response: retrofit2.Response<T>) : Result<T>()
        data class Failure<T>(val call: Call<T>, val error: Throwable) : Result<T>()
    }


    private inline fun <reified T> Call<T>.enqueue(crossinline result: (Result<T>) -> Unit) {
        enqueue(object : retrofit2.Callback<T> {
            override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
                result(Result.Success(call, response))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                result(Result.Failure(call = call, error = t))
            }

        })
    }

}
