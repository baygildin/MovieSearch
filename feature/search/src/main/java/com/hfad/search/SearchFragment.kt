package com.hfad.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.hfad.search.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var viewModel: SearchViewModel
    @Inject
    lateinit var omdbApi: OmdbApi

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in FragmentSearchBinding of SearchFragment must not be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //viewModel с провайдером потому что это начальная страница, и класс не будет имееть параметров.
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
//        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val view = binding.root

        with(binding)
        {
            searchTextView.text = "ssssss"
            btnStartSearch.setOnClickListener {
                val request = NavDeepLinkRequest.Builder
                    .fromUri("android-app://com.hfad.movie_details/movieDetailsFragment".toUri())
                    .build()
                findNavController().navigate(request)}
            btnEpisodesList.setOnClickListener{
                val request = NavDeepLinkRequest.Builder
                    .fromUri("android-app://com.hfad.show_episodes/ShowEpisodesFragment".toUri())
                    .build()
                findNavController().navigate(request)
            }
        }




        lifecycleScope.launch {
            omdbApi.searchByTitle("terminator")
        }


//        val movieTitleTextView = view.findViewById<TextView>(R.id.movie_title_textview)
//
//
//
//        viewModel.getMovie(
//            onSuccess = { movieTitle ->
//                movieTitleTextView.text = movieTitle
//                Log.d("getmovieError", movieTitle)
//            },
//            onError = { errorMessage ->
//                Log.e("getmovieError", errorMessage)
//            }
//        )

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val movie = RetrofitClient.service.getMovie()
//                withContext(Dispatchers.Main) {
//                    movieTitleTextView.text = movie.toString()
//                    Log.d("messatrh", "${movie.Title}")
//                }
//            } catch (e: Exception) {
//                Log.e("MainActivity", "Failed to get movie", e)
//            }
//        }