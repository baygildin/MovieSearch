package com.hfad.mytestapp.app.movie_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hfad.mytestapp.R
import android.widget.Button
import androidx.navigation.findNavController

class MovieDetailsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val startButton = view.findViewById<Button>(R.id.poster_to_maxSize_button)
        startButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_movieDetailsFragment_to_posterFragment)
        }
        return view
    }
}