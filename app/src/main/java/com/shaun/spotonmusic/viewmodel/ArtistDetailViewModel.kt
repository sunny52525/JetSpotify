package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.repository.MusicDetailRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.Artist
import kaaes.spotify.webapi.android.models.Tracks
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    datastoreManager: DatastoreManager,
    spotifyAppService: SpotifyAppService
) : ViewModel() {
    private var accessToken = datastoreManager.accessToken
    private var repository: MusicDetailRepositoryImpl =
        MusicDetailRepositoryImpl(accessToken = accessToken.toString(), spotifyAppService)


    var id = MutableLiveData("")
    var artist: LiveData<Artist?> = Transformations.switchMap(id) {
        repository.getArtist(it)
    }

    val artistTopTracks: LiveData<Tracks?> = Transformations.switchMap(id) {
        repository.getArtistTopTrack(it)
    }

    val topAlbums = Transformations.switchMap(id) {
        repository.getArtistTopAlbums(it)
    }

    val relatedArtist = Transformations.switchMap(id) {
        repository.getRelatedArtist(it)
    }

    val getAppearsOn = Transformations.switchMap(id) {
        repository.getAppearsOn(it)
    }


    var followed = MutableLiveData(booleanArrayOf(false))

    fun setArtist(id: String) {
        this.id.postValue(id)

        followed=repository.followsArtist(id = id)
    }


    fun followArtist(id: String, onFollowed: () -> Unit) {

        followed.postValue(booleanArrayOf(true))
        repository.followArtist(id) {

            onFollowed()
        }

    }

    fun unFollowArtist(id: String, onUnFollowed: () -> Unit) {
        followed.postValue(booleanArrayOf(false))
        repository.unFollowArtist(id) {
            onUnFollowed()
        }

    }
}