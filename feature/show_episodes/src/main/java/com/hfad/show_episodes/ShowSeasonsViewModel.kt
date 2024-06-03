package com.hfad.show_episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.search.OmdbApi
import com.hfad.search.model.SearchResponseBySeason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowSeasonsViewModel @Inject constructor(
    private val omdbApi: OmdbApi
) : ViewModel() {

    private val _seasons = MutableLiveData<Result<SearchResponseBySeason>>()
    val seasons: LiveData<Result<SearchResponseBySeason>> = _seasons

    fun fetchSeasons(id: String) {
        Log.d("nowornever", "seasons vm ${id}")
        viewModelScope.launch {
            try {
                val result = omdbApi.searchSeasonByIdAndSeason(id, "1")
                _seasons.value = Result.success(result)
                Log.d("nowornever", "seasons vm try ${result.title}")
            } catch (e: Exception) {
                _seasons.value = Result.failure(e)
            }
        }
    }
}