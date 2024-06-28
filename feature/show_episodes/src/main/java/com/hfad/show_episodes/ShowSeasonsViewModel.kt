package com.hfad.show_episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.search.model.SearchResponseBySeason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShowSeasonsViewModel @Inject constructor(
    private val repository: ShowSeasonsRepository
) : ViewModel() {

    private val _seasons = MutableLiveData<Result<SearchResponseBySeason>>()
    val seasons: LiveData<Result<SearchResponseBySeason>> = _seasons

    fun fetchSeasons(id: String) {
        viewModelScope.launch {
            val result = repository.getSeasonsByIdAndSeasons(id)
            _seasons.value = result
            result.onSuccess {
                Log.d("myError42", "watch in fetchSeasons")
            }
            result.onFailure {
                Log.e("myError", "fetchSeasons failure")
            }
        }
    }
}