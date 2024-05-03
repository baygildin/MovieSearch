package com.hfad.network


import com.google.gson.annotations.SerializedName

data class Ratings(
    @SerializedName("Source")
    val source: String?,
    @SerializedName("Value")
    val value: String?
)

data class Search(
    @SerializedName("Search")
    val resultSearch: List<Movie>?
)

data class SearchResponse(
    @SerializedName("Search") val searchResults: List<Movie>?,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("Response") val response: String?
)