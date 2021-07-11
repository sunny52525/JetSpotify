package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.pghaz.spotify.webapi.auth.SpotifyAuthorizationCallback
import com.pghaz.spotify.webapi.auth.SpotifyAuthorizationClient
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.di.DatastoreManager
import com.shaun.spotonmusic.utils.AppConstants.AUTH_SCOPES
import com.shaun.spotonmusic.utils.AppConstants.CLIENT_ID
import com.shaun.spotonmusic.utils.AppConstants.REDIRECT_URL
import com.spotify.android.appremote.api.ConnectionParams
import dagger.hilt.android.AndroidEntryPoint
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import kotlinx.coroutines.launch
import net.openid.appauth.TokenResponse
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseSpotifyActivity : ComponentActivity(), SpotifyAuthorizationCallback.Authorize,
    SpotifyAuthorizationCallback.RefreshToken {


    lateinit var dataStoreManager: DatastoreManager



    @Inject
    lateinit var connectionParams: ConnectionParams

    companion object {
        const val EXTRA_USING_PENDING_INTENT = "EXTRA_USING_PENDING_INTENT"
    }

    lateinit var spotifyAuthClient: SpotifyAuthorizationClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreManager = DatastoreManager(
            this
        )

        spotifyAuthClient = SpotifyAuthorizationClient.Builder(
            CLIENT_ID,
            REDIRECT_URL

        )
            .setScopes(AUTH_SCOPES)
            .setFetchUserAfterAuthorization(true)
            .setCustomTabColor(ContextCompat.getColor(this, R.color.spotifyGreen))
            .build(this)


        spotifyAuthClient.setDebugMode(true)
        spotifyAuthClient.addAuthorizationCallback(this)
        spotifyAuthClient.addRefreshTokenCallback(this)
    }

    override fun onStart() {
        super.onStart()





        spotifyAuthClient.onStart()

        val usingPendingIntent = intent?.getBooleanExtra(EXTRA_USING_PENDING_INTENT, false)

        // Note: Use this if you're using: authorize(context, completionPendingIntent, cancelPendingIntent)
        if (intent != null && usingPendingIntent == true) {
            // At this point it is authorized but we don't have access token yet.
            // We get it at when onAuthorizationSucceed() is called
            spotifyAuthClient.onCompletionActivity(intent)
        }
    }

    // Note: Use this if you're using: spotifyAuthClient.authorize(context, requestCode)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // At this point it is authorized but we don't have access token yet.
        // We get it at when onAuthorizationSucceed() is called
        spotifyAuthClient.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        spotifyAuthClient.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        spotifyAuthClient.onDestroy()
    }

    override fun onAuthorizationStarted() {

    }

    override fun onAuthorizationCancelled() {

    }

    override fun onAuthorizationFailed(error: String?) {

    }

    override fun onAuthorizationRefused(error: String?) {

    }

    override fun onAuthorizationSucceed(tokenResponse: TokenResponse?, user: UserPrivate?) {
        Log.d(TAG, "onAuthorizationSucceed: ${tokenResponse?.accessToken}")
        lifecycleScope.launch {
            dataStoreManager.setAccessToken(accessToken = tokenResponse?.accessToken!!)
        }
    }

    override fun onRefreshAccessTokenStarted() {

    }

    override fun onRefreshAccessTokenSucceed(tokenResponse: TokenResponse?, user: UserPrivate?) {

        Log.d(TAG, "onRefreshAccessTokenSucceed: ${tokenResponse?.accessToken}")
        lifecycleScope.launch {
//            save(dataStore, "accesstoken", tokenResponse?.accessToken!!)
            dataStoreManager.setAccessToken(accessToken = tokenResponse?.accessToken!!)
        }
    }
}

private const val TAG = "BaseSpotifyActivity"