package com.shaun.spotonmusic.presentation.ui.components.routeScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shaun.spotonmusic.presentation.ui.activity.HomeActivity
import com.shaun.spotonmusic.presentation.ui.components.GreetingCard
import com.shaun.spotonmusic.presentation.ui.components.RecentHeardBlock
import com.shaun.spotonmusic.presentation.ui.components.SuggestionsRow
import com.shaun.spotonmusic.viewmodel.HomeScreenViewModel
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.PlaylistsPager


@Composable
fun Home(
    viewModel: HomeScreenViewModel, context: HomeActivity
) {

    val playList: PlaylistsPager by viewModel.categoryPlaylistsPager.observeAsState(PlaylistsPager())



    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 50.dp),
    ) {


//        viewModel.tokenExpired.observeForever {
//            if (it == true) {
//                Toast.makeText(
//                    context.applicationContext,
//                    "Restart App",
//                    Toast.LENGTH_SHORT
//                ).show()
////                val intent = context.intent
//                context.recreate()
////                context.finish()
////                context.startActivity(intent)
//
//            }
//        }

        LazyColumn {

            item {
                val title: Album by viewModel.albums.observeAsState(Album())
                android.os.Handler().postDelayed({
                    viewModel.categoryPlaylist("mood")
                }, 2000)

                Greeting(title = title, onClick = { it ->

                    viewModel.getAlbum(it)
//                    Log.d("TAG", "Home: ${viewModel.albums.hasActiveObservers()}")
                    Log.d("TAG", "Home:${title.name} ")
                    Log.d("TAG", "Home: ${viewModel.albums.value?.name}")
                    viewModel.categoryPlaylistsPager.value?.playlists?.items?.forEach {
                        Log.d("TAG", "Homes: ${it.tracks.href}")
                    }
                })
            }
            item {
                RecentHeardBlock()
            }


            item {

                SuggestionsRow(playlistsPager = playList,title = "Mood")
            }



            item {
                Spacer(modifier = Modifier.height(50.dp))
            }

        }

    }

}

@Composable
fun Greeting(title: Album?, onClick: (String) -> Unit) {

    GreetingCard(title = title?.name ?: "", onSettingClicked = {

        onClick("2dIGnmEIy1WZIcZCFSj6i8")


    }, onHistoryClicked = {


        onClick("6QeosPQpJckkW0Obir5RT8")

    })
}