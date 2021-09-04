package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.lifecycleScope
import com.pghaz.spotify.webapi.auth.SpotifyAuthorizationCallback
import com.shaun.spotonmusic.presentation.ui.screens.Splash
import com.shaun.spotonmusic.ui.theme.SpotOnMusicTheme
import com.shaun.spotonmusic.ui.theme.black
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.openid.appauth.TokenResponse

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
class SplashActivity : BaseSpotifyActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpotOnMusicTheme {

//                val homeViewModel: SharedViewModel = viewModel()
                Surface(color = black) {
                    Splash()
                }
                check()
            }
        }


    }

    private fun check() {

        spotifyAuthClient.addRefreshTokenCallback(object :
            SpotifyAuthorizationCallback.RefreshToken {
            override fun onRefreshAccessTokenStarted() {
                Log.d("TAG", "onRefreshAccessTokenStarted: Started")
            }

            override fun onRefreshAccessTokenSucceed(
                tokenResponse: TokenResponse?,
                user: UserPrivate?
            ) {
                Log.d("TAG", "onRefreshAccessTokenSucceed: ${tokenResponse?.accessToken} ")
                Intent(this@SplashActivity, HomeActivity::class.java).apply {
                    startApp(this)
                }
            }

        })


        lifecycleScope.launch {
            delay(1000)
            if (!spotifyAuthClient.isAuthorized())
                Intent(this@SplashActivity, LoginActivity::class.java).apply {
                    startApp(this)
                }
            else {
                spotifyAuthClient.refreshAccessToken()
            }
        }
    }

    private fun startApp(intent: Intent) {
        startActivity(intent)
        finish()
    }


}
