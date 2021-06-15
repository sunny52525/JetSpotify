package com.shaun.spotonmusic.presentation.ui.components.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack


@Preview
@Composable
fun DurationRow(
    dateString: String = "June 2,2021",
    songCount: Int = 1,
    durationInMs: Long = 1000
) {

    Column(
        Modifier
            .fillMaxWidth()
            .background(spotifyDarkBlack)
    ) {

        Text(text = dateString, color = Color.White, modifier = Modifier.padding(start = 10.dp))
        Text(
            text = "$songCount Songs\u2022 ${msToMinutes(durationInMs)}",
            color = Color.White,
            modifier = Modifier.padding(start = 10.dp)
        )
    }

}


fun msToMinutes(milliseconds: Long): String {

    val seconds = (milliseconds / 1000).toInt() % 60
    val minutes = (milliseconds / (1000 * 60) % 60)

    return """
        $minutes min $seconds sec
    """.trimIndent()
}