package com.hfad.search

import com.hfad.search.model.SearchBySearch
import com.hfad.search.model.SearchResponseById
import com.hfad.search.model.SearchResponseByTitle
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET("/")
    suspend fun searchByTitle(@Query("t") title: String): SearchResponseByTitle



    @GET("/")
    suspend fun searchBySearch(@Query("s") searchText: String): SearchBySearch


    @GET("/")
    suspend fun searchById(@Query("i") movieId: String): SearchResponseById
}