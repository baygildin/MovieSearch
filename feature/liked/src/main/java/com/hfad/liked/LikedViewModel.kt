package com.hfad.liked

import androidx.lifecycle.ViewModel
import com.hfad.search.model.FavouriteDao
import com.hfad.search.model.FavouriteItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LikedViewModel @Inject constructor(
    private val favouriteDao: FavouriteDao
) : ViewModel() {
    git add .

    val favouriteItems: Flow<List<FavouriteItem>> = favouriteDao.getAllFavourites()
        .map { items -> items.sortedByDescending { it.dateAdded } }
    val favouriteItemsByName: Flow<List<FavouriteItem>> = favouriteDao.getAllFavourites()
        .map { items -> items.sortedBy{it.title}}
}
