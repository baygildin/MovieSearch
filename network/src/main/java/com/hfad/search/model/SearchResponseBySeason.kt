package com.hfad.search.model

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

data class Episode(
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

