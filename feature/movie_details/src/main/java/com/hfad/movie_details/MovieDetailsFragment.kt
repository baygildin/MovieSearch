package com.hfad.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.hfad.movie_details.databinding.FragmentMovieDetailsBinding


class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in MovieDetailsBinding of MovieDetailsFragment must not be null")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding) {
            btnPosterToMaxSize.setOnClickListener {
                val request = NavDeepLinkRequest.Builder
                    .fromUri("android-app://com.hfad.poster/PosterFragment".toUri())
                    .build()
                findNavController().navigate(request)
            }
        }
        return view
    }
}