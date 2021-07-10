package com.shaun.spotonmusic.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.database.model.LibraryItem
import com.shaun.spotonmusic.database.model.LibraryModel
import com.shaun.spotonmusic.database.model.TYPE
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.network.model.ArtistsArray
import com.shaun.spotonmusic.network.model.LikedSongs
import com.shaun.spotonmusic.network.model.Playlists
import com.shaun.spotonmusic.network.model.SavedAlbums
import com.shaun.spotonmusic.repository.LibraryRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject


@DelicateCoroutinesApi
@HiltViewModel
class LibraryViewModel @Inject constructor(
    context: SpotOnApplication,
    private val retrofit: SpotifyAppService,
    private val datastoreManager: DatastoreManager,
    private val hasInternetConnection: Boolean
) : ViewModel() {

    private var accessToken = datastoreManager.accessToken
    private lateinit var repo: LibraryRepositoryImpl
    var tokenExpired = MutableLiveData<Boolean>()

    var userSavedPlaylist = MutableLiveData<Pager<PlaylistSimple>?>()
    var followedArtists = MutableLiveData<ArtistsCursorPager?>()
    val followedAlbums = MutableLiveData<Pager<SavedAlbum>?>()

    var likedSongs = MutableLiveData<LikedSongs>()

    var libraryItemsList = MutableLiveData<LibraryModel>()
    var libraryItemOriginal = MutableLiveData<LibraryModel>()

    var userDetails = MutableLiveData<UserPrivate>()

    var isGrid = MutableLiveData<Boolean>()

    val chipSelected = MutableLiveData("")


    init {
        isGrid.postValue(true)

        getAccessToken()
    }

    private fun setToken() {
        repo = LibraryRepositoryImpl(accessToken.toString(), retrofit)

        userDetails = repo.getUserDetails()
        likedSongs = repo.getLikedSongs()
        getLibraryItems()
    }

    private fun getAccessToken() {
        setToken()
    }


    fun getLibraryItems() {
        chipSelected.postValue("")

        viewModelScope.launch {
            var savedPlaylist = Playlists(listOf())
            var followedArtist = ArtistsArray()
            var savedAlbums = SavedAlbums(listOf())

            try {
                savedPlaylist = repo.getSavedPlaylistSynchronously()
                followedArtist = repo.getFollowedArtistsSynchronously()
                savedAlbums = repo.getSavedAlbumSynchronously()


            } catch (e: Exception) {

                Log.d(TAG, "getLibraryItems: ${e.message}")

            }


            val libraryList = arrayListOf<LibraryItem>()
            savedPlaylist.items.forEach {

                val libraryItem = LibraryItem(
                    title = it.name,
                    typeID = TYPE.PLAYLIST,
                    type = "Playlist",
                    owner = it.owner.display_name,
                    id = it.id,
                    imageUrl = it.images,
                    recentlyPlayedTime = "",
                    addedAt = "",
                    creator = it.owner.display_name
                )
                libraryList.add(libraryItem)
            }


            followedArtist.artists.items.forEach {

                val libraryItem = LibraryItem(
                    title = it.name,
                    type = "Artist",
                    typeID = TYPE.ARTIST,
                    owner = it.name,
                    id = it.id,
                    imageUrl = it.images,
                    recentlyPlayedTime = "",
                    addedAt = "",
                    creator = it.name
                )

                libraryList.add(libraryItem)
            }


            savedAlbums.items.forEach {
                val libraryItem = LibraryItem(
                    title = it.album.name,
                    typeID = TYPE.ALBUM,
                    type = "Album",
                    owner = it.album.artists[0].name,
                    id = it.album.id,
                    imageUrl = it.album.images,
                    recentlyPlayedTime = "",
                    addedAt = it.added_at,
                    creator = it.album.artists[0].name
                )
                libraryList.add(libraryItem)
            }




            libraryItemsList.postValue(LibraryModel(libraryList))

            libraryItemOriginal.postValue(LibraryModel(libraryList))


            libraryList.forEach {
                Log.d(TAG, "getLibraryItems: ${it.title}")
            }

        }
    }


    fun updateGrid() {
        isGrid.postValue(!isGrid.value!!)
    }

    fun sortItems(mode: String, isSort: Boolean) {

        if (!isSort) {
            libraryItemsList.postValue(libraryItemOriginal.value)
            chipSelected.postValue("")
            return
        }



        chipSelected.postValue(mode)

        var allItems = libraryItemOriginal.value?.items

        allItems = allItems?.filter {
            it.type == mode
        } as ArrayList<LibraryItem>?

        libraryItemsList.postValue(allItems?.let { LibraryModel(it) })

    }

    companion object {
        private const val TAG = "LibraryViewModel"
    }

}