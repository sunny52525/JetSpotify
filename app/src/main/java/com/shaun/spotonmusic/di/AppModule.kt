package com.shaun.spotonmusic.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.shaun.spotonmusic.database.accesscode.AccessCodeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun injectRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,AccessCodeDatabase::class.java,"SpotOnMusic").build()


    @Singleton
    @Provides
    @Named("dao")
    fun injectDao(
        database: AccessCodeDatabase
    ) = database.accessCodeDao()


//    @Singleton
//    @Provides
//    fun injectRetrofitAPI() : AccessCodeTre {
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl("www.google.com").build().create(RetrofitAPI::class.java)
//    }



}