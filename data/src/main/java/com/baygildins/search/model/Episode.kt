package com.baygildins.search.model

import com.google.gson.annotations.SerializedName

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
