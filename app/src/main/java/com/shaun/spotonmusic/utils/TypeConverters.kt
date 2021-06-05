package com.shaun.spotonmusic.utils

import com.shaun.spotonmusic.database.model.SuggestionModel
import com.shaun.spotonmusic.network.model.RecentlyPlayed
import kaaes.spotify.webapi.android.models.*

class TypeConverters {

    companion object {
        fun Pager<Artist>.toSuggestionModel(): List<SuggestionModel>? {

            return this.items?.map {
                SuggestionModel(
                    title = it.name,
                    type = "artist",
                    id = it.id,
                    imageUrls = it.images
                )
            }
        }

        @JvmName("toSuggestionModelAlbum")
        fun Pager<Album>.toSuggestionModel(): List<SuggestionModel>? {

            return this.items?.map {

                SuggestionModel(
                    title = it.name,
                    type = "album",
                    id = it.id,
                    imageUrls = it.images
                )
            }

        }


        @JvmName("toSuggestionModelPlaylistSimple")
        fun Pager<PlaylistSimple>.toSuggestionModel(): List<SuggestionModel>? {

            return this.items?.map {
                SuggestionModel(
                    title = it.name,
                    imageUrls = it.images,
                    id = it.id,
                    type = "playlist"
                )
            }
        }


        fun RecentlyPlayed.toSuggestionModel(): List<SuggestionModel> {
            return this.items.map {
                SuggestionModel(
                    title = it.track.name,
                    imageUrls = it.track.album.images,
                    id = it.track.album.id,
                    type = "album"
                )
            }
        }


        fun Recommendations.toSuggestionModel(): List<SuggestionModel>? {
            return this.tracks?.map {
                SuggestionModel(
                    title = it.album.name,
                    imageUrls = it.album.images,
                    type = "album",
                    id = it.album.id
                )
            }
        }

        fun PlaylistsPager.toSuggestionModel(): List<SuggestionModel>? {
            return this.playlists?.items?.map {
                SuggestionModel(
                    title = it.name,
                    imageUrls = it.images,
                    type = "playlist",
                    id = it.id
                )
            }
        }


        fun NewReleases.toSuggestionModel(): List<SuggestionModel>? {
            return this.albums?.items?.map {
                SuggestionModel(
                    title = it.name,
                    imageUrls = it.images,
                    type = "album",
                    id = it.id
                )
            }
        }
        fun List<Playlist>.toSuggestionModel(): List<SuggestionModel> {

            return this.map {

                SuggestionModel(
                    title = it.description,
                    type = "album",
                    id = it.id,
                    imageUrls = it.images
                )
            }
        }


        fun FeaturedPlaylists.toSuggestionModel(): List<SuggestionModel>? {

            return  this.playlists?.items?.map {
                SuggestionModel( title = it.name,
                type = "playlist",
                id = it.id,
                imageUrls = it.images)
            }
        }



    }
}