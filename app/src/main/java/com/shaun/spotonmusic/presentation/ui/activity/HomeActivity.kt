package com.shaun.spotonmusic.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.android.material.snackbar.Snackbar
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.presentation.ui.screens.HomeScreen
import com.shaun.spotonmusic.ui.theme.SpotOnMusicTheme
import com.shaun.spotonmusic.viewmodel.LibraryViewModel
import com.shaun.spotonmusic.viewmodel.MusicPlayerViewModel
import com.shaun.spotonmusic.viewmodel.SharedViewModel
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.yashovardhan99.timeit.Stopwatch
import dagger.hilt.android.AndroidEntryPoint
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import net.openid.appauth.TokenResponse


@AndroidEntryPoint
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class HomeActivity : BaseSpotifyActivity(), Stopwatch.OnTickListener {

    var spotifyAppRemote: SpotifyAppRemote? = null
    private val stopwatch = Stopwatch()
    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()
    private val homeViewModel: SharedViewModel by viewModels()
    private val libraryViewModel: LibraryViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        stopwatch.apply {
            setOnTickListener(this@HomeActivity)
            clockDelay = 500
            start()
        }




        if (spotifyAuthClient.hasConfigurationChanged()) {
            Toast.makeText(this, "Configuration change detected", Toast.LENGTH_SHORT).show()
            signOut()
            return
        }

        setContent {
            SpotOnMusicTheme(darkTheme = true) {
                HomeScreen(

                    context = this,
                    musicPlayerViewModel = musicPlayerViewModel,
                    homeViewModel = homeViewModel,
                    libraryViewModel = libraryViewModel
                )
            }

        }


    }

    override fun onStart() {
        super.onStart()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(spotifyAppRemote: SpotifyAppRemote?) {
                Log.d(TAG, "onConnected: Connected")
                this@HomeActivity.spotifyAppRemote = spotifyAppRemote
                musicPlayerViewModel.setSpotifyRemote(spotifyAppRemote = spotifyAppRemote)




                spotifyAppRemote?.let { it ->
                    it.playerApi.subscribeToPlayerState()
                        .setEventCallback { playerState ->

                            val track: Track = playerState.track

                            musicPlayerViewModel.trackDuration.postValue(track.duration)
                            val repeatMode = playerState.playbackOptions.repeatMode
                            musicPlayerViewModel.updateSeekState((playerState.playbackPosition / 1000) / (track.duration / 1000).toFloat())

                            musicPlayerViewModel.isPlaying.postValue(!playerState.isPaused)


                            musicPlayerViewModel.updateRepeatMode(repeatMode)
//                            if (playerState.isPaused)
//                                stopwatch.stop()
//                            else
//                                stopwatch.start()


                            val currentTrackId= track.uri.split(":")[2]

                            musicPlayerViewModel.setPlayerDetails(
                                track.name, track.artist.name,
                                track.imageUri.raw ?: "",
                                track.album.name,
                                currentTrackId

                            )
                        }

                    it.connectApi.subscribeToVolumeState().setEventCallback {
                        musicPlayerViewModel.volume.postValue(it.mVolume)
                    }

                }


            }

            override fun onFailure(throwable: Throwable?) {

                Log.e("MainActivity", throwable?.message, throwable)
            }

        })

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
        homeViewModel.tokenExpired.postValue(false)
        libraryViewModel.tokenExpired.postValue(false)

        homeViewModel.setToken()
        libraryViewModel.setToken()




    }

    companion object {
        private const val TAG = "HomeActivity"
    }


    override fun onDestroy() {
        super.onDestroy()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    override fun onTick(stopwatch: Stopwatch?) {
        spotifyAppRemote?.let {
            it.playerApi.subscribeToPlayerState()
                .setEventCallback { playerState ->
                    val track: Track = playerState.track
                    musicPlayerViewModel.updateSeekState((playerState.playbackPosition / 1000) / (track.duration / 1000).toFloat())


                }


        }


    }
}
