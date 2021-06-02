package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.repository.AlbumDetailRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.Album
import javax.inject.Inject


@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    datastoreManager: DatastoreManager
) : ViewModel() {
    private var accessToken = datastoreManager.accessToken

    private var repository: AlbumDetailRepositoryImpl =
        AlbumDetailRepositoryImpl(accessToken = accessToken.toString())

    var tokenExpired = MutableLiveData<Boolean>()

    var albumDetail = MutableLiveData<Album>()


    fun getAlbum(id: String): MutableLiveData<Album> {

        if (id.isEmpty())
            return MutableLiveData(Album())

        return repository.getAlbum(id)
    }

}