package com.shaun.spotonmusic.repository

import androidx.lifecycle.MutableLiveData
import com.shaun.spotonmusic.model.RecentlyPlayed
import kaaes.spotify.webapi.android.models.*
import retrofit2.Response

interface HomeScreenRepository {

    fun getAlbum(albumID: String)
    fun getCategoryPlaylist(category: String): MutableLiveData<PlaylistsPager>
    fun getFeaturedPlaylist(): MutableLiveData<FeaturedPlaylists>

    fun getAlbumsFromFavouriteArtists(index: Int): MutableLiveData<Pager<Album>>

    fun getNewReleases(): MutableLiveData<NewReleases>
    fun getUserPlaylist(): MutableLiveData<Pager<PlaylistSimple>>

    fun getRecentlyPlayed(): MutableLiveData<RecentlyPlayed>
    fun getPlayList(playlistId: String): Response<Playlist>
    fun getPlaylistAsync(playlistId: String): MutableLiveData<Playlist>
    fun getGenre(index: Int, artistId: String?, items:  Pager<Track>?)
    fun getAlbumsOfArtist(artistId: String): MutableLiveData<Pager<Album>>

    fun getBrowse(): MutableLiveData<CategoriesPager>
}