package com.sbaygildin.search.model

import com.google.gson.annotations.SerializedName

data class SearchByTitle(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val search: List<Search>,
    @SerializedName("totalResults")
    val totalResults: String
)

