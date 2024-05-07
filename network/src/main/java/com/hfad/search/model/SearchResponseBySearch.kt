package com.hfad.search.model

import com.google.gson.annotations.SerializedName
data class SearchBySearch(
    @SerializedName("Response")
    val Response: String,
    @SerializedName("Search")
    val Search: List<Search>,
    @SerializedName("totalResults")
    val totalResults: String
)

data class Search(
    @SerializedName("Poster")
    val Poster: String,
    @SerializedName("Title")
    val Title: String,
    @SerializedName("Type")
    val Type: String,
    @SerializedName("Year")
    val Year: String,
    @SerializedName("imdbID")
    val imdbID: String
)