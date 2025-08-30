package com.baygildins.movie_details

import androidx.lifecycle.ViewModel
import com.baygildins.search.model.FavouriteDao
import com.baygildins.search.model.FavouriteItem
import javax.inject.Inject

class MediaDetailsDbRepository @Inject constructor(
    private val favouriteDao: FavouriteDao,
) : ViewModel() {
    suspend fun makeItFavorite(
        mediaId: String,
        title: String,
        year: String,
        dateadded: String
    ): Boolean {
        return if (checkIsMediaFavorite(mediaId)) {
            favouriteDao.removeFavourite(favouriteDao.getFavouriteByImdbId(mediaId))
            false
        } else {
            val favouriteItem = FavouriteItem(null, mediaId, title, year, dateadded)
            favouriteDao.addFavourite(favouriteItem)
            true
        }
    }

    suspend fun checkIsMediaFavorite(mediaId: String): Boolean {
        val existingFavourite: FavouriteItem = favouriteDao.getFavouriteByImdbId(mediaId)
        return existingFavourite != null
    }
}