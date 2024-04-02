package com.hfad.mytestapp.app.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hfad.mytestapp.R
import android.widget.Button
import androidx.navigation.findNavController


class SearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val startButton = view.findViewById<Button>(R.id.start_search)
        startButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_searchFragment_to_movieDetailsFragment)

        }
        return view
    }
}