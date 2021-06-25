package com.shaun.spotonmusic.di

import com.shaun.spotonmusic.utils.AppConstants
import com.spotify.android.appremote.api.ConnectionParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpotifyModule {


    @Provides
    @Singleton
    fun provideConnectionParams(): ConnectionParams =
        ConnectionParams.Builder(AppConstants.CLIENT_ID).setRedirectUri(AppConstants.REDIRECT_URL)
            .showAuthView(true)
            .build()

}