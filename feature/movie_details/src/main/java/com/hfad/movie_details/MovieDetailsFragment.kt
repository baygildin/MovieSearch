package com.hfad.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.hfad.movie_details.databinding.FragmentMovieDetailsBinding
import com.hfad.search.SearchViewModel


class MovieDetailsFragment : Fragment() {
    lateinit var viewModel: SearchViewModel
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in MovieDetailsBinding of MovieDetailsFragment must not be null")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding) {
            btnPosterToMaxSize.setOnClickListener {
                val request = NavDeepLinkRequest.Builder
                    .fromUri("android-app://com.hfad.poster/PosterFragment".toUri())
                    .build()
                findNavController().navigate(request)
            }
            tvFromSearchText.text = viewModel.searchText

        }
        return view
    }
}