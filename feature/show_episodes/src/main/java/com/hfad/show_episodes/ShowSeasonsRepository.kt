package com.hfad.show_episodes

import com.hfad.search.network.OmdbApi
import com.hfad.search.model.SearchResponseBySeason
import javax.inject.Inject
class ShowSeasonsRepository @Inject constructor(
    private val ombdiApi: OmdbApi
){
    suspend fun getSeasonsByIdAndSeasons(id: String) : SearchResponseBySeason {
        return ombdiApi.searchSeasonByIdAndSeason(id, "1")
    }
}