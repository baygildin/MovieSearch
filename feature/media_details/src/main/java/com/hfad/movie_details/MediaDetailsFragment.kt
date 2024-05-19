package com.hfad.media_details


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hfad.media_details.databinding.FragmentMediaDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaDetailsFragment : Fragment() {

    private var _binding: FragmentMediaDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MediaDetailsFragmentArgs by navArgs()
    private val viewModel: MediaDetailsViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMediaDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        val chosenMovieId = args.id
        viewModel.fetchMediaDetails(chosenMovieId)

        viewModel.mediaDetails.observe(viewLifecycleOwner) { result ->
            Glide.with(requireContext())
                .load(result.poster)
                .into(binding.ivPoster)

            val text = SpannableStringBuilder()
                .bold { append("Title: ") }
                .append(result.title + "\n")
                .bold { append("IMDB rating: ") }
                .append(result.imdbRating + "\n")
                .bold { append("Year: ") }
                .append(result.year + "\n")
                .bold { append("Director: ") }
                .append(result.director + "\n")
                .bold { append("Actors: ") }
                .append(result.actors + "\n")
                .bold { append("Plot:\n") }
                .append(result.plot + "\n")
                .bold { append("Awards: ") }
                .append(result.awards + "\n")

            binding.tvDetailsScreen.text = text
            if (result.type == "series") binding.btnShowEpisodes.visibility = View.VISIBLE
        }

        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            binding.likeButtonHeart.setImageResource(
                if (isFavourite) R.drawable.heart_red_in_circle
                else R.drawable.heart_black
            )
        }

        binding.btnShowEpisodes.setOnClickListener {
            (activity as com.hfad.navigation.Navigator).navigateMediaDetailsToShowEpisodesWithId(
                chosenMovieId
            )
        }

        binding.ivPoster.setOnClickListener {
            (activity as com.hfad.navigation.Navigator).navigateMediaDetailsToPosterWithId(
                chosenMovieId
            )
        }

        binding.likeButtonHeart.setOnClickListener {
            viewModel.toggleFavourite(chosenMovieId)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}