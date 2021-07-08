package com.shaun.spotonmusic.repository

import com.shaun.spotonmusic.network.api.SpotifyAppService

class SearchRepositoryImpl(
    private val accessToken: String,
    private val retrofit: SpotifyAppService
) {
}