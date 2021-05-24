package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.lifecycle.lifecycleScope
import com.shaun.spotonmusic.presentation.ui.components.screens.Splash
import com.shaun.spotonmusic.ui.theme.SpotOnMusicTheme
import com.shaun.spotonmusic.ui.theme.black
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : BaseSpotifyActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpotOnMusicTheme {
                Surface(color = black) {
                    Splash()
                }
            }
        }

        lifecycleScope.launch {
            delay(1000)
            if (!spotifyAuthClient.isAuthorized())
                Intent(this@SplashActivity, LoginActivity::class.java).apply {
                    startApp(this)
                }
            else {
                Intent(this@SplashActivity, HomeActivity::class.java).apply {
                    startApp(this)
                }
            }
        }


    }

    private fun startApp(intent: Intent) {
        startActivity(intent)
        finish()
    }




}
