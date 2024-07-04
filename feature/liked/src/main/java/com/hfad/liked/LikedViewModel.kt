package com.hfad.liked

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hfad.search.model.FavouriteDao
import com.hfad.search.model.FavouriteItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LikedViewModel @Inject constructor(
    private val favouriteDao: FavouriteDao
) : ViewModel() {

    val favouriteItems: Flow<List<FavouriteItem>> = favouriteDao.getAllFavourites()
        .map { items -> items.sortedByDescending { it.dateAdded } }
    val favouriteItemsByName: Flow<List<FavouriteItem>> = favouriteDao.getAllFavourites()
        .map { items -> items.sortedBy{it.title}}



    fun getFavouritesJson(): String = runBlocking {
        val favouritesList = favouriteDao.getAllFavourites().first()
        Log.d("mymy", "in VM  ${favouritesList}")
        Gson().toJson(favouritesList)
    }

    fun convertFavouritesJsonToDataClass(): String = runBlocking {
        val favouritesList = favouriteDao.getAllFavourites().first()
        Log.d("mymy", "in VM  ${favouritesList}")
        Gson().toJson(favouritesList)
        TODO()
    }


}
