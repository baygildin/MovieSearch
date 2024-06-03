package com.hfad.liked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hfad.search.model.FavouriteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LikedViewModel @Inject constructor(
    private val favouriteDao: FavouriteDao
) : ViewModel() {

    val favouriteItems = favouriteDao.getAllFavourites().asLiveData()
}
