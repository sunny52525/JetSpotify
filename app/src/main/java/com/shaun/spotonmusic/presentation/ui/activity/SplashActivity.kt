package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
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
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.database.accesscode.AccessCodeDao
import com.shaun.spotonmusic.ui.theme.SpotOnMusicTheme
import com.shaun.spotonmusic.ui.theme.black
import dagger.hilt.android.AndroidEntryPoint
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import net.openid.appauth.TokenResponse
import javax.inject.Inject
import javax.inject.Named


class SplashActivity : BaseSpotifyActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpotOnMusicTheme {
                Surface(color = black) {
                    Splash(onclick = {

                    })
                }
            }
        }

        Handler().postDelayed({
            if (!spotifyAuthClient.isAuthorized())
                Intent(this, LoginActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            else {
                val intent = getTokenActivityIntent()
                startActivity(intent)
                finish()
            }

        }, 1000)


    }

    private fun getTokenActivityIntent(): Intent {
        return Intent(this, HomeActivity::class.java)
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

    @MainThread
    private fun showSnackBar(message: String) {
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }

}
