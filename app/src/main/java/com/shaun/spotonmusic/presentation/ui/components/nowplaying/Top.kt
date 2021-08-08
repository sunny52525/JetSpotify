package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.R
import java.util.*

@Composable
fun Top (
    albumName:String?,
    dropDown:()->Unit){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxHeight(0.08f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_dropdown),
            contentDescription = "",
            modifier = Modifier.size(30.dp).clickable {
                dropDown()
            }
        )

        Column(Modifier.weight(0.8f)) {
            Text(
                text = "PLAYING FROM PLAYLIST".uppercase(Locale.US),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                fontSize = 10.sp
            )
            Text(
                text = albumName?:"",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold

            )

        }
        Icon(
            imageVector = Icons.Default.MoreVert, tint = MaterialTheme.colors.onSurface,
            contentDescription = null,
            modifier = Modifier.clickable {

            }
        )

    }
}