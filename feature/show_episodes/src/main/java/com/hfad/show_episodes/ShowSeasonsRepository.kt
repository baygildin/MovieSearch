package com.hfad.show_episodes

import com.hfad.search.OmdbApi
import com.hfad.search.model.SearchResponseBySeason
import javax.inject.Inject
class ShowSeasonsRepository @Inject constructor(
    private val ombdiApi: OmdbApi
){
    suspend fun getSeasonsByIdAndSeasons(id: String) : Result<SearchResponseBySeason> {
        return try {
            val result = ombdiApi.searchSeasonByIdAndSeason(id, "1")
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}