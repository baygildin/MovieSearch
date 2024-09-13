package com.hfad.media_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.movie_details.MediaDetailsDbRepository
import com.hfad.movie_details.MediaDetailsOmdbRepository
import com.hfad.search.model.SearchResponseById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(

    private val dbRepository: MediaDetailsDbRepository,
    private val omdbRepository: MediaDetailsOmdbRepository

) : ViewModel() {

    private val _mediaDetails = MutableStateFlow<Result<SearchResponseById>?>(null)
    val mediaDetails: StateFlow<Result<SearchResponseById>?> get() = _mediaDetails

    private val _isFavourite = MutableStateFlow(false)
    val isFavourite: StateFlow<Boolean> get() = _isFavourite


    fun fetchMediaDetails(mediaId: String) {
        viewModelScope.launch {
            flow {
                try {
                    val result = omdbRepository.getMediaInfoById(mediaId)
                    _isFavourite.value = checkIsMediaFavorite(mediaId)
                    emit(Result.success(result))
                } catch (e: Exception) {
                    emit(Result.failure<SearchResponseById>(e))
                }
            }
                .collect { result ->
                    _mediaDetails.value = result
                }
        }
    }

    private suspend fun checkIsMediaFavorite(mediaId: String): Boolean {
        return dbRepository.checkIsMediaFavorite(mediaId)

    }

    fun toggleFavourite(mediaId: String, title: String, year: String, dateadded: String) {
        viewModelScope.launch {

            _isFavourite.value = dbRepository.makeItFavorite(mediaId, title, year, dateadded)

        }
    }


}