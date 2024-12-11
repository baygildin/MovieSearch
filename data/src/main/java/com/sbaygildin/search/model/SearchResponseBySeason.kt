package com.sbaygildin.search.model

import com.google.gson.annotations.SerializedName

data class SearchResponseBySeason(
    @SerializedName("Episodes")
    val episodes: List<Episode>,
    @SerializedName("Response")
    val response: String,
    @SerializedName("Season")
    val season: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("totalSeasons")
    val totalSeasons: String
)

