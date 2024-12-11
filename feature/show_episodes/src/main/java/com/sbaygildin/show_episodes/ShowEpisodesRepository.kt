package com.sbaygildin.show_episodes

import com.sbaygildin.search.model.SearchResponseBySeason
import com.sbaygildin.search.network.OmdbApi
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
