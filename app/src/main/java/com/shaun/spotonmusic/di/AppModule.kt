package com.shaun.spotonmusic.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import com.google.gson.GsonBuilder
import com.shaun.spotonmusic.BASEURL
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.network.SpotifyAppService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .setLenient().create()

    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor)
        .build()


    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): SpotOnApplication {
        return app as SpotOnApplication
    }
//    @Singleton
//    @Provides
//    fun provideRepository():HomeScreenRepository{
//        return  HomeScreenRepository()
//    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
        appContext.createDataStore("accesstoken")

    @Provides
    @Singleton
    fun spotifyService(): SpotifyAppService = Retrofit.Builder()
        .baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create(gson))
        .client(client) .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(SpotifyAppService::class.java)

}
