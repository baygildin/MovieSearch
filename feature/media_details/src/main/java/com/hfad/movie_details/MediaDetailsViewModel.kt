package com.hfad.media_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.search.OmdbApi
import com.hfad.search.model.FavouriteDao
import com.hfad.search.model.FavouriteItem
import com.hfad.search.model.SearchResponseById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val omdbApi: OmdbApi,
    private val favouriteDao: FavouriteDao
) : ViewModel() {

    private val _mediaDetails = MutableLiveData<SearchResponseById>()
    val mediaDetails: LiveData<SearchResponseById> get() = _mediaDetails

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> get() = _isFavourite

    fun fetchMediaDetails(mediaId: String) {
        viewModelScope.launch {
            try {
                val result = omdbApi.searchById(mediaId)
                _mediaDetails.value = result

                val existingFavourite = favouriteDao.getFavouriteByImdbId(mediaId)
                _isFavourite.value = existingFavourite != null
            } catch (e: Exception) {
                Log.d("myError42", "in fun fetch media")
            }
        }
    }

    fun toggleFavourite(mediaId: String) {
        viewModelScope.launch {
            try {
                val existingFavourite = favouriteDao.getFavouriteByImdbId(mediaId)
                if (existingFavourite != null) {
                    favouriteDao.removeFavourite(existingFavourite)
                    _isFavourite.value = false
                } else {
                    val favouriteItem = FavouriteItem(null, mediaId)
                    favouriteDao.addFavourite(favouriteItem)
                    _isFavourite.value = true
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
