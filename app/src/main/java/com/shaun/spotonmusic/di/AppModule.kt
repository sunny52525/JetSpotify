package com.shaun.spotonmusic.di

import android.content.Context
import com.shaun.spotonmusic.SpotOnApplication
import com.shaun.spotonmusic.utils.PaletteExtractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
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
