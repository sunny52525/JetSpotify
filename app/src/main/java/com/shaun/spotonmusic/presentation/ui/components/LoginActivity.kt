
package com.shaun.spotonmusic.presentation.ui.components
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MainThread
import com.google.android.material.snackbar.Snackbar
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.presentation.ui.activity.BaseSpotifyActivity
import com.shaun.spotonmusic.presentation.ui.activity.TokenActivity
import io.github.kaaes.spotify.webapi.core.models.UserPrivate
import net.openid.appauth.TokenResponse

class LoginActivity : BaseSpotifyActivity() {
    private var usingPendingIntent = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val pendingIntentCheckBox = findViewById<CheckBox>(R.id.pending_intents_checkbox)
        pendingIntentCheckBox.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean -> usingPendingIntent = isChecked }

        findViewById<View>(R.id.retry).setOnClickListener { view: View? -> spotifyAuthClient.authorize(this, REQUEST_CODE_AUTHORIZATION) }

        findViewById<View>(R.id.go_to_token_activity).setOnClickListener { view: View? ->
            val intent = getTokenActivityIntent()
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.start_auth).setOnClickListener { view: View? ->
            if (usingPendingIntent) {
                val completionIntent = getTokenActivityIntent()
                val cancelIntent = Intent(this, LoginActivity::class.java)
                cancelIntent.putExtra(EXTRA_FAILED, true)
                cancelIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                val completionPendingIntent = PendingIntent.getActivity(this, 6, completionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                val cancelPendingIntent = PendingIntent.getActivity(this, 7, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                spotifyAuthClient.authorize(this, completionPendingIntent, cancelPendingIntent)
                finish()
            } else {
                spotifyAuthClient.authorize(this, REQUEST_CODE_AUTHORIZATION)
            }
        }

        if (intent.getBooleanExtra(EXTRA_FAILED, false)) {
            showSnackBar("Authorization with PendingIntent refused")
        }

        displayAuthOptions()

        if (spotifyAuthClient.isAuthorized()) {
            if (spotifyAuthClient.getNeedsTokenRefresh()) {
                spotifyAuthClient.refreshAccessToken()
            } else {
                updateButtonsVisibility(true)
            }
        } else {
            updateButtonsVisibility(false)
        }
    }

    @MainThread
    private fun displayLoading(loadingMessage: String) {
        findViewById<View>(R.id.loading_container).visibility = View.VISIBLE
        findViewById<View>(R.id.auth_container).visibility = View.GONE
        findViewById<View>(R.id.error_container).visibility = View.GONE
        (findViewById<View>(R.id.loading_description) as TextView).text = loadingMessage
    }

    @MainThread
    private fun displayError(error: String?, recoverable: Boolean) {
        findViewById<View>(R.id.error_container).visibility = View.VISIBLE
        findViewById<View>(R.id.loading_container).visibility = View.GONE
        findViewById<View>(R.id.auth_container).visibility = View.GONE
        (findViewById<View>(R.id.error_description) as TextView).text = error
        findViewById<View>(R.id.retry).visibility = if (recoverable) View.VISIBLE else View.GONE
    }

    @MainThread
    private fun displayAuthOptions() {
        findViewById<View>(R.id.auth_container).visibility = View.VISIBLE
        findViewById<View>(R.id.loading_container).visibility = View.GONE
        findViewById<View>(R.id.error_container).visibility = View.GONE
    }

    @MainThread
    private fun showSnackBar(message: String) {
        Snackbar.make(findViewById(R.id.coordinator),
                message,
                Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun onAuthorizationStarted() {
        super.onAuthorizationStarted()
        displayLoading("onAuthorizationStarted")
    }

    override fun onAuthorizationCancelled() {
        super.onAuthorizationCancelled()
        showSnackBar("Authorization cancelled")
        displayAuthOptions()
    }

    override fun onAuthorizationFailed(error: String?) {
        super.onAuthorizationFailed(error)
        showSnackBar("Authorization failed")
        displayError(error, true)
    }

    override fun onAuthorizationRefused(error: String?) {
        super.onAuthorizationRefused(error)
        showSnackBar("Authorization refused")
        displayAuthOptions()
    }

    override fun onAuthorizationSucceed(tokenResponse: TokenResponse?, user: UserPrivate?) {
        super.onAuthorizationSucceed(tokenResponse, user)
        Toast.makeText(this, "AccessToken: " + tokenResponse!!.accessToken, Toast.LENGTH_SHORT).show()
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
        val intent = Intent(this, TokenActivity::class.java)
        intent.putExtra(EXTRA_USING_PENDING_INTENT, usingPendingIntent)
        return intent
    }

    private fun updateButtonsVisibility(isAuthorized: Boolean) {
        if (isAuthorized) {
            findViewById<View>(R.id.start_auth).visibility = View.GONE
            findViewById<View>(R.id.go_to_token_activity).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.start_auth).visibility = View.VISIBLE
            findViewById<View>(R.id.go_to_token_activity).visibility = View.GONE
        }
    }

    companion object {
        private const val EXTRA_FAILED = "failed"
        private const val REQUEST_CODE_AUTHORIZATION = 100
    }
}