package com.hfad.movie_details

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import com.hfad.movie_details.R
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController


class MovieDetailsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val startButton = view.findViewById<Button>(R.id.poster_to_maxSize_button)

        startButton.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://com.hfad.poster/PosterFragment".toUri())
                .build()
            findNavController().navigate(request)
        }
        return view
    }
}