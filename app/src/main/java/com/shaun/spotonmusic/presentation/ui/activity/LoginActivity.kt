package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import com.shaun.spotonmusic.AppConstants.AUTH_SCOPES
import com.shaun.spotonmusic.AppConstants.CLIENT_ID
import com.shaun.spotonmusic.AppConstants.REDIRECT_URL
import com.shaun.spotonmusic.presentation.ui.components.LoginScreen
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.authentication.AuthenticationResponse
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import net.openid.appauth.TokenResponse


class LoginActivity : BaseSpotifyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(onLoginButtonClick = {
                startLoginFlow()
            }, onSingUpButton = {

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

                    Toast.makeText(this, "Eror", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this, "AccessTokenwo: " + tokenResponse!!.accessToken, Toast.LENGTH_SHORT)
            .show()
        val intent = getTokenActivityIntent()
        startActivity(intent)
        finish()
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
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }
}


