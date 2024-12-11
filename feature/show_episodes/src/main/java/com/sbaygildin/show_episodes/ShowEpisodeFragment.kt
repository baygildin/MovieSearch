package com.sbaygildin.show_episodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sbaygildin.core.BaseFragment
import com.sbaygildin.show_episodes.databinding.FragmentShowEpisodeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowEpisodeFragment : BaseFragment(R.layout.fragment_show_episode) {

    private var _binding: FragmentShowEpisodeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShowEpisodeViewModel by viewModels()
    private val args: ShowEpisodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowEpisodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shimmerFrameLayout = binding.shimmerFrame
        val realContent = binding.realContent
        shimmerFrameLayout.startShimmer()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.episode.collect { result ->
                result?.fold(
                    onSuccess = { episode ->
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
                        binding.tvEpisodeInfoBodyTitle.text = episode.title
                        binding.tvEpisodeInfoBodyImdb.text = episode.imdbRating
                        binding.tvEpisodeInfoBodyYear.text = episode.year
                        binding.tvEpisodeInfoBodyDirector.text = episode.director
                        binding.tvEpisodeInfoBodyActors.text = episode.actors
                        binding.tvEpisodeInfoBodyPlot.text = episode.plot

                        Glide.with(requireContext())
                            .load(episode.poster)
                            .into(binding.ivPoster)
                    },
                    onFailure = { error ->
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                )
            }
        }


        val seasonNumber = args.seasonNumber
        val episodeNumber = args.episodeNumber

        if (seasonNumber != null && episodeNumber != null) {
            viewModel.fetchEpisode(args.title, seasonNumber, episodeNumber)
        } else {
            Log.e(
                "ShowEpisodeFragment",
                "Invalid season or episode number: ${args.seasonNumber}, ${args.episodeNumber}"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

