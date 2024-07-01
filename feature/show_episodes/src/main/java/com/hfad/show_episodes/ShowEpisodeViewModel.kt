package com.hfad.show_episodes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.search.model.SearchResponseEpisodeFullInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowEpisodeViewModel @Inject constructor(
    private val repository: ShowEpisodeRepository
) : ViewModel() {

    private val _episode = MutableStateFlow<Result<SearchResponseEpisodeFullInfo>?>(null)
    val episode: StateFlow<Result<SearchResponseEpisodeFullInfo>?> = _episode

    fun fetchEpisode(title: String, seasonNumber: String, episodeNumber: String) {
        viewModelScope.launch {
            flow {
                try {
                    val result = repository.getEpisodeByTitleAndSeasonAndEpisodeNumber(
                        title,
                        seasonNumber,
                        episodeNumber
                    )
                    emit(Result.success(result))
                } catch (e: Exception) {
                    emit(Result.failure < SearchResponseEpisodeFullInfo>(e))
                }
            }
                .collect { result ->
                    _episode.value = result
                }
        }
    }
}