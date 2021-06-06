package com.shaun.spotonmusic.presentation.ui.components.homeComponents

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.shaun.spotonmusic.database.model.SuggestionModel
import com.shaun.spotonmusic.utils.getImageUrl

private const val TAG = "SuggestionsRow"


@Composable
fun SuggestionsRow(
    title: String, data: List<SuggestionModel>?,
    size: Int = 170,
    onCardClicked: (String) -> Unit
) {


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


            data?.let {

                it.forEachIndexed { index, item ->
                    item {

                        SuggestionCard(
                            0,
                            imageUrl = getImageUrl(item.imageUrls, 1),
                            item.title,
                            paddingValues = if (index == 0) 20 else 10,
                            size = size,
                            onCardClicked = {
                                onCardClicked(item.id)
                            }
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
    size: Int,
    paddingValues: Int,
    onCardClicked:()->Unit
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
                    }.clickable {
                        onCardClicked()
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
