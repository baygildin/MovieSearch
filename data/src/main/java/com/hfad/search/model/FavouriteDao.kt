package com.hfad.search.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavourite(item: FavouriteItem)

    @Delete
    suspend fun removeFavourite(item: FavouriteItem)

    @Update
    suspend fun updateFavourite(item: FavouriteItem)

    @Query("SELECT * FROM favourites")
    fun getAllFavourites(): Flow<List<FavouriteItem>>

    @Query("SELECT * FROM favourites WHERE imdbId = :imdbId")
    suspend fun getFavouriteByImdbId(imdbId: String): FavouriteItem

    @Query("DELETE FROM favourites")
    suspend fun clearFavourites()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favourites: List<FavouriteItem>)
}
