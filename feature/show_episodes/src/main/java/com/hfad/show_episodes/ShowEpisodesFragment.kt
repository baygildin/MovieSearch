package com.hfad.show_episodes

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.net.toUri
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.hfad.search.OmdbApi
import com.hfad.search.SharedViewModel
import com.hfad.show_episodes.databinding.FragmentShowEpisodesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShowEpisodesFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentShowEpisodesBinding? = null
    @Inject
    lateinit var omdbApi: OmdbApi
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in FragmentShowEpisodesBinding of FragmentShowEpisodes Fragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.chosenImbdId.observe(viewLifecycleOwner) { id ->
            val chosenMovieId = id
            lifecycleScope.launch {
                try {
                    val seriesInfo = omdbApi.searchBySeason(chosenMovieId, "1")
                    val numberOfSeasons = seriesInfo.totalSeasons.toInt()
                    Log.e("cod43", "${seriesInfo.totalSeasons.toInt()}")

                    for (seasonNumber in 1..numberOfSeasons) {
                        val seasonButton = createSeasonButton(seriesInfo.title, seasonNumber)
                        binding.seasonsContainer.addView(seasonButton)
                    }
                } catch (e: Exception) {
                }
            }
        }

        _binding = FragmentShowEpisodesBinding.inflate(inflater, container, false)
        val view = binding.root

        // Moved createEpisodeButton and createSeasonButton functions outside onCreateView

        binding.btnDetailsScreen.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://com.hfad.movie_details/movieDetailsFragment".toUri())
                .build()
            findNavController().navigate(request)
        }
        return view
    }

    private fun createEpisodeButton(title: String, seasonNumber: Int, episodeNumber: Int): View {
        val button = Button(requireContext())
        button.text = "Episode $episodeNumber"
        binding.seasonsContainer.visibility= View.GONE
        binding.episodesContainer.visibility= View.VISIBLE
        button.setBackgroundColor(Color.TRANSPARENT)
        button.setTextColor(Color.parseColor("#E0D9D9"))
        button.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val result = omdbApi.searchByEpisode(
                        title,
                        seasonNumber.toString(),
                        episodeNumber.toString()

                    )

                    val title = "Title: "
                    val actors = "Actors: "
                    val plot = "Plot:\n"
                    val imdbRating = "IMDB rating: "
                    val Year = "Year: "
                    val director = "Director: "
                    val Season = "Season: "
                    val Episode = "Episode: "

                    val text = SpannableStringBuilder()
                        .bold { append(title) }
                        .append(result.title + "\n")
                        .bold { append(imdbRating) }
                        .append(result.imdbRating + "\n")
                        .bold { append(Year) }
                        .append(result.year + "\n")
                        .bold { append(director) }
                        .append(result.director + "\n")
                        .bold { append(actors) }
                        .append(result.actors + "\n")
                        .bold { append(plot) }
                        .append(result.plot + "\n")

                    binding.tvEpisodeInfo.text = text
                    binding.tvEpisodeInfo.visibility= View.VISIBLE
                    binding.episodesContainer.visibility = View.GONE
                } catch (e: Exception) {
                    Log.e("cod43", "createEpisodeButton doesnt work")
                }
            }
        }
        return button
    }

    private fun createSeasonButton(title: String, seasonNumber: Int): View {
        val button = Button(requireContext())
        button.text = "Season $seasonNumber"
        button.setBackgroundColor(Color.TRANSPARENT)
        button.setTextColor(Color.parseColor("#E0D9D9"))

        button.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val episodesInfo = omdbApi.searchBySeason(title, seasonNumber.toString())
                    val numberOfEpisodes = episodesInfo.episodes.size
                    Log.e("cod43", "${episodesInfo.episodes.size}")

                    for (episodeNumber in 1..numberOfEpisodes) {
                        val episodeButton = createEpisodeButton(episodesInfo.title, seasonNumber, episodeNumber)
                        binding.episodesContainer.addView(episodeButton)
                    }
                } catch (e: Exception) {
                    Log.e("cod43", "где то в кнопках эпизодов")
                }
            }
        }
        return button
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

