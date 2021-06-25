package com.shaun.spotonmusic.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.utils.AppConstants
import com.shaun.spotonmusic.utils.AppConstants.BASEURL
import com.shaun.spotonmusic.utils.PaletteExtractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {



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
    @Singleton
    fun getPaletteExtractor() = PaletteExtractor()

}
