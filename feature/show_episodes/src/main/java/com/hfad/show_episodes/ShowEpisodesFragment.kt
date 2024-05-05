package com.hfad.show_episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.hfad.show_episodes.databinding.FragmentShowEpisodesBinding


class ShowEpisodesFragment : Fragment() {
    private var _binding: FragmentShowEpisodesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in FragmentShowEpisodesBinding of FragmentShowEpisodes Fragment must not be null")



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_show_episodes, container, false)
        _binding = FragmentShowEpisodesBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnDetailsScreen.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://com.hfad.movie_details/movieDetailsFragment".toUri())
                .build()
            findNavController().navigate(request)
        }

        return view

    }
}
