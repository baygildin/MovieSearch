package com.hfad.media_details


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hfad.core.BaseFragment
import com.hfad.media_details.databinding.FragmentMediaDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MediaDetailsFragment : BaseFragment(R.layout.fragment_media_details) {

    private var _binding: FragmentMediaDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MediaDetailsFragmentArgs by navArgs()
    private val viewModel: MediaDetailsViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMediaDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        val shimmerFrameLayout = binding.shimmerFrame
        val realContent = binding.realContent
        val chosenMovieId = args.id

        viewModel.fetchMediaDetails(chosenMovieId)
        shimmerFrameLayout.startShimmer()

        viewModel.mediaDetails.observe(viewLifecycleOwner) { result ->
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
                .load(result.poster)
                .into(binding.ivPoster)

            binding.tvMediaInfoBodyTitle.text=result.title
            binding.tvMediaInfoBodyImdb.text=result.imdbRating
            binding.tvMediaInfoBodyYear.text=result.year
            binding.tvMediaInfoBodyDirector.text=result.director
            binding.tvMediaInfoBodyActors.text=result.actors
            binding.tvMediaInfoBodyPlot.text=result.plot
            binding.tvMediaInfoBodyAwards.text=result.awards
            binding.tvEpisodeInfoBodyGenre.text=result.genre

            if (result.type == "series") binding.btnShowEpisodes.visibility = View.VISIBLE
        }

        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            binding.likeButtonHeart.setImageResource(
                if (isFavourite) R.drawable.heart_red_in_circle
                else R.drawable.heart_black
            )
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

        binding.likeButtonHeart.setOnClickListener {
            viewModel.mediaDetails.observe(viewLifecycleOwner) { result ->
                val unixTimeSeconds: Long =
                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                val unixTimeString = unixTimeSeconds.toString()
                viewModel.toggleFavourite(chosenMovieId, result.title, result.year, unixTimeString)
            }

        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}