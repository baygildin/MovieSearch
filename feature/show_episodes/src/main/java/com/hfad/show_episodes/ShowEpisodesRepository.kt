package com.hfad.show_episodes

import com.hfad.search.network.OmdbApi
import com.hfad.search.model.SearchResponseBySeason
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
