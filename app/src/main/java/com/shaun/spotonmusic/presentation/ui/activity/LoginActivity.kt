package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.shaun.spotonmusic.presentation.ui.screens.LoginScreen
import com.spotify.sdk.android.authentication.AuthenticationResponse
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import net.openid.appauth.TokenResponse

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
class LoginActivity : BaseSpotifyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(onLoginButtonClick = {
                Log.d(TAG, "onCreate: Login")
                startLoginFlow()
            }, onSingUpButton = {
                startLoginFlow()

            })
        }


    }

    private fun startLoginFlow() {
        spotifyAuthClient.authorize(this, REQUEST_CODE_AUTHORIZATION)
    }

    override fun onNewIntent(intent: Intent?) {


        if (intent == null)
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show()
        intent?.let {
            val uri = it.data

            Log.d(TAG, "onNewIntent: $uri")

            val response = AuthenticationResponse.fromUri(uri)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {

                    Log.d(TAG, "onActivityResult: ${response.code}")
                    Log.d(TAG, "onActivityResult: ${response.accessToken}")

                    Toast.makeText(this, "Success ${response.accessToken}", Toast.LENGTH_SHORT)
                        .show()
                }
                AuthenticationResponse.Type.ERROR -> {

                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onNewIntent: Error")
                }
                else -> {

                    Toast.makeText(this, "wtf", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onNewIntent: WTF")
                }
            }
        }
        super.onNewIntent(intent)
    }

    override fun onAuthorizationCancelled() {
        super.onAuthorizationCancelled()
        showSnackBar("Canceled")

    }

    override fun onAuthorizationFailed(error: String?) {
        super.onAuthorizationFailed(error)
        showSnackBar("Authorization failed")

    }

    override fun onAuthorizationRefused(error: String?) {
        super.onAuthorizationRefused(error)
        showSnackBar("Authorization refused")

    }

    override fun onAuthorizationSucceed(tokenResponse: TokenResponse?, user: UserPrivate?) {
        super.onAuthorizationSucceed(tokenResponse, user)

        tokenResponse?.let {
            Log.d(TAG, "onAuthorizationSucceed: ${tokenResponse.accessToken}")
            Log.d(TAG, "onAuthorizationSucceed: ${tokenResponse.refreshToken}")
            Log.d(TAG, "onAuthorizationSucceed: ${tokenResponse.tokenType}")
        }
        Toast.makeText(this, "AccessTokenNo: " + tokenResponse!!.accessToken, Toast.LENGTH_SHORT)
            .show()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = getTokenActivityIntent()
            startActivity(intent)
            finish()
        }, 1000)
    }


    override fun onRefreshAccessTokenSucceed(tokenResponse: TokenResponse?, user: UserPrivate?) {
        super.onRefreshAccessTokenSucceed(tokenResponse, user)
        val intent = getTokenActivityIntent()
        startActivity(intent)
        finish()
    }


    private fun getTokenActivityIntent(): Intent {
        return Intent(this, HomeActivity::class.java)
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val REQUEST_CODE_AUTHORIZATION = 100
    }

    @MainThread
    private fun showSnackBar(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


