package com.hfad.movie_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.hfad.movie_details.databinding.FragmentMovieDetailsBinding
import com.hfad.search.OmdbApi
import com.hfad.search.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentMovieDetailsBinding? = null

    @Inject
    lateinit var omdbApi: OmdbApi
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in MovieDetailsBinding of MovieDetailsFragment must not be null")
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        var chosenMovieId = ""
        viewModel.movieTitle.observe(viewLifecycleOwner) { title ->
            chosenMovieId = title

            binding.tvDetailsScreen.text = chosenMovieId
            Log.e("getmovieError","chosenMovieId = title$chosenMovieId")
            lifecycleScope.launch {
                try {
                    Log.e("findError", "44 str")
                    val result = omdbApi.searchById(chosenMovieId)
                    Glide.with(requireContext())
                        .load(result.poster) // Предполагаемый URL изображения
                        .into(binding.ivPoster)
                    val title = "Title: "
                    val actors = "Actors: "
                    val plot = "Plot:\n"
                    val imdbRating = "IMDB rating: "
                    val movieYear = "Year: "
                    val awards = "Awards: "

                    val text = SpannableStringBuilder()
                        .bold { append(title) }
                        .append(result.title+"\n")
                        .bold { append(imdbRating)}
                        .append(result.imdbRating+"\n")
                        .bold { append(movieYear) }
                        .append(result.year + "\n")
                        .bold { append(actors) }
                        .append(result.actors+"\n")
                        .bold { append(plot) }
                        .append(result.plot + "\n")
                        .bold { append(awards) }
                        .append(result.awards)

                    binding.tvDetailsScreen.text = text

                    binding.ivPoster.setOnClickListener {
                        val request = NavDeepLinkRequest.Builder
                            .fromUri("android-app://com.hfad.poster/PosterFragment".toUri())
                            .build()
                        findNavController().navigate(request)
                    }

                } catch (e: Exception) {
                    Log.e(
                        "getmovieError",
                        "$e в строчке viewModel.movieTitle.observe(viewLifecycleOwner) { title -> chosenMovieId = titlebinding.tvFromSearchText.text= chosenMovieIdbinding.tvDetailsScreen"
                    )
                }


            }
        }
        Log.e("findError", "41 str")
        return view
    }
}