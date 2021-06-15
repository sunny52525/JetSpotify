package com.shaun.spotonmusic.presentation.ui.components.album

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.ui.theme.lightGreen


@Preview(showBackground = true)
@Composable
fun TopBar(
    showHeart: Boolean = true,
    alpha: Float = 1f,
    title: String? = "Album",
    onBackPressed: () -> Unit = {},
    onMoreOptionClicked: () -> Unit = {},
    onLikeButtonClicked: () -> Unit = {},
    backgroundColor: Color = Color.Transparent,
    paddingStart: Int = 8,
    liked: Boolean = false,
    showThreeDots: Boolean = true
) {

    Row(
        Modifier
            .padding(top = 35.dp)
            .fillMaxWidth()
            .background(backgroundColor),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack, tint = MaterialTheme.colors.onSurface,
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    onBackPressed()
                }
                .padding(start = paddingStart.dp)
        )


        Text(
            text = title ?: "",
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(16.dp)
                .alpha(
                    alpha = alpha
                )

        )

        Row {
            if (showHeart) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = if (liked) lightGreen else Color.Transparent,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(20.dp)
                        .clickable {
                            onLikeButtonClicked()
                        },
                )
            }


            if (showThreeDots)
                Icon(
                    imageVector = Icons.Default.MoreVert, tint = MaterialTheme.colors.onSurface,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onMoreOptionClicked()
                    }
                )

        }

    }
}