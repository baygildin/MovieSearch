package com.hfad.search.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class FavouriteItem(
    @PrimaryKey val imdbId: String
)