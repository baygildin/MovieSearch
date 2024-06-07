package com.hfad.show_episodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hfad.core.BaseFragment
import com.hfad.show_episodes.databinding.FragmentShowEpisodeBinding
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.episode.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { episode ->
                    binding.tvEpisodeInfoBodyTitle.text=episode.title
                    binding.tvEpisodeInfoBodyImdb.text=episode.imdbRating
                    binding.tvEpisodeInfoBodyYear.text=episode.year
                    binding.tvEpisodeInfoBodyDirector.text=episode.director
                    binding.tvEpisodeInfoBodyActors.text=episode.actors
                    binding.tvEpisodeInfoBodyPlot.text=episode.plot

                    Glide.with(requireContext())
                        .load(episode.poster)
                        .into(binding.ivPoster)
                },
                onFailure = { error ->
                    Log.e("ShowEpisodeFragment", "Error fetching episode", error)
                }
            )

        }

        val seasonNumber = args.seasonNumber.toIntOrNull()
        val episodeNumber = args.episodeNumber.toIntOrNull()

        if (seasonNumber != null && episodeNumber != null) {
            viewModel.fetchEpisode(args.title, seasonNumber, episodeNumber)
        } else {
            Log.e("ShowEpisodeFragment", "Invalid season or episode number: ${args.seasonNumber}, ${args.episodeNumber}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

