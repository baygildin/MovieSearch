package com.hfad.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.search.model.SearchBySearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val omdbApi: OmdbApi
) : ViewModel() {

    private val _searchResults = MutableStateFlow<Result<SearchBySearch>?>(null)
    val searchResults: StateFlow<Result<SearchBySearch>?> = _searchResults

    fun searchMediaWithTitle(title: String) {
        viewModelScope.launch {
            flow {
                val result = omdbApi.searchByTitle(title)
                emit(Result.success(result))
            }
                .catch { e ->
                    emit(Result.failure(e))
                    Log.d("myerror42", "searchMediaWithTitle error", e)
                }
                .collect { result ->
                    _searchResults.value = result
                }
        }
    }
}

