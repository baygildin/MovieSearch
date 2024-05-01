package com.hfad.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import android.widget.TextView
import androidx.lifecycle.Observer

class SearchFragment : Fragment() {
    lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        //viewModel с провайдером потому что это начальная страница, и класс не будет имееть параметров.
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

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

        searchViewModel._searchText.observe(viewLifecycleOwner, Observer { newText ->
            // Обновляем TextView с новым текстом
            view?.findViewById<TextView>(R.id._searchText)?.text = newText
        })
        return view
    }
}
