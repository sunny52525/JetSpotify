package com.shaun.spotonmusic.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.utils.AppConstants.BASEURL
import com.shaun.spotonmusic.utils.PaletteExtractor
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

    @Provides
    fun hasInternetConnection(@ApplicationContext context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result

    }

    @Provides
    @Singleton
    fun getPaletteExtractor() = PaletteExtractor()

}
