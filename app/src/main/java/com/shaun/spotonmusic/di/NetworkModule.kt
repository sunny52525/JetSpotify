package com.shaun.spotonmusic.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shaun.spotonmusic.network.api.SpotifyAppService
import com.shaun.spotonmusic.utils.AppConstants
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
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .setLenient().create()

//    private val interceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }



    @Provides
    @Singleton
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
    fun spotifyService(@ApplicationContext context: Context): SpotifyAppService {


        val cacheSpotify = Cache(context.cacheDir, AppConstants.CACHE_SIZE)

        val okHttpClient = OkHttpClient.Builder().cache(cacheSpotify)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasInternetConnection(context = context)) {
                    Log.d("TAG", "spotifyService: Here")
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 60)
                        .removeHeader("Pragma")
                        .build()
                } else
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale= ${60 * 60 * 24 * 7}"
                    ).removeHeader("Pragma")
                        .build()


                chain.proceed(request = request)
            }
//            .addInterceptor(
//                interceptor
//            )
            .build()

        return Retrofit.Builder()
            .baseUrl(AppConstants.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create(SpotifyAppService::class.java)
    }


}