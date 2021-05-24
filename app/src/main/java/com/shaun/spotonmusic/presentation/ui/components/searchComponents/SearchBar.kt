package com.shaun.spotonmusic.presentation.ui.components.searchComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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

@ExperimentalComposeUiApi
@Preview
@Composable
fun SearchBar(
    onSearch: (String) -> Unit = {}
) {


    Column(
        Modifier
            .padding( bottom = 10.dp)
            .fillMaxWidth()
            .background(black)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 30.dp, bottom = 10.dp)
                .background(black)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White)
                    .padding(
                        start = 15.dp
                    )
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(Color.Black),
                    alpha = 0.5f
                )
                Text(
                    text = "Artists,Songs, or podcasts", color = Color.Gray,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold

                )

            }


        }
    }
}


//@ExperimentalComposeUiApi
//@Composable
//fun TextFieldWithHint(
//    query: String,
//    onQuery: (String) -> Unit,
//    search: () -> Unit
//) {
//    val keyboardController = LocalSoftwareKeyboardController.current
//
//    TextField(
//
//
//        value = query,
//        onValueChange = { onQuery(it) },
//        textStyle = TextStyle(
//            fontWeight = FontWeight.Bold,
//            color = MaterialTheme.colors.onSecondary
//        ),
//        modifier = Modifier
//
//            .height(50.dp)
//            .fillMaxWidth()
//            .background(Color.White),
//        label = {
//            Text(
//                text = "Search",
//                color = MaterialTheme.colors.onSecondary,
//                modifier = Modifier.fillMaxSize()
//            )
//        },
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Text,
//            imeAction = ImeAction.Search,
//            autoCorrect = true,
//        ),
//        keyboardActions = KeyboardActions(
//            onSearch = {
//                search()
//                keyboardController?.hide()
//            }
//        ),
//        leadingIcon = {
//
//            Image(
//                painter = painterResource(id = R.drawable.ic_search),
//                contentDescription = "",
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .size(24.dp),
//                colorFilter = ColorFilter.tint(Color.Black)
//            )
//
//        }
//
//
//    )
//
//}