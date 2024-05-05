package com.hfad.search

import com.hfad.search.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET("/")
    suspend fun searchByTitle(@Query("t") title: String): SearchResponse

}