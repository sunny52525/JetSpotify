package com.shaun.spotonmusic.presentation.ui.components.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.ui.theme.green
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack

@ExperimentalMaterialApi
@Composable
fun TopClose(onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
        Card(
            onClick = onClick,
            backgroundColor = spotifyDarkBlack,
            modifier = Modifier.padding(end = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close, contentDescription = "", tint = Color.White,
                modifier = Modifier.background(spotifyDarkBlack)
            )
        }
    }
}


@Composable
fun CurrentDevice(deviceName: String, isPhone: Boolean) {


    val image =
        if (isPhone) painterResource(id = R.drawable.ic_phone) else painterResource(id = R.drawable.ic_computer)
    Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {

        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painter = image, contentDescription = "",
            colorFilter = ColorFilter.tint(green),
            modifier = Modifier.size(35.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(Modifier.fillMaxWidth()) {
            Text(
                text = "Listening On",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = deviceName, color = green)
        }
    }
}


@Composable
fun AvailableDevice(isPhone: Boolean, deviceName: String, onClick: () -> Unit) {

    val image =
        if (isPhone) painterResource(id = R.drawable.ic_phone) else painterResource(id = R.drawable.ic_computer)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 5.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = image, contentDescription = "", colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(35.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = deviceName, color = Color.White, fontSize = 15.sp)


    }
}
