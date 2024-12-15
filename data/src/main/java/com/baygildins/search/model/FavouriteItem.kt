package com.baygildins.search.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "imdbId")
    val imdbId: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "year")
    val year: String,
    @ColumnInfo(name = "dateAdded")
    val dateAdded: String
)