package com.shaun.spotonmusic.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.read
import com.shaun.spotonmusic.repository.AlbumDetailRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kaaes.spotify.webapi.android.models.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    context: SpotOnApplication,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private var accessToken = MutableLiveData("")

    private lateinit var repository: AlbumDetailRepositoryImpl

    var tokenExpired = MutableLiveData<Boolean>()

    var albumDetail = MutableLiveData<Album>()

    init {
        getAccessToken()
    }

    private fun setToken() {

        repository = AlbumDetailRepositoryImpl(accessToken = accessToken.value.toString())


    }

    private fun getAccessToken() {
        viewModelScope.launch {
            accessToken.value = read(dataStore, "accesstoken")
            withContext(Dispatchers.Main) {
                setToken()
            }

        }
    }

    fun getAlbum(id: String): MutableLiveData<Album> {

        if (id.isEmpty())
            return MutableLiveData(Album())

        return repository.getAlbum(id)
    }

}