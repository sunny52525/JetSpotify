package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.repository.MusicDetailRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.Album
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val retrofit: SpotifyAppService,
    private val datastoreManager: DatastoreManager
) : ViewModel() {

    private var accessToken = datastoreManager.accessToken
    private var repo: MusicDetailRepositoryImpl =
        MusicDetailRepositoryImpl(accessToken.toString(), retrofit)
    var tokenExpired = MutableLiveData<Boolean>()

    var albumId = MutableLiveData("")

    var album: LiveData<Album?> = Transformations.switchMap(albumId) {
        repo.getAAlbum(it)
    }

    var liked = MutableLiveData(false)


    fun setUserId(id: String) {
        this.albumId.postValue(id)
        liked = repo.followsAlbum(id)

    }

    fun updateAlbum(id: String) {

//        if(id==albumId.value)
//            return
        albumId.postValue(id)


    }


}