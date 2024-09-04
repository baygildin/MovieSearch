package com.hfad.liked

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hfad.search.firebase.FirebaseRepository
import com.hfad.search.model.FavouriteDao
import com.hfad.search.model.FavouriteItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LikedViewModel @Inject constructor(
    private val favouriteDao: FavouriteDao,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    var doesUserAgreeToReplaceFromCloud = false
    var doesUserAgreeToSendToCloud = false

    val favouriteItems: Flow<List<FavouriteItem>> = favouriteDao.getAllFavourites()
        .map { items -> items.sortedByDescending { it.dateAdded } }
    val favouriteItemsByName: Flow<List<FavouriteItem>> = favouriteDao.getAllFavourites()
        .map { items -> items.sortedBy { it.title } }


    suspend fun getFavouritesJson(): String {
        val favouritesList = favouriteDao.getAllFavourites().first()
        return Gson().toJson(favouritesList)
    }

    suspend fun updateRoomDatabaseFromJson(jsonData: String): Unit {
        val favouritesList = convertJsonToFavouritesList(jsonData)
        favouriteDao.clearFavourites()
        favouriteDao.insertAll(favouritesList)
    }

    private fun convertJsonToFavouritesList(jsonData: String): List<FavouriteItem> {
        return Gson().fromJson(jsonData, Array<FavouriteItem>::class.java).toList()
    }

    fun saveFavouritesToClouds(jsonData: String) {
        firebaseRepository.saveFavouritesToCloud(jsonData,
            onSuccess = {

            },
            onFailure = {
                Log.d("s", "ddd")
            })
    }

    fun fetchFavouritesFromCloud(onSucces: (String) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseRepository.fetchFavouritesFromCloud(onSucces, onFailure)
    }
}
