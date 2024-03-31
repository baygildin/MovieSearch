package com.hfad.mytestapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val API_KEY = "360c2147"
private const val BASE_URL = "http://www.omdbapi.com/?apikey=$API_KEY&"

/**
 * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * Retrofit service object for creating api calls
 */
//interface OmdbApi {
//    TODO()
////    @GET("jsons")
////    suspend fun getJsons(): List<>
//}