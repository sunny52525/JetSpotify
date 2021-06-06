package com.shaun.spotonmusic.repository

import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.network.model.RecentlyPlayed
import kaaes.spotify.webapi.android.models.*
import retrofit2.Response

interface HomeScreenRepository {

    fun getAlbum(albumID: String)
    fun getCategoryPlaylist(category: String): MutableLiveData<List<SpotOnMusicModel>>
    fun getFeaturedPlaylist(): MutableLiveData<List<SpotOnMusicModel>>

    fun getAlbumsFromFavouriteArtists(index: Int): MutableLiveData<Pager<Album>>

    fun getNewReleases(): MutableLiveData<List<SpotOnMusicModel>>
    fun getUserPlaylist(): MutableLiveData<List<SpotOnMusicModel>>

    fun getRecentlyPlayed(): MutableLiveData<List<SpotOnMusicModel>>
    fun getPlayList(playlistId: String): Response<Playlist>
    fun getPlaylistAsync(playlistId: String): MutableLiveData<Playlist>
    fun getGenre(index: Int, artistId: String?, items:  Pager<Track>?)
    fun getAlbumsOfArtist(artistId: String): MutableLiveData<List<SpotOnMusicModel>>

    fun getBrowse(): MutableLiveData<List<SpotOnMusicModel>>
}