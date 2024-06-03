package com.hfad.search.model

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

data class EpisodeByIdAndSeason(
    @SerializedName("Episode")
    val episode: String,
    @SerializedName("Released")
    val released: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("imdbRating")
    val imdbRating: String
)