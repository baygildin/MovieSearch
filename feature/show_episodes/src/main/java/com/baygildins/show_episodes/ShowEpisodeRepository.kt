package com.baygildins.show_episodes

import com.baygildins.search.model.SearchResponseEpisodeFullInfo
import com.baygildins.search.network.OmdbApi
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