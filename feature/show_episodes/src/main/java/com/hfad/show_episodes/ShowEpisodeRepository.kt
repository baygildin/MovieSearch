package com.hfad.show_episodes

import com.hfad.search.network.OmdbApi
import com.hfad.search.model.SearchResponseEpisodeFullInfo
import javax.inject.Inject

class ShowEpisodeRepository @Inject constructor(
    val OmbdiApi: OmdbApi
) {
    suspend fun getEpisodeByTitleAndSeasonAndEpisodeNumber(
        title: String,
        seasonNumber: String,
        episodeNumber: String
    ): SearchResponseEpisodeFullInfo {
        return OmbdiApi.searchByEpisode(title, seasonNumber, episodeNumber)
    }
}