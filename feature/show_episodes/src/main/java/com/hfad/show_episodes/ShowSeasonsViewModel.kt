package com.hfad.show_episodes

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
class ShowSeasonsViewModel @Inject constructor(
    private val repository: ShowSeasonsRepository
) : ViewModel() {

    private val _seasons = MutableStateFlow<Result<SearchResponseBySeason>?>(null)
    val seasons: StateFlow<Result<SearchResponseBySeason>?> = _seasons

    fun fetchSeasons(id: String) {
        viewModelScope.launch {
            flow {
                try {
                    val result = repository.getSeasonsByIdAndSeasons(id)
                    emit(Result.success(result))
                } catch (e: Exception) {
                    emit(Result.failure < SearchResponseBySeason>(e))
                }
            }
                .collect { result ->
                    _seasons.value = result
                }
        }
    }
}