package com.hfad.show_episodes

import com.hfad.search.OmdbApi
import com.hfad.search.model.SearchResponseEpisodeFullInfo
import javax.inject.Inject

class ShowEpisodeRepository @Inject constructor(
    val OmbdiApi: OmdbApi
) {
    suspend fun getEpisodeByTitleAndSeasonAndEpisodeNumber(title: String, seasonNumber: String, episodeNumber: String): Result<SearchResponseEpisodeFullInfo> {
        return try {
            val result = OmbdiApi.searchByEpisode(title, seasonNumber, episodeNumber)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}