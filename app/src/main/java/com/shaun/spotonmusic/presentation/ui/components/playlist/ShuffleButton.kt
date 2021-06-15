package com.shaun.spotonmusic.presentation.ui.components.playlist

import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.ui.theme.greenDark
import com.shaun.spotonmusic.ui.theme.lightGreen

enum class ComponentState { Pressed, Released }

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun ShuffleButton(
    modifier: Modifier = Modifier,
    shuffleClicked: () -> Unit = {},
) {


    var toState: ComponentState by remember { mutableStateOf(ComponentState.Released) }

    var backgroundColor: Color by remember {
        mutableStateOf(lightGreen)
    }

    val transition = updateTransition(targetState = toState, label = "")


    val scalex: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 900f) }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed)
            0.95f
        else
            1f
    }


    Surface(
        modifier = modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .graphicsLayer {
                scaleX = scalex;
                scaleY = scalex
            },
        elevation = 8.dp,
        shape = RoundedCornerShape(22.dp),
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = backgroundColor
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            toState = ComponentState.Pressed
                            backgroundColor = greenDark
                            Log.d("TAG", "ShuffleButton: Clicked")
                            tryAwaitRelease()
                            toState = ComponentState.Released
                            backgroundColor = lightGreen
                        },
                        onTap = {
                            shuffleClicked()
                        }

                    )
                }

        ) {
            Text(
                text = "Shuffle Play",
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(
                        start = 25.dp,
                        end = 25.dp,
                        top = 13.dp,
                        bottom = 13.dp
                    ),
            )
        }


    }

}