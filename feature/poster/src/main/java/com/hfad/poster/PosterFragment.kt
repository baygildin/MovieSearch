package com.hfad.poster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hfad.poster.databinding.FragmentPosterBinding

class PosterFragment : Fragment() {

    private var _binding: FragmentPosterBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in FragmentPosterBinding of FragmentPoster must not be null")


    private val viewModel: PosterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPosterBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }
}