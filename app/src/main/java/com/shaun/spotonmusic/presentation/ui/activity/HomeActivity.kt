/*
 * Copyright 2015 The AppAuth for Android Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.presentation.ui.activity.BaseSpotifyActivity
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import io.github.kaaes.spotify.webapi.core.models.UserPublic
import net.openid.appauth.TokenResponse
import org.joda.time.format.DateTimeFormat

class HomeActivity : BaseSpotifyActivity()  {
    val TAG = "LOL"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (spotifyAuthClient.hasConfigurationChanged()) {
            Toast.makeText(this, "Configuration change detected", Toast.LENGTH_SHORT).show()
            signOut()
            return
        }

        setContentView(R.layout.activity_token)
    }

    override fun onStart() {
        super.onStart()

        if (spotifyAuthClient.isAuthorized()) {
            if (spotifyAuthClient.getNeedsTokenRefresh()) {
                spotifyAuthClient.refreshAccessToken() // Check onRefreshAccessTokenSucceed() called for result
            } else {
                // do your stuff here
                Toast.makeText(this, "User logged in and token valid", Toast.LENGTH_SHORT).show()
                displayAuthorized(
                    spotifyAuthClient.getLastTokenResponse(),
                    spotifyAuthClient.getCurrentUser()
                )
            }
        } else {
            displayNotAuthorized("Refused by user")
        }
    }

    @MainThread
    private fun displayLoading(message: String) {
        findViewById<View>(R.id.loading_container).visibility = View.VISIBLE
        findViewById<View>(R.id.authorized).visibility = View.GONE
        findViewById<View>(R.id.not_authorized).visibility = View.GONE
        (findViewById<View>(R.id.loading_description) as TextView).text = message
    }

    @MainThread
    private fun displayAuthorized(tokenResponse: TokenResponse?, currentUser: UserPublic?) {
        findViewById<View>(R.id.authorized).visibility = View.VISIBLE
        findViewById<View>(R.id.not_authorized).visibility = View.GONE
        findViewById<View>(R.id.loading_container).visibility = View.GONE

        val refreshTokenInfoView = findViewById<TextView>(R.id.refresh_token_info)
        refreshTokenInfoView.setText(if (tokenResponse!!.refreshToken == null) R.string.no_refresh_token_returned else R.string.refresh_token_returned)

        val idTokenInfoView = findViewById<TextView>(R.id.id_token_info)
        idTokenInfoView.setText(if (tokenResponse.idToken == null) R.string.no_id_token_returned else R.string.id_token_returned)

        val accessTokenInfoView = findViewById<TextView>(R.id.access_token_info)

        if (tokenResponse.accessToken == null) {
            accessTokenInfoView.setText(R.string.no_access_token_returned)
        } else {
            val expiresAt = tokenResponse.accessTokenExpirationTime
            if (expiresAt == null) {
                accessTokenInfoView.setText(R.string.no_access_token_expiry)
            } else if (expiresAt < System.currentTimeMillis()) {
                accessTokenInfoView.setText(R.string.access_token_expired)
            } else {
                val template = resources.getString(R.string.access_token_expires_at)
                accessTokenInfoView.text = String.format(
                    template,
                    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss ZZ").print(expiresAt)
                )
            }
        }
        val refreshTokenButton = findViewById<Button>(R.id.refresh_token)
        refreshTokenButton.visibility =
            if (tokenResponse.refreshToken != null) View.VISIBLE else View.GONE
        refreshTokenButton.setOnClickListener { view: View? -> spotifyAuthClient.refreshAccessToken() }

        val viewProfileButton = findViewById<Button>(R.id.view_profile)
        viewProfileButton.visibility = View.VISIBLE
        viewProfileButton.setOnClickListener { view: View? -> spotifyAuthClient.fetchUser() }

        findViewById<View>(R.id.sign_out).setOnClickListener { view: View? -> signOut() }

        val userInfoCard = findViewById<View>(R.id.userinfo_card)

        if (currentUser == null) {
            userInfoCard.visibility = View.INVISIBLE
        } else {
            (findViewById<View>(R.id.userinfo_name) as TextView).text = currentUser.display_name
            (findViewById<View>(R.id.userinfo_json) as TextView).text = Gson().toJson(currentUser)
            userInfoCard.visibility = View.VISIBLE
        }
    }

    @MainThread
    private fun displayNotAuthorized(explanation: String?) {
        findViewById<View>(R.id.not_authorized).visibility = View.VISIBLE
        findViewById<View>(R.id.authorized).visibility = View.GONE
        findViewById<View>(R.id.loading_container).visibility = View.GONE
        (findViewById<View>(R.id.explanation) as TextView).text = explanation
        findViewById<View>(R.id.reauth).setOnClickListener { view: View? -> signOut() }
    }

    @MainThread
    private fun signOut() {
        spotifyAuthClient.logOut()

        val mainIntent = Intent(this, LoginActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainIntent)
        finish()
    }

    override fun onAuthorizationStarted() {
        super.onAuthorizationStarted()
        displayLoading("on Authorization Started")
    }

    override fun onAuthorizationCancelled() {
        super.onAuthorizationCancelled()
        displayNotAuthorized("onAuthorizationCanceled")
    }

    override fun onAuthorizationFailed(error: String?) {
        super.onAuthorizationFailed(error)
        displayNotAuthorized(error)
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
        android.util.Log.d("TAG", "onAuthorizationSucceed: " + tokenResponse?.refreshToken)
        displayAuthorized(tokenResponse, user)
    }

    override fun onRefreshAccessTokenStarted() {
        super.onRefreshAccessTokenStarted()
        displayLoading("on Refresh Access Token Started")
    }

    override fun onRefreshAccessTokenSucceed(tokenResponse: TokenResponse?, user: UserPrivate?) {
        super.onRefreshAccessTokenSucceed(tokenResponse, user)

        android.util.Log.d(TAG, "onRefreshAccessTokenSucceed: ${tokenResponse?.refreshToken}")
        android.util.Log.d(TAG, "onRefreshAccessTokenSucceed: ${tokenResponse?.tokenType}")
        android.util.Log.d(TAG, "onRefreshAccessTokenSucceed: ${tokenResponse?.accessToken}")
        Toast.makeText(this, "Refresh access token succeed ", Toast.LENGTH_SHORT).show()
        displayAuthorized(tokenResponse, user)
    }

}