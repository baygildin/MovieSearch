package com.baygildins.show_episodes

import com.baygildins.search.model.SearchResponseBySeason
import com.baygildins.search.network.OmdbApi
import javax.inject.Inject

class ShowSeasonsRepository @Inject constructor(
    private val ombdiApi: OmdbApi
) {
    suspend fun getSeasonsByIdAndSeasons(id: String): SearchResponseBySeason {
        return ombdiApi.searchSeasonByIdAndSeason(id, "1")
    }
}