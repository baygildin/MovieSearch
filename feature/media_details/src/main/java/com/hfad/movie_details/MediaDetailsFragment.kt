package com.hfad.media_details


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hfad.core.BaseFragment
import com.hfad.media_details.databinding.FragmentMediaDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MediaDetailsFragment : BaseFragment(R.layout.fragment_media_details) {

    private var _binding: FragmentMediaDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MediaDetailsFragmentArgs by navArgs()
    private val viewModel: MediaDetailsViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        val shimmerFrameLayout = binding.shimmerFrame
        val realContent = binding.realContent
        val chosenMovieId = args.id
        viewModel.fetchMediaDetails(chosenMovieId)
        shimmerFrameLayout.startShimmer()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mediaDetails.collect { result ->
                result?.fold(
                    onSuccess = {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .withEndAction {
                                shimmerFrameLayout.visibility = View.GONE
                                realContent.apply {
                                    alpha = 0f
                                    visibility = View.VISIBLE
                                    animate()
                                        .alpha(1f)
                                        .setDuration(100)
                                        .start()
                                }
                            }
                            .start()
                        Glide.with(requireContext())
                            .load(it.poster)
                            .into(binding.ivPoster)

                        binding.tvMediaInfoBodyTitle.text = it.title
                        binding.tvMediaInfoBodyImdb.text = it.imdbRating
                        binding.tvMediaInfoBodyYear.text = it.year
                        binding.tvMediaInfoBodyDirector.text = it.director
                        binding.tvMediaInfoBodyActors.text = it.actors
                        binding.tvMediaInfoBodyPlot.text = it.plot
                        binding.tvMediaInfoBodyAwards.text = it.awards
                        binding.tvEpisodeInfoBodyGenre.text = it.genre

                        if (it.type == "series") binding.btnShowEpisodes.visibility =
                            View.VISIBLE
                    },
                    onFailure = {
                        Log.e("MyError42", "$it in viewModel.mediaDetails.collect")

                    }
                )
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFavourite.collect{ it ->
                Log.d("MediaDetailsFragment", "isFavourite:ssssss $it")
                binding.likeButtonHeart.setImageResource(
                    if (it) R.drawable.heart_red_in_circle
                    else R.drawable.heart_black
                )
            }
        }

        binding.btnShowEpisodes.setOnClickListener {
            (activity as com.hfad.navigation.Navigator).navigateMediaDetailsToShowSeasonsWithId(
                chosenMovieId
            )
        }

        binding.ivPoster.setOnClickListener {
            (activity as com.hfad.navigation.Navigator).navigateMediaDetailsToPosterWithId(
                chosenMovieId
            )
        }
        binding.likeButtonHeart.setOnClickListener{
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.mediaDetails.collect { result ->
                    result?.onSuccess {
                        val unixTimeSeconds: Long =
                            TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                        val unixTimeString = unixTimeSeconds.toString()
                        viewModel.toggleFavourite(
                            it.imdbID,
                            it.title,
                            it.year,
                            unixTimeString
                        )
                    }
                }
            }
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}