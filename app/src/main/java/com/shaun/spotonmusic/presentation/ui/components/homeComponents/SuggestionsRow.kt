package com.shaun.spotonmusic.presentation.ui.components.homeComponents

import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import kaaes.spotify.webapi.android.models.NewReleases
import kaaes.spotify.webapi.android.models.PlaylistsPager

private const val TAG = "SuggestionsRow"


@Composable
fun SuggestionsRow(
    title: String, playlistsPager: PlaylistsPager?
) {


    playlistsPager?.playlists?.let {
        it.items.forEach {
            Log.d(TAG, "SuggestionsRow: ${it.name} + $title")
        }
    }
    Column(Modifier.padding(top = 30.dp)) {


        Text(
            text = title, textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            fontSize = 23.sp,
            color = Color.White

        )
        LazyRow() {


            playlistsPager?.playlists?.let {

                it.items.forEachIndexed { index, item ->
                    item {

                        SuggestionCard(
                            0,
                            imageUrl = item.images[0].url,
                            item.name,
                            paddingValues = if (index == 0) 20 else 10
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun NewReleasesRow(title: String = "Throwback", newReleases: NewReleases) {

    Log.d("TAG", "SuggestionsRow: ")
    Column(Modifier.padding(top = 30.dp)) {


        Text(
            text = title, textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            fontSize = 23.sp,
            color = Color.White
        )
        LazyRow {


            newReleases.albums?.items?.let {


                it.forEachIndexed { index, item ->

                    item {

                        SuggestionCard(
                            0,
                            imageUrl = item.images[0].url,
                            item.name,
                            paddingValues = if (index == 0) 20 else 10
                        )
                    }
                }
            }

        }
    }
}


enum class ComponentState { Pressed, Released }


//@Preview
@Composable
fun SuggestionCard(
    cornerRadius: Int = 0,
    imageUrl: String,
    title: String,
    size: Int = 170,
    paddingValues: Int
) {

    var toState: ComponentState by remember { mutableStateOf(ComponentState.Released) }


    val transition = updateTransition(targetState = toState, label = "")


    val scalex: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed)
            0.99f
        else
            1f
    }
    val scaley: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed) 0.99f else 1f
    }

    Column(
        Modifier
            .padding(bottom = 10.dp, top = 10.dp, start = paddingValues.dp)
            .width(size.dp)


    ) {
        Box(

            modifier = Modifier
                .size((size * scalex).dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            toState = ComponentState.Pressed
                            tryAwaitRelease()
                            toState = ComponentState.Released
                        }
                    )

                },
            contentAlignment = Alignment.Center
        ) {


            Image(
                painter = rememberGlidePainter(request = imageUrl),
                contentDescription = "",

                modifier =
                Modifier
                    .graphicsLayer {
                        scaleX = scalex;
                        scaleY = scaley
                    }

            )
        }
        Text(
            text = title,
            color = Color.Gray,
            textAlign = TextAlign.Left,
            fontSize = 13.sp,
            modifier = Modifier.width(size.dp)
        )

    }


}
