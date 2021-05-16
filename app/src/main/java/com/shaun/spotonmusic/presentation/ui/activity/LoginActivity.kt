package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.shaun.spotonmusic.AppConstants.AUTH_SCOPES
import com.shaun.spotonmusic.AppConstants.CLIENT_ID
import com.shaun.spotonmusic.AppConstants.REDIRECT_URL
import com.shaun.spotonmusic.presentation.ui.components.LoginScreen
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.authentication.AuthenticationResponse


class LoginActivity : ComponentActivity() {

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
        val builder =
            AuthorizationRequest.Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URL
            )

        builder.setScopes(AUTH_SCOPES)
        val request = builder.build()
        com.spotify.sdk.android.auth.AuthorizationClient.openLoginInBrowser(this, request)
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

    companion object {
        private const val TAG = "LoginActivity"
    }

}


