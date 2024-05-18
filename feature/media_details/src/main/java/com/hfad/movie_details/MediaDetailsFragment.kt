package com.hfad.media_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hfad.media_details.databinding.FragmentMediaDetailsBinding
import com.hfad.search.OmdbApi
import com.hfad.search.model.FavouriteDao
import com.hfad.search.model.FavouriteDatabase
import com.hfad.search.model.FavouriteItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MediaDetailsFragment : Fragment() {
    private var _binding: FragmentMediaDetailsBinding? = null
    private val args: MediaDetailsFragmentArgs by navArgs<MediaDetailsFragmentArgs>()


    @Inject
    lateinit var omdbApi: OmdbApi
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in MovieDetailsBinding of MovieDetailsFragment must not be null")
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMediaDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        var chosenMovieId = args.id
        var favouriteDB: FavouriteDao
        favouriteDB = FavouriteDatabase.getDatabase(requireContext()).favouriteDao()
        binding.tvDetailsScreen.text = chosenMovieId
        lifecycleScope.launch {
            try {
                Log.e("findError", "44 str")
                val result = omdbApi.searchById(chosenMovieId)
                Log.e("findError", "${result.toString()}")
                Log.e("cod43", "chosenmov in mov det frag $chosenMovieId")
                Glide.with(requireContext())
                    .load(result.poster)
                    .into(binding.ivPoster)
                val title = "Title: "
                val actors = "Actors: "
                val plot = "Plot:\n"
                val imdbRating = "IMDB rating: "
                val movieYear = "Year: "
                val awards = "Awards: "
                val director = "Director: "

                val text = SpannableStringBuilder()
                    .bold { append(title) }
                    .append(result.title + "\n")
                    .bold { append(imdbRating) }
                    .append(result.imdbRating + "\n")
                    .bold { append(movieYear) }
                    .append(result.year + "\n")
                    .bold { append(director) }
                    .append(result.director + "\n")
                    .bold { append(actors) }
                    .append(result.actors + "\n")
                    .bold { append(plot) }
                    .append(result.plot + "\n")
                    .bold { append(awards) }
                    .append(result.awards + "\n")
                binding.likeButtonHeart.setImageResource(R.drawable.heart_black)
                val existingFavourite = favouriteDB.getFavouriteByImdbId(chosenMovieId)
                if (existingFavourite != null) {
                    binding.likeButtonHeart.setImageResource(R.drawable.heart_red_in_circle)
                }
                with(binding) {
                    tvDetailsScreen.text = text
                    btnShowEpisodes.setOnClickListener {
                        (activity as com.hfad.navigation.Navigator).navigateMediaDetailsToShowEpisodesWithId(
                            chosenMovieId
                        )
                    }
                    ivPoster.setOnClickListener {
                        (activity as com.hfad.navigation.Navigator).navigateMediaDetailsToPosterWithId(
                            chosenMovieId
                        )
                    }
                    likeButtonHeart.setOnClickListener {
                        lifecycleScope.launch {
                            try {
                                val existingFavourite =
                                    favouriteDB.getFavouriteByImdbId(chosenMovieId)
                                if (existingFavourite != null) {
                                    favouriteDB.removeFavourite(existingFavourite)
//                                    binding.likeButton.text = "Добавить в избранное"
                                    binding.likeButtonHeart.setImageResource(R.drawable.heart_black)
                                } else {
                                    val favouriteItem = FavouriteItem(null, chosenMovieId)
                                    favouriteDB.addFavourite(favouriteItem)
//                                    binding.likeButton.text = "Убрать из избранного"
                                    binding.likeButtonHeart.setImageResource(R.drawable.heart_red_in_circle)

                                }
                            } catch (e: Exception) {
                                Log.e(
                                    "Favorite",
                                    "6Failed to execute likeButton onClick handler: $e"
                                )
                            }
                        }
                    }
                }
                if (result.type == "series") binding.btnShowEpisodes.visibility = View.VISIBLE

            } catch (e: Exception) {
                Log.e(
                    "getmovieError",
                    "$e в строчке viewModel.movieTitle.observe(viewLifecycleOwner) { title -> chosenMovieId = titlebinding.tvFromSearchText.text= chosenMovieIdbinding.tvDetailsScreen"
                )
            }
        }


        return view

    }
}