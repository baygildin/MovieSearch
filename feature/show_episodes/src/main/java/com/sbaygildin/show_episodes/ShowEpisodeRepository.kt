package com.sbaygildin.show_episodes

import com.sbaygildin.search.model.SearchResponseEpisodeFullInfo
import com.sbaygildin.search.network.OmdbApi
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