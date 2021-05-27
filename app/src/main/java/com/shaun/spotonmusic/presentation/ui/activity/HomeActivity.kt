package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.snackbar.Snackbar
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.presentation.ui.components.screens.HomeScreen
import com.shaun.spotonmusic.ui.theme.SpotOnMusicTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import net.openid.appauth.TokenResponse


@AndroidEntryPoint
class HomeActivity : BaseSpotifyActivity() {

    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        if (spotifyAuthClient.hasConfigurationChanged()) {
            Toast.makeText(this, "Configuration change detected", Toast.LENGTH_SHORT).show()
            signOut()
            return
        }

        setContent {
            SpotOnMusicTheme(darkTheme = true) {
                HomeScreen(
                    this
                )
            }

        }


    }

    override fun onStart() {
        super.onStart()

        if (spotifyAuthClient.isAuthorized()) {
            if (spotifyAuthClient.getNeedsTokenRefresh()) {
                spotifyAuthClient.refreshAccessToken() // Check onRefreshAccessTokenSucceed() called for result
            } else {
                Toast.makeText(this, "User logged in and token valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @MainThread
    private fun signOut() {
        spotifyAuthClient.logOut()

        val mainIntent = Intent(this, LoginActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainIntent)
        finish()
    }


    override fun onAuthorizationRefused(error: String?) {
        super.onAuthorizationRefused(error)
        Snackbar.make(
            findViewById(R.id.coordinator),
            "Refused by user",
            Snackbar.LENGTH_SHORT
        )
            .show()
    }

    override fun onAuthorizationSucceed(tokenResponse: TokenResponse?, user: UserPrivate?) {
        super.onAuthorizationSucceed(tokenResponse, user)
        Log.d("TAG", "onAuthorizationSucceed: " + tokenResponse?.refreshToken)

    }


    override fun onRefreshAccessTokenSucceed(tokenResponse: TokenResponse?, user: UserPrivate?) {
        super.onRefreshAccessTokenSucceed(tokenResponse, user)

        Log.d(TAG, "onRefreshAccessTokenSucceed: ${tokenResponse?.refreshToken}")
        Log.d(TAG, "onRefreshAccessTokenSucceed: ${tokenResponse?.tokenType}")
        Log.d(TAG, "onRefreshAccessTokenSucceed: ${tokenResponse?.accessToken}")
        Toast.makeText(this, "Refresh access token succeed ", Toast.LENGTH_SHORT).show()

    }

    companion object {
        private const val TAG = "HomeActivity"
    }


}
