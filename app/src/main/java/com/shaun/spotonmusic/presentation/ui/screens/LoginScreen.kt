package com.shaun.spotonmusic.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import com.shaun.spotonmusic.ui.theme.black
import com.shaun.spotonmusic.ui.theme.green

@ExperimentalMaterialApi
@Preview
@Composable
fun LoginScreen(
    onLoginButtonClick: () -> Unit = {},
    onSingUpButton: () -> Unit = {
    }
) {


    Log.d("TAG", "LoginScreen: HERE")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(black),


        ) {
        Box(
            Modifier
                .padding(top = 200.dp, bottom = 50.dp)
                .align(CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_spotify_no_name),
                contentDescription = "Spotify",
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
        ) {
            Text(
                text = "Millions Of Songs.\nFree on Spotify.", color = Color.White,
                modifier = Modifier.align(CenterHorizontally),
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )
        }
        Surface(

            modifier = Modifier

                .width(250.dp)
                .height(50.dp)
                .align(CenterHorizontally), shape = RoundedCornerShape(25.dp),
            onClick = {
                onLoginButtonClick()
            }

        ) {
            Row(
                Modifier
                    .fillMaxHeight()
                    .background(green)
            ) {
                Text(
                    text = "Login", modifier = Modifier
                        .fillMaxWidth(1f)


                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold


                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Card(
                modifier = Modifier

                    .width(250.dp)
                    .height(50.dp)
                    .align(CenterHorizontally)
                    .clickable {
                        Log.d("TAG", "LoginScreen: Signup")
                        onSingUpButton()
                    }, shape = RoundedCornerShape(25.dp)

            ) {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .background(green)
                ) {
                    Text(
                        text = "Signup", modifier = Modifier
                            .fillMaxWidth(1f)


                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        lineHeight = 40.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold


                    )
                }
            }
        }
    }
}