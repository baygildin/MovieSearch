package com.hfad.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val OMBD_API_KEY = "360c2147"
interface OmdbApiService {
    @GET("/")
    fun search(@Query("s") searchText: String, @Query("apikey") ombd_api_key: String = OMBD_API_KEY): Call<Search>

    @GET("/")
    fun searchByTitle(@Query("t") searchText: String, @Query("apikey") ombd_api_key: String = OMBD_API_KEY): Call<Search>

    @GET("/")
    fun getMovie(@Query("i") movieId: String, @Query("apikey") ombd_api_key: String = OMBD_API_KEY): Call<Search>
}