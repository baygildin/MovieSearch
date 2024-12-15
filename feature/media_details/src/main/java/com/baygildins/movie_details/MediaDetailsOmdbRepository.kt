package com.baygildins.movie_details

import androidx.lifecycle.ViewModel
import com.baygildins.search.model.SearchResponseById
import com.baygildins.search.network.OmdbApi
import javax.inject.Inject

class MediaDetailsOmdbRepository @Inject constructor(
    private val omdbApi: OmdbApi,
) : ViewModel() {
    suspend fun getMediaInfoById(mediaId: String)
            : SearchResponseById {
        return omdbApi.searchById(mediaId)
    }
}