package com.hfad.search.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavourite(item: FavouriteItem)

    @Delete
    suspend fun removeFavourite(item: FavouriteItem)
    @Update
    suspend fun updateFavourite(item: FavouriteItem)

    @Query("SELECT * FROM favorites")
    fun getAllFavourites(): LiveData<List<FavouriteItem>>

    @Query("SELECT * FROM favorites WHERE imdbId = :imdbId")
    fun getFavouriteByImdbId(imdbId: String): LiveData<FavouriteItem>
}
