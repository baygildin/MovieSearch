package com.hfad.movie_details

import androidx.lifecycle.ViewModel
import com.hfad.search.network.OmdbApi
import com.hfad.search.model.SearchResponseById
import javax.inject.Inject

class MediaDetailsOmdbRepository @Inject constructor(
    private val omdbApi: OmdbApi,
) : ViewModel() {
    suspend fun getMediaInfoById(mediaId: String)
            : SearchResponseById {
        return omdbApi.searchById(mediaId)
    }
}