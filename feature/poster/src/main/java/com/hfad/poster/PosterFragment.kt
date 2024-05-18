package com.hfad.poster


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hfad.poster.databinding.FragmentPosterBinding
import com.hfad.search.OmdbApi
import com.hfad.search.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PosterFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    @Inject
    lateinit var omdbApi: OmdbApi
    private var _binding: FragmentPosterBinding? = null
    private val args: PosterFragmentArgs by navArgs<PosterFragmentArgs>()
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in FragmentPosterBinding of FragmentPoster must not be null")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPosterBinding.inflate(inflater, container, false)
        val view = binding.root
        var chosenMovieId = args.id
        viewModel.movieTitle.observe(viewLifecycleOwner) { title ->
            Log.e("getmovieError","poster fragment.. get image poster original")
            lifecycleScope.launch {
                try {
                    Log.e("findError", "44 str")
                    val result = omdbApi.searchById(chosenMovieId)
                    Glide.with(requireContext())
                        .load(result.poster)
                        .into(binding.myZoomageView)
                } catch (e: Exception) {
                    Log.e("getmovieError", "$e poster fragment.. get image poster original after nav")
                }


            }
        }
        Log.e("findError", "41 str")

        return view
    }
}