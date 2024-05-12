package com.hfad.search.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavouriteItem::class], version = 1)
abstract class FavouriteDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteDatabase? = null

        fun getDatabase(context: Context): FavouriteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavouriteDatabase::class.java,
                    "favorite_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

//fun getInstance(context: Context): TaskDatabase {
//    synchronized(this) {
//        var instance = INSTANCE
//        if (instance == null) {
//            instance = Room.databaseBuilder(
//                context.applicationContext,
//                TaskDatabase::class.java,
//                "tasks_database"
//            ).build()
//            INSTANCE = instance
//        }
//        return instance
//    }
//}