package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.snackbar.Snackbar
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.presentation.ui.components.screens.HomeScreen
import com.shaun.spotonmusic.ui.theme.SpotOnMusicTheme
import com.shaun.spotonmusic.viewmodel.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import net.openid.appauth.TokenResponse

@AndroidEntryPoint
class HomeActivity : BaseSpotifyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (spotifyAuthClient.hasConfigurationChanged()) {
            Toast.makeText(this, "Configuration change detected", Toast.LENGTH_SHORT).show()
            signOut()
            return
        }

        setContent {
            SpotOnMusicTheme {
                val viewModel: HomeScreenViewModel = viewModel()
                HomeScreen(
                    this,
                    viewModel
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

//                    spotifyAuthClient.getCurrentUser()
//                )
            }
        } else {
//            displayNotAuthorized("Refused by user")
        }
    }


//
//    @MainThread
//    private fun displayAuthorized(tokenResponse: TokenResponse?, currentUser: UserPublic?) {
//        findViewById<View>(R.id.authorized).visibility = View.VISIBLE
//        findViewById<View>(R.id.not_authorized).visibility = View.GONE
//        findViewById<View>(R.id.loading_container).visibility = View.GONE
//
//        val refreshTokenInfoView = findViewById<TextView>(R.id.refresh_token_info)
//        refreshTokenInfoView.setText(if (tokenResponse!!.refreshToken == null) R.string.no_refresh_token_returned else R.string.refresh_token_returned)
//
//        val idTokenInfoView = findViewById<TextView>(R.id.id_token_info)
//        idTokenInfoView.setText(if (tokenResponse.idToken == null) R.string.no_id_token_returned else R.string.id_token_returned)
//
//        val accessTokenInfoView = findViewById<TextView>(R.id.access_token_info)
//
//        if (tokenResponse.accessToken == null) {
//            accessTokenInfoView.setText(R.string.no_access_token_returned)
//        } else {
//            val expiresAt = tokenResponse.accessTokenExpirationTime
//            if (expiresAt == null) {
//                accessTokenInfoView.setText(R.string.no_access_token_expiry)
//            } else if (expiresAt < System.currentTimeMillis()) {
//                accessTokenInfoView.setText(R.string.access_token_expired)
//            } else {
//                val template = resources.getString(R.string.access_token_expires_at)
//                accessTokenInfoView.text = String.format(
//                    template,
//                    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss ZZ").print(expiresAt)
//                )
//            }
//        }
//        val refreshTokenButton = findViewById<Button>(R.id.refresh_token)
//        refreshTokenButton.visibility =
//            if (tokenResponse.refreshToken != null) View.VISIBLE else View.GONE
//        refreshTokenButton.setOnClickListener { view: View? -> spotifyAuthClient.refreshAccessToken() }
//
//        val viewProfileButton = findViewById<Button>(R.id.view_profile)
//        viewProfileButton.visibility = View.VISIBLE
//        viewProfileButton.setOnClickListener { view: View? -> spotifyAuthClient.fetchUser() }
//
//        findViewById<View>(R.id.sign_out).setOnClickListener { view: View? -> signOut() }
//
//        val userInfoCard = findViewById<View>(R.id.userinfo_card)
//
//        if (currentUser == null) {
//            userInfoCard.visibility = View.INVISIBLE
//        } else {
//            (findViewById<View>(R.id.userinfo_name) as TextView).text = currentUser.display_name
//            (findViewById<View>(R.id.userinfo_json) as TextView).text = Gson().toJson(currentUser)
//            userInfoCard.visibility = View.VISIBLE
//        }
//    }
//
//    @MainThread
//    private fun displayNotAuthorized(explanation: String?) {
//        findViewById<View>(R.id.not_authorized).visibility = View.VISIBLE
//        findViewById<View>(R.id.authorized).visibility = View.GONE
//        findViewById<View>(R.id.loading_container).visibility = View.GONE
//        (findViewById<View>(R.id.explanation) as TextView).text = explanation
//        findViewById<View>(R.id.reauth).setOnClickListener { view: View? -> signOut() }
//    }

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