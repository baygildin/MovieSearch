package com.hfad.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.search.model.SearchBySearch
import dagger.hilt.android.AndroidEntryPoint



class SharedViewModel : ViewModel() {


    var searchText =""

    private val _movieTitle = MutableLiveData<String>()
    val movieTitle: LiveData<String> = _movieTitle
    fun setMovieTitle(title: String) {
        _movieTitle.value = title
    }
    lateinit var result: SearchBySearch
}