package com.shaun.spotonmusic.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shaun.spotonmusic.AppConstants.BASEURL
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.network.api.SpotifyAppService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .setLenient().create()

//    val interceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
//    val client = OkHttpClient.Builder().addInterceptor(interceptor)
//        .build()


//    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): SpotOnApplication {
        return app as SpotOnApplication
    }
//
//    @Provides
//
//    fun dataStoreManager(@ApplicationContext appContext: Context): DatastoreManager =
//        DatastoreManager(appContext)

    @Provides
//    @Singleton
    fun spotifyService(): SpotifyAppService = Retrofit.Builder()
        .baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create(gson))
        .build().create(SpotifyAppService::class.java)

}
