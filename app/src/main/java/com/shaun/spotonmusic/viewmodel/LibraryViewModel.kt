package com.shaun.spotonmusic.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.database.model.LibraryItem
import com.shaun.spotonmusic.database.model.LibraryModel
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.repository.LibraryRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


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

    var libraryItemsList = MutableLiveData<LibraryModel>()

    var userDetails = MutableLiveData<UserPrivate>()

    var isGrid = MutableLiveData<Boolean>()


    init {
        isGrid.postValue(true)

        getAccessToken()
    }

    private fun setToken() {
        repo = LibraryRepositoryImpl(accessToken.toString(), retrofit)

        userDetails = repo.getUserDetails()
        getLibraryItems()
    }

    private fun getAccessToken() {

        //        accessToken.postValue(value)
        setToken()
    }


    private fun getLibraryItems() {


        GlobalScope.launch {
            var savedPlaylist: Pager<PlaylistSimple> = Pager()
            var followedArtist = ArtistsCursorPager()
            var savedAlbums: Pager<SavedAlbum> = Pager()

            try {
                savedPlaylist = repo.getSavedPlaylistSynchronously()
                followedArtist = repo.getFollowedArtistsSynchronously()
                savedAlbums = repo.getSavedAlbumSynchronously()

            } catch (e: Exception) {

                Log.d(TAG, "getLibraryItems: $e")

            }
            withContext(Dispatchers.Main) {

                val libraryList = arrayListOf<LibraryItem>()
                savedPlaylist?.items?.forEach {

                    val libraryItem = LibraryItem(
                        title = it.name,
                        typeID = "playlist",
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

                followedArtist?.artists?.items?.forEach {

                    val libraryItem = LibraryItem(
                        title = it.name,
                        type = "Artist",
                        typeID = "artist",
                        owner = it.name,
                        id = it.id,
                        imageUrl = it.images,
                        recentlyPlayedTime = "",
                        addedAt = "",
                        creator = it.name
                    )

                    libraryList.add(libraryItem)
                }


                savedAlbums?.items?.forEach { it ->
                    val libraryItem = LibraryItem(
                        title = it.album.name,
                        typeID = "album",
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


                libraryList.forEach {
                    Log.d(TAG, "getLibraryItems: $it")
                }
            }
        }
    }


    fun updateGrid() {
        isGrid.postValue(!isGrid.value!!)
    }

    companion object {
        private const val TAG = "LibraryViewModel"
    }

}