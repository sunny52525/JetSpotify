package com.shaun.spotonmusic.utils

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shaun.spotonmusic.SpotOnApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ViewModelTest @Inject constructor(
    context: SpotOnApplication,
//
//    datastoreManager: DatastoreManager,
//    spotifyAppService: SpotifyAppService
) : ViewModel() {


//    val ds=DatastoreManager()

    private var t = mutableStateOf("")
}