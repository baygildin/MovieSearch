package com.hfad.search.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavouriteItem::class], version = 1)//, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {
   abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteDatabase? = null

        fun getDatabase(context: Context): FavouriteDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                FavouriteDatabase::class.java,
                "favourites.db"
            ).build()
        }
    }
}