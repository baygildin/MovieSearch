package com.hfad.network

import com.google.gson.annotations.SerializedName

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