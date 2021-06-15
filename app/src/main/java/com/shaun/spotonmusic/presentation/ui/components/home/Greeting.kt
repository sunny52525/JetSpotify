package com.shaun.spotonmusic.presentation.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.R


//@Preview
@Composable
fun GreetingCard(
    title:String,
    onSettingClicked: () -> Unit,
    onHistoryClicked: () -> Unit

) {

    Column(
        modifier = Modifier.fillMaxWidth(1f),
    ) {
        Row(Modifier.padding(end = 10.dp)) {

            Spacer(modifier = Modifier.width(20.dp))
            Text(
                title,
                color = Color.White,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(.8f),
                fontSize = 23.sp
            )
            Image(
                painter = painterResource(id = R.drawable.ic_history),
                contentDescription = "History",

                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .clickable {
                        onHistoryClicked()
                    },
                colorFilter = ColorFilter.tint(Color.White)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Setting",

                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .clickable {
                        onSettingClicked()
                    },
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}