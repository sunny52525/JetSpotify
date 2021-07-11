package com.shaun.spotonmusic.di

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("accesstoken")



class DatastoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val accessTokenDatastore = appContext.dataStore
    private var accessTokenKey = stringPreferencesKey("accesstoken")

    suspend fun setAccessToken(accessToken: String) {
        accessTokenDatastore.edit { accessTokenpref ->
            accessTokenpref[accessTokenKey] = accessToken

        }
    }


    val accessToken: String? = runBlocking {
        accessTokenDatastore.data.map {
            it[accessTokenKey]
        }.first()
    }
//    val accessToken: Flow<String?> = accessTokenDatastore.data.map {
//        it[ACCESSTOKEN_KEY]
//    }

}