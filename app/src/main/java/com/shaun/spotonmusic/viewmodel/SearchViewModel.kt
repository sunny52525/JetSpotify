package com.shaun.spotonmusic.viewmodel

import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.repository.SearchRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    context: SpotOnApplication,
    private val retrofit: SpotifyAppService,
    datastoreManager: DatastoreManager,
) : ViewModel() {


    private var accessToken = datastoreManager.accessToken
    private val repository = SearchRepositoryImpl(accessToken.toString(), retrofit)


}