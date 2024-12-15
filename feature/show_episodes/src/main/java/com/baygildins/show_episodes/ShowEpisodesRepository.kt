package com.baygildins.show_episodes

import com.baygildins.search.model.SearchResponseBySeason
import com.baygildins.search.network.OmdbApi
import javax.inject.Inject

class ShowEpisodesRepository @Inject constructor(
    val omdbApi: OmdbApi
) {
    suspend fun getEpisodesByTitleAndSeasonNumber(
        title: String,
        seasonNumber: String
    ): SearchResponseBySeason {
        return omdbApi.searchBySeason(title, seasonNumber.toString())
    }
}
