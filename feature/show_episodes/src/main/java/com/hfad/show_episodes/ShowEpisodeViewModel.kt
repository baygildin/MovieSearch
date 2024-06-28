package com.hfad.show_episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.search.model.SearchResponseEpisodeFullInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowEpisodeViewModel @Inject constructor(
    private val repository: ShowEpisodeRepository
) : ViewModel() {

    private val _episode = MutableLiveData<Result<SearchResponseEpisodeFullInfo>>()
    val episode: LiveData<Result<SearchResponseEpisodeFullInfo>> = _episode

    fun fetchEpisode(title: String, seasonNumber: String, episodeNumber: String) {
        viewModelScope.launch {
            val result = repository.getEpisodeByTitleAndSeasonAndEpisodeNumber(
                title,
                seasonNumber,
                episodeNumber
            )
            _episode.value = result
            result.onSuccess {
                Log.d("myError42", "watch in fetchEpisode")
            }
            result.onFailure { Log.d("myError42", "watch e in fetchEpisode") }
        }
    }
}
