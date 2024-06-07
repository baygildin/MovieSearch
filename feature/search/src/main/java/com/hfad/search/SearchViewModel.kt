package com.hfad.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.search.model.SearchBySearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val omdbApi: OmdbApi
) : ViewModel() {

    private val _searchResults = MutableLiveData<Result<SearchBySearch>>()
    val searchResults: LiveData<Result<SearchBySearch>> = _searchResults

    fun searchMediaWithTitle(title: String) {
        viewModelScope.launch {
            try {
                val result = omdbApi.searchByTitle(title)
                _searchResults.postValue(Result.success(result))
            } catch (e: Exception) {
                _searchResults.postValue(Result.failure(e))
                Log.d("myerror42", "searchMediaWithTitle")
            }
        }
    }
}
