package com.shaun.spotonmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.network.api.RetrofitEnqueue.Companion.Result
import com.shaun.spotonmusic.network.api.RetrofitEnqueue.Companion.enqueue
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.utils.TypeConverters.toListString
import com.shaun.spotonmusic.utils.TypeConverters.toSpotOnMusicModel
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.*
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
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
        "market" to "IN "
    )
    var album = MutableLiveData<Album>()

    val favouriteArtistUrl = MutableLiveData<String>()
    val secondFavouriteArtistUrl = MutableLiveData<String>()

    val favouriteArtists = MutableLiveData<List<SpotOnMusicModel>>()

    val topTracks = MutableLiveData<List<SpotOnMusicModel>>()
    val seedsGenres = MutableLiveData<List<String>>()

    val firstArtistRecommendations = MutableLiveData<List<SpotOnMusicModel>>()
    val secondArtistRecommendations = MutableLiveData<List<SpotOnMusicModel>>()

    init {

        api.setAccessToken(accessToken);
        spotify = api.service

    }

    override fun getAlbum(albumID: String) {

        Log.d(TAG, "getRecommendations: $accessToken")

        spotify.getAlbum(albumID, object : Callback<Album> {
            override fun success(t: Album?, response: Response?) {
                Log.d(TAG, "onResponse: ${response?.body}")
                Log.d(TAG, "success: ${t!!.artists[0].name}")

                album.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "onFailure: ${error?.message}")
                tokenExpired.postValue(true)

            }

        })

    }


    override fun getCategoryPlaylist(category: String): MutableLiveData<List<SpotOnMusicModel>> {

        val result = MutableLiveData<List<SpotOnMusicModel>>()
        spotify.getPlaylistsForCategory(category, mapOf(), object : Callback<PlaylistsPager> {
            override fun success(t: PlaylistsPager?, response: Response?) {

                val v = t?.playlists?.items?.size

                Log.d(TAG, "success: $response")
                Log.d(TAG, "success: Playlist $v")
                result.postValue(t?.toSpotOnMusicModel())
            }

            override fun failure(error: RetrofitError?) {
                tokenExpired.postValue(true)
                Log.d(TAG, "onFailure: ${error?.message}")
            }

        })
        return result
    }


    override fun getFeaturedPlaylist(): MutableLiveData<List<SpotOnMusicModel>> {
        val result = MutableLiveData<List<SpotOnMusicModel>>()
        spotify.getFeaturedPlaylists(map, object : Callback<FeaturedPlaylists> {
            override fun success(t: FeaturedPlaylists?, response: Response?) {
                result.postValue(t?.toSpotOnMusicModel())

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

                favouriteArtists.postValue(t?.toSpotOnMusicModel())

                val artistId = try {

                    t?.items?.get(index = index)?.id
                } catch (e: Exception) {
                    "Error"
                }

                val firstImageUrl = try {

                    t?.items?.forEach {
                        Log.d(TAG, "success: ${it.name}")
                        Log.d(TAG, "success: ${it.images[0].url}")
                    }
                    t?.items?.get(index = 0)?.images?.get(0)?.url
                } catch (e: Exception) {
                    "Error"
                }
                val secondImageUrl = try {
                    t?.items?.get(index = 1)?.images?.get(0)?.url
                } catch (e: Exception) {
                    "Error"
                }

                if (index == 0)
                    favouriteArtistUrl.postValue(firstImageUrl)
                else
                    secondFavouriteArtistUrl.postValue(secondImageUrl)


                getTopTracks(index, artistId)
                Log.d(TAG, "success: $firstImageUrl")

                Log.d(TAG, "artist: $artistId")
                spotify.getArtistAlbums(artistId, object : Callback<Pager<Album>> {
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
                topTracks.postValue(t?.toSpotOnMusicModel())
                Log.d(TAG, "success: Top tracks ${t?.items?.get(0)?.album?.name}")
                getGenre(index, artistId, t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: ${error?.message}")
            }

        })
    }

    override fun getNewReleases(): MutableLiveData<List<SpotOnMusicModel>> {

        val result = MutableLiveData<List<SpotOnMusicModel>>()
        spotify.getNewReleases(mapOf("country" to "IN"), object : Callback<NewReleases> {
            override fun success(t: NewReleases?, response: Response?) {
                result.postValue(t?.toSpotOnMusicModel())
            }

            override fun failure(error: RetrofitError?) {

                Log.d(TAG, "onFailures: ${error?.message}")
            }

        })
        return result

    }


    override fun getUserPlaylist(): MutableLiveData<List<SpotOnMusicModel>> {

        val result = MutableLiveData<List<SpotOnMusicModel>>()

        spotify.getMyPlaylists(mapOf("limit" to 10), object : Callback<Pager<PlaylistSimple>> {

            override fun failure(error: RetrofitError?) {

                Log.d(TAG, "onFailures: ${error?.message}")
            }

            override fun success(t: Pager<PlaylistSimple>?, response: Response?) {
                result.postValue(t?.toSpotOnMusicModel())
            }

        })
        return result
    }


    override fun getRecentlyPlayed(): MutableLiveData<List<SpotOnMusicModel>> {

        val result = MutableLiveData<List<SpotOnMusicModel>>()

        val call = retrofit.getRecentlyPlayed(50, "Authorization: Bearer $accessToken")

        call.enqueue {

            when (it) {
                is Result.Success -> {
                    result.postValue(it.response.body()?.toSpotOnMusicModel())
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

    suspend fun getAPlaylist(playlistId: String): Playlist {
        return retrofit.getOnePlayList(
            playList_id = playlistId,
            "IN",
            "Authorization: Bearer $accessToken"
        )
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
                seedsGenres.postValue(t?.toListString())
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

        val indexToSeed = index.apply {
            if (this == 1)
                2 * 1


        }
        Log.d(TAG, "getRecommendations: $indexToSeed")
        try {

            val recommendationMap = mapOf(
                "limit" to 7,
                "market" to "IN",
                "seed_artists" to artistId,
                "seed_genres" to t!!.genres[indexToSeed] + "," + t.genres[indexToSeed + 1],
                "seed_tracks" to items!!.items[indexToSeed].id + "," + items.items[indexToSeed + 1].id
            )

            spotify.getRecommendations(recommendationMap, object : Callback<Recommendations> {
                override fun success(t: Recommendations?, response: Response?) {

                    Log.d(TAG, "success rec ${t?.tracks?.get(0)?.album?.name}")

                    if (index == 0) {
                        firstArtistRecommendations.postValue(t?.toSpotOnMusicModel())
                    } else {
                        secondArtistRecommendations.postValue(t?.toSpotOnMusicModel())
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


    override fun getAlbumsOfArtist(artistId: String): MutableLiveData<List<SpotOnMusicModel>> {
        val result = MutableLiveData<List<SpotOnMusicModel>>()
        spotify.getArtistAlbums(artistId, map, object : Callback<Pager<Album>> {
            override fun success(t: Pager<Album>?, response: Response?) {
                Log.d(TAG, "artist: ${t?.items?.get(0)?.name}")
                result.postValue(t?.toSpotOnMusicModel())
            }

            override fun failure(error: RetrofitError?) {
//                        tokenExpired.postValue(true)

                Log.d(TAG, "onFailures: ${error?.message}")
            }

        })
        return result
    }


    override fun getBrowse(): MutableLiveData<List<SpotOnMusicModel>> {

        val result = MutableLiveData<List<SpotOnMusicModel>>()
        spotify.getCategories(
            mapOf("country" to "IN", "limit" to 50),
            object : Callback<CategoriesPager> {
                override fun success(t: CategoriesPager?, response: Response?) {

                    result.postValue(t?.toSpotOnMusicModel())
                }

                override fun failure(error: RetrofitError?) {

                    Log.d(TAG, "failure: $error")
                }

            })
        return result
    }


    fun getUserDetail(): MutableLiveData<UserPrivate> {
        val result = MutableLiveData<UserPrivate>()
        spotify.getMe(object : Callback<UserPrivate> {
            override fun success(t: UserPrivate?, response: Response?) {
                result.postValue(t)
            }

            override fun failure(error: RetrofitError?) {
                Log.d(TAG, "failure: $error")
            }

        })
        return result
    }


    companion object {
        private const val TAG = "HomeScreenRepository"
    }

}
