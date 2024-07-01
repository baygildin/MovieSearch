package com.hfad.show_episodes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.search.model.SearchResponseBySeason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowEpisodesViewModel @Inject constructor(
    private val repository: ShowEpisodesRepository
) : ViewModel() {

    private val _episodes = MutableStateFlow<Result<SearchResponseBySeason>?>(null)
    val episodes: StateFlow<Result<SearchResponseBySeason>?> = _episodes

    fun fetchEpisodes(title: String, seasonNumber: String) {
        viewModelScope.launch {
            flow {
                try {
                    val result = repository.getEpisodesByTitleAndSeasonNumber(title, seasonNumber)
                    emit(Result.success(result))
                } catch (e: Exception) {
                    emit(Result.failure<SearchResponseBySeason>(e))
                    Log.e("MyError42", "$e in catch in fetchEpisode in showepisodeVM")
                }
            }
                .collect { result ->
                    _episodes.value = result
                }
        }
    }
}

