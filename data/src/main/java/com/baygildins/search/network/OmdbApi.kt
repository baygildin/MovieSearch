package com.baygildins.search.network

import com.baygildins.search.model.ExtendedSearchResponseByTitle
import com.baygildins.search.model.SearchByTitle
import com.baygildins.search.model.SearchResponseById
import com.baygildins.search.model.SearchResponseBySeason
import com.baygildins.search.model.SearchResponseEpisodeFullInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET("/")
    suspend fun searchOnlyOneByTitle(@Query("t") title: String): ExtendedSearchResponseByTitle

    @GET("/")
    suspend fun searchByTitle(@Query("s") searchText: String): SearchByTitle

    @GET("/")
    suspend fun searchById(@Query("i") movieId: String): SearchResponseById

    @GET("/")
    suspend fun searchByEpisode(
        @Query("t") title: String,
        @Query("Season") seasonNumber: String,
        @Query("Episode") episodeNumber: String
    ): SearchResponseEpisodeFullInfo

    @GET("/")
    suspend fun searchBySeason(
        @Query("t") title: String,
        @Query("Season") seasonNumber: String
    ): SearchResponseBySeason

    @GET("/")
    suspend fun searchSeasonByIdAndSeason(
        @Query("i") id: String,
        @Query("Season") seasonNumber: String
    ): SearchResponseBySeason
}