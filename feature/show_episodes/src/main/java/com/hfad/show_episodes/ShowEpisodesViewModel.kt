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
class ShowEpisodesViewModel @Inject constructor(
    val repository: ShowEpisodesRepository
) : ViewModel() {

    private val _episodes = MutableLiveData<Result<SearchResponseBySeason>>()
    val episodes: LiveData<Result<SearchResponseBySeason>> = _episodes

    fun fetchEpisodes(title: String, seasonNumber: String) {
        viewModelScope.launch {
            val result = repository.getEpisodesByTitleAndSeasonNumber(title, seasonNumber)
            _episodes.value = result
            result.onSuccess {
                Log.d("myError42", "ok fetchEpisodes")
            }
            result.onFailure {
                Log.e("myError42", "e in fetchepisodes ")
            }
        }

    }
}
