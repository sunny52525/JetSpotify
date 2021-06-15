package com.shaun.spotonmusic.presentation.ui.components.playlistgrid

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.ui.theme.black

@Preview(showBackground = true)
@Composable
fun TopSpace(title: String = "Chill", color: Color = Color.Cyan) {

    val brush = Brush.verticalGradient(listOf(color, black), endY = 140f)
    Row(
        Modifier
            .background(brush = brush)
            .fillMaxWidth()
            .height(100.dp)
    ) {

        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp),
            textAlign = TextAlign.Left,

            )

    }
}