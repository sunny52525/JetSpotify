package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import com.shaun.spotonmusic.AppConstants
import com.shaun.spotonmusic.AppConstants.CLIENT_ID
import com.shaun.spotonmusic.AppConstants.REDIRECT_URL
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.database.accesscode.AccessCodeDao
import com.shaun.spotonmusic.database.datastore.read
import com.shaun.spotonmusic.database.datastore.save
import com.shaun.spotonmusic.ui.theme.SpotOnMusicTheme
import com.shaun.spotonmusic.ui.theme.black
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {


    lateinit var dataStore: DataStore<Preferences>

    @Inject
    @Named("dao")
    lateinit var accessCodeDao: AccessCodeDao

    private val REQUEST_CODE = 1337
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = createDataStore(name = "settings")

        setContent {
            SpotOnMusicTheme {
                Surface(color = black) {
                    Splash(onclick = {
                        Log.d(TAG, "onCreate: $REQUEST_CODE")
                        GlobalScope.launch {
                            Log.d(TAG, "onCreate: ${read("code", dataStore)}")
                        }
                    })
                }
            }
        }


    }

    override fun onStart() {
        super.onStart()

        val builder =
            AuthorizationRequest.Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.CODE,
                REDIRECT_URL
            )



        builder.setScopes(AppConstants.AUTH_SCOPES)
        val request = builder.build()
        com.spotify.sdk.android.auth.AuthorizationClient.openLoginActivity(
            this,
            REQUEST_CODE,
            request
        )


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_CODE) {
            val response =
                com.spotify.sdk.android.auth.AuthorizationClient.getResponse(resultCode, intent)
            Log.d(TAG, "onActivityResult: ${response.state} ")

            when (response.type) {
                AuthorizationResponse.Type.CODE -> {


                    GlobalScope.launch {
//                        accessCodeDao.insertCode(AccessCodeEntity(response.code, 1))
                        save("code", response.code, dataStore = dataStore)
                        withContext(Dispatchers.Main) {
                            Intent(this@SplashActivity, com.shaun.spotonmusic.presentation.ui.components.LoginActivity::class.java).apply {
                                startActivity(this)
                                finish()
                            }
                        }
                    }
                    Log.d(TAG, "onActivityResulst: ${response.code}")

                }

                AuthorizationResponse.Type.ERROR -> {
                    startActivity()
                    Log.d(TAG, "onActivityResult2wwwww: ${response.state}")
                }
                else -> {
                    startActivity()
                    Log.d(TAG, "onActivityResult3: error")
                }
            }
        }
    }

    private fun startActivity() {
        Intent(this, LoginActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    companion object {
        private const val TAG = "SplashActivity"
    }


    @Composable
    fun Splash(onclick: () -> Unit) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize(1f)
                .background(black)
        ) {


            Image(
                painter = painterResource(id = R.drawable.ic_spotify_logo),
                contentDescription = "Spotify Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
                    .clickable {
                        onclick()
                    }

            )
        }


    }


}
