package com.hfad.show_episodes

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
class ShowEpisodesViewModel @Inject constructor(
    private val omdbApi: OmdbApi
) : ViewModel() {

    private val _episodes = MutableLiveData<Result<SearchResponseBySeason>>()
    val episodes: LiveData<Result<SearchResponseBySeason>> = _episodes

    fun fetchEpisodes(title: String, seasonNumber: Int) {
        viewModelScope.launch {
            try {
                val result = omdbApi.searchBySeason(title, seasonNumber.toString())
                _episodes.value = Result.success(result)
            } catch (e: Exception) {
                _episodes.value = Result.failure(e)
            }
        }
    }
}
