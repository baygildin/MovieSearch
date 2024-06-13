package com.hfad.show_episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.search.OmdbApi
import com.hfad.search.model.SearchResponseEpisodeFullInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowEpisodeViewModel @Inject constructor(
    private val omdbApi: OmdbApi
) : ViewModel() {

    private val _episode = MutableLiveData<Result<SearchResponseEpisodeFullInfo>>()
    val episode: LiveData<Result<SearchResponseEpisodeFullInfo>> = _episode

    fun fetchEpisode(title: String, seasonNumber: Int, episodeNumber: Int) {
        viewModelScope.launch {
            try {
                val result = omdbApi.searchByEpisode(title, seasonNumber.toString(), episodeNumber.toString())
                _episode.value = Result.success(result)
            } catch (e: Exception) {
                _episode.value = Result.failure(e)
            }
        }
    }
}
