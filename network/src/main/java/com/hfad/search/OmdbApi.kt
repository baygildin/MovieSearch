package com.hfad.search

import com.hfad.search.model.SearchBySearch
import com.hfad.search.model.SearchResponseById
import com.hfad.search.model.SearchResponseBySeason
import com.hfad.search.model.SearchResponseByTitle
import com.hfad.search.model.SearchResponseEpisodeFullInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET("/")
    suspend fun searchOnlyOneByTitle(@Query("t") title: String): SearchResponseByTitle



    @GET("/")
    suspend fun searchByTitle(@Query("s") searchText: String): SearchBySearch


    @GET("/")
    suspend fun searchById(@Query("i") movieId: String): SearchResponseById
    @GET("/")
    suspend fun searchByEpisode(
        @Query("t") title: String,
        @Query("Season") seasonNumber: String,
        @Query("Episode")episodeNumber: String
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