package com.shaun.spotonmusic.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.network.api.SpotifyAppService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class SearchViewModel @Inject constructor(
    context: SpotOnApplication,
    private val dataStore: DataStore<Preferences>,
    private val retrofit: SpotifyAppService
) : ViewModel() {

}