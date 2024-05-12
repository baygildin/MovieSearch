package com.hfad.network

import com.google.gson.annotations.SerializedName

data class SearchResponseEpisodeFullInfo(
    val Actors: String,
    val Awards: String,
    val Country: String,
    val Director: String,
    val Episode: String,
    val Genre: String,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Ratings: List<Rating>,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Season: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String,
    val seriesID: String
)

data class Rating(
    val Source: String,
    val Value: String
)

data class Movie(
    @SerializedName("Title" )
    var Title : String?,
    @SerializedName("Year" )
    var Year : String?,
    @SerializedName("Rated" )
    var Rated : String?,
    @SerializedName("Released" )
    var Released : String?,
    @SerializedName("Runtime" )
    var Runtime : String?,
    @SerializedName("Genre" )
    var Genre : String?,
    @SerializedName("Director" )
    var Director : String?,
    @SerializedName("Writer" )
    var Writer : String?,
    @SerializedName("Actors" )
    var Actors : String?,
    @SerializedName("Plot" )
    var Plot : String?,
    @SerializedName("Language" )
    var Language : String?,
    @SerializedName("Country" )
    var Country : String?,
    @SerializedName("Awards" )
    var Awards : String?,
    @SerializedName("Poster" )
    var Poster : String?,
    @SerializedName("Ratings" )
    var Ratings : ArrayList<Ratings> = arrayListOf(),
    @SerializedName("Metascore" )
    var Metascore : String?,
    @SerializedName("imdbRating" )
    var imdbRating : String?,
    @SerializedName("imdbVotes" )
    var imdbVotes : String?,
    @SerializedName("imdbID" )
    var imdbID : String?,
    @SerializedName("Type" )
    var Type : String?,
    @SerializedName("DVD" )
    var DVD : String?,
    @SerializedName("BoxOffice" )
    var BoxOffice : String?,
    @SerializedName("Production" )
    var Production : String?,
    @SerializedName("Website" )
    var Website : String?,
    @SerializedName("Response" )
    var Response : String?
)