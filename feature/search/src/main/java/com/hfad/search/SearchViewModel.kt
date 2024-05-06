package com.hfad.search


import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    var searchText = ""
}
//class SearchViewModel : ViewModel() {
//    private val movieRepository = RetrofitClient.service
//
//    fun getMovie(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
//        viewModelScope.launch {
//            try {
//                val movie = movieRepository.getMovie()
//                withContext(Dispatchers.Main) {
//                    onSuccess.invoke(movie.toString())
//                }
//            } catch (e: Exception) {
//                onError.invoke("Failed to get movie")
//            }
//        }
//    }
//}


//GlobalScope.launch(Dispatchers.IO) {
//    try {
//        val movie = RetrofitClient.service.getMovie()
//        withContext(Dispatchers.Main) {
//            movieTitleTextView.text = movie.toString()
//            Log.d("messatrh", "${movie.Title}")
//        }
//    } catch (e: Exception) {
//        Log.e("MainActivity", "Failed to get movie", e)
//    }
//}