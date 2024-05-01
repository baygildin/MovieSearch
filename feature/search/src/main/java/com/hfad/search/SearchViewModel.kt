package com.hfad.search

import OmdbApiService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.network.RetrofitClient
import javax.inject.Inject

class SearchViewModel : ViewModel() {

    private val _searchText = MutableLiveData<String>()
    fun setSearchText(text: String) {
        _searchText.value = text
    }

    fun getSearchText(): LiveData<String> {
        return _searchText
    }

    @Inject
    lateinit var apiService: OmdbApiService

    init {
        // Initialize apiService using RetrofitClient
        apiService = RetrofitClient.retrofit.create(OmdbApiService::class.java)

        // Perform your API call here, maybe in a method like fetchData()
        fetchData()
    }

    private fun fetchData() {
        val movieResponse = apiService.searchByTitle("friends", "1999")
        if (movieResponse.Response == "True") {
            val movie = movieResponse
        } else {
            val movie = "херакнулось"
        }
    }
}
