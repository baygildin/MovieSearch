package com.baygildins.search.model

import com.google.gson.annotations.SerializedName

data class SearchSeasonByIdAndSeason(
    @SerializedName("Episodes")
    val episodes: List<EpisodeByIdAndSeason>,
    @SerializedName("Response")
    val response: String,
    @SerializedName("Season")
    val season: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("totalSeasons")
    val totalSeasons: String
)
