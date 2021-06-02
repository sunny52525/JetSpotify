package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.repository.AlbumDetailRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.Album
import javax.inject.Inject


@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    context: SpotOnApplication,
    private val datastoreManager: DatastoreManager
) : ViewModel() {
    private var accessToken = datastoreManager.accessToken

    private lateinit var repository: AlbumDetailRepositoryImpl

    var tokenExpired = MutableLiveData<Boolean>()

    var albumDetail = MutableLiveData<Album>()

    init {
        getAccessToken()
    }

    private fun setToken() {

        repository = AlbumDetailRepositoryImpl(accessToken = accessToken.toString())


    }

    private fun getAccessToken() {
        val value = datastoreManager.accessToken

//        accessToken.postValue(value)
        setToken()
    }

    fun getAlbum(id: String): MutableLiveData<Album> {

        if (id.isEmpty())
            return MutableLiveData(Album())

        return repository.getAlbum(id)
    }

}