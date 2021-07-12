package com.shaun.spotonmusic.utils

import android.util.Log
import com.shaun.spotonmusic.presentation.ui.activity.HomeActivity
import com.shaun.spotonmusic.viewmodel.*

inline fun <reified T> observeToken(context: HomeActivity, viewModel: T) {

    when (viewModel) {
        is SearchViewModel -> {
            viewModel.tokenExpired.observeForever {
                if (it){
                    context.spotifyAuthClient.refreshAccessToken()
                    viewModel.tokenExpired.postValue(false)
                }
            }

        }
        is PlaylistDetailViewModel -> {

            viewModel.tokenExpired.observeForever {
                if (it){
                    context.spotifyAuthClient.refreshAccessToken()
                    viewModel.tokenExpired.postValue(false)
                }
            }

        }

        is AlbumDetailViewModel ->{
            viewModel.tokenExpired.observeForever {
                if (it){
                    context.spotifyAuthClient.refreshAccessToken()
                    viewModel.tokenExpired.postValue(false)
                }
            }
        }
        is PlaylistGridViewModel ->{
            viewModel.tokenExpired.observeForever {
                if (it){
                    context.spotifyAuthClient.refreshAccessToken()
                    viewModel.tokenExpired.postValue(false)
                }
            }
        }
        is ArtistDetailViewModel ->{
            viewModel.tokenExpired.observeForever {
                if (it){
                    context.spotifyAuthClient.refreshAccessToken()
                    viewModel.tokenExpired.postValue(false)
                }
            }
        }
        else -> {
            Log.d("TAG", "observeToken: Invalid")
        }
    }

}