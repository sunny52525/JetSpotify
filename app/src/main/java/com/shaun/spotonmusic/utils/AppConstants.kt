package com.shaun.spotonmusic.utils

object AppConstants {
    const val REDIRECT_URL = "spotonmusic://callback"
    const val CLIENT_ID = "a4308528f35d4e27898de471f9ae8102"
    val AUTH_SCOPES: Array<String?> = arrayOf(
        "streaming",
        "user-read-recently-played",
        "user-top-read",
        "user-read-playback-position",
        "user-read-playback-state",
        "user-modify-playback-state",
        "user-read-currently-playing",
        "app-remote-control",
        "playlist-modify-public",
        "playlist-modify-private",
        "playlist-read-private",
        "user-follow-modify",
        "user-follow-read",
        "user-library-modify",
        "user-library-read"

    )
    const val BASEURL = "https://api.spotify.com"

    val LIBRARYCHIPS = arrayOf("Playlist", "Album", "Artist")

    val SORT_ITEMS = arrayOf("Recently Played", "Recently Added", "Alphabetical", "Creator")

    const val CACHE_SIZE = (5 * 1024 * 1024).toLong()
    const val DUMMY_IMAGE = "https://i.scdn.co/image/ab67616d0000b273d7fb3e4c63020039d1cff6b2"


    const val RECENTLY_PLAYED = "Recently Played"
    const val RECENTLY_ADDED = "Recently Added"
    const val ALPHABETICAL = "Alphabetical"
    const val CREATOR = "Creator"


}



