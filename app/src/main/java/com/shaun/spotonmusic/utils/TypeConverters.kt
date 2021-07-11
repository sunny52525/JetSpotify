package com.shaun.spotonmusic.utils

import com.shaun.spotonmusic.database.model.SpotOnMusicModel
import com.shaun.spotonmusic.network.model.RecentlyPlayed
import kaaes.spotify.webapi.android.models.*

object TypeConverters {


    fun Pager<Artist>.toSpotOnMusicModel(): List<SpotOnMusicModel>? {

        return this.items?.map {
            SpotOnMusicModel(
                title = it.name,
                type = "artist",
                id = it.id,
                imageUrls = it.images.map { image ->
                    image.url
                }
            )
        }
    }

    @JvmName("toSuggestionModelAlbum")
    fun Pager<Album>.toSpotOnMusicModel(): List<SpotOnMusicModel>? {

        return this.items?.map {

            SpotOnMusicModel(
                title = it.name,
                type = "album",
                id = it.id,
                imageUrls = it.images.map { image ->
                    image.url
                }
            )
        }

    }


    @JvmName("toSuggestionModelPlaylistSimple")
    fun Pager<PlaylistSimple>.toSpotOnMusicModel(): List<SpotOnMusicModel>? {

        return this.items?.map {
            SpotOnMusicModel(
                title = it.name,
                imageUrls = it.images.map { image ->
                    image.url
                },
                id = it.id,
                type = "playlist"
            )
        }
    }


    fun RecentlyPlayed.toSpotOnMusicModel(): List<SpotOnMusicModel> {
        return this.items.map {
            SpotOnMusicModel(
                title = it.track.name,
                imageUrls = it.track.album.images.map { image ->
                    image.url
                },
                id = it.track.album.id,
                type = "album"
            )
        }
    }


    fun Recommendations.toSpotOnMusicModel(): List<SpotOnMusicModel>? {
        return this.tracks?.map {
            SpotOnMusicModel(
                title = it.album.name,
                imageUrls = it.album.images.map { image ->
                    image.url
                },
                type = "album",
                id = it.album.id
            )
        }
    }

    fun PlaylistsPager.toSpotOnMusicModel(): List<SpotOnMusicModel>? {
        return this.playlists?.items?.map {
            SpotOnMusicModel(
                title = it?.name ?: "",
                imageUrls = it?.images?.map { image ->
                    image.url
                } ?: listOf(),
                type = "playlist",
                id = it.id ?: ""
            )
        }
    }


    fun NewReleases.toSpotOnMusicModel(): List<SpotOnMusicModel>? {
        return this.albums?.items?.map {
            SpotOnMusicModel(
                title = it.name,
                imageUrls = it.images.map { image ->
                    image.url
                },
                type = "album",
                id = it.id
            )
        }
    }

    fun List<Playlist>.toSpotOnMusicModel(): List<SpotOnMusicModel> {

        return this.map {

            SpotOnMusicModel(
                title = it.description,
                type = "album",
                id = it.id,
                imageUrls = it.images.map { image ->
                    image.url
                }
            )
        }
    }


    fun FeaturedPlaylists.toSpotOnMusicModel(): List<SpotOnMusicModel>? {

        return this.playlists?.items?.map {
            SpotOnMusicModel(
                title = it.name,
                type = "playlist",
                id = it.id,
                imageUrls = it.images.map { image ->
                    image.url
                }
            )
        }
    }

    @JvmName("toSuggestionModelTrack")
    fun Pager<Track>.toSpotOnMusicModel(): List<SpotOnMusicModel> {

        return this.items.map {
            SpotOnMusicModel(
                id = it.id,
                imageUrls = it.album.images.map { image ->
                    image.url
                },
                type = "track",
                title = it.name
            )
        }
    }


    fun SeedsGenres.toListString(): List<String> {
        return this.genres
    }


    fun CategoriesPager.toSpotOnMusicModel(): List<SpotOnMusicModel> {
        return this.categories.items.map {
            SpotOnMusicModel(
                id = it.id,
                imageUrls = it.icons.map {
                    it.url
                },
                title = it.name,
                type = "genre"
            )
        }
    }

    fun Artists.toSpotOnMusicModel():List<SpotOnMusicModel> {
        return  this.artists.map {
            SpotOnMusicModel(
                id = it.id,
                imageUrls = it.images.toListString(),
                title = it.name,
                type = "artist"
            )
        }

    }

    fun List<Image>.toListString(): List<String> {
        return this.map {
            it.url
        }
    }

    fun ArtistsPager.toSpotOnMusicModel():List<SpotOnMusicModel>{
        return this.artists.items.map {
            SpotOnMusicModel(
                id=it.id,
                imageUrls = it.images.toListString(),
                title = it.name,
                type = "artist"
            )
        }
    }


}