package com.hfad.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController

class SearchFragment : Fragment() {
    lateinit var viewModel: SearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        //viewModel с провайдером потому что это начальная страница, и класс не будет имееть параметров.
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        val startButton = view.findViewById<Button>(R.id.start_search)
        val episodesListButton = view.findViewById<Button>(R.id.btn_episodes_list)

        startButton.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://com.hfad.movie_details/movieDetailsFragment".toUri())
                .build()
            findNavController().navigate(request)
        }
        episodesListButton.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://com.hfad.show_episodes/ShowEpisodesFragment".toUri())
                .build()
            findNavController().navigate(request)
        }
        return view
    }
}
