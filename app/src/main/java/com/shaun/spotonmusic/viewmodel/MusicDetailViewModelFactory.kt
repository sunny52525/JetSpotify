package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import javax.inject.Inject


class MusicDetailViewModelFactory
    (
    private val id: String?,
) : ViewModelProvider.Factory {
    @Inject
    lateinit var datastoreManager: DatastoreManager

    @Inject
    lateinit var spotifyAppService: SpotifyAppService
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
            return AlbumDetailViewModel(spotifyAppService, datastoreManager ) as T
        } else
            throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {

    }
}