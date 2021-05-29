package com.shaun.spotonmusic.presentation.ui.components.libraryComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaun.spotonmusic.R
import com.shaun.spotonmusic.ui.theme.spotifyDarkBlack


@Preview
@Composable
fun Header() {

    Column(
        modifier = Modifier
            .background(spotifyDarkBlack)
            .padding(top = 80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp,bottom = 20.dp)
        ,verticalAlignment = Alignment.CenterVertically
        ) {

            Card(shape = RoundedCornerShape(50)) {
                Image(
                    painter = painterResource(id = R.drawable.spotify_liked),
                    contentDescription = null,

                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = "Your Library", color = Color.White, textAlign = TextAlign.Left,
                fontWeight = FontWeight.ExtraBold, fontSize = 25.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp)
                    .fillMaxWidth(
                        .8f
                    )
            )

            Image(painter = painterResource(id = R.drawable.ic_search) , contentDescription ="" ,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(20.dp)
                    .fillMaxWidth(0.2f)

                )
            Spacer(modifier = Modifier.width(10.dp))
            Image(painter = painterResource(id = R.drawable.ic_add) , contentDescription ="" ,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(20.dp)
                    .fillMaxWidth(0.2f)

                )


        }
    }
}