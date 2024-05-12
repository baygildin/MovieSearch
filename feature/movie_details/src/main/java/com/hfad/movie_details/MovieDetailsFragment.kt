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
import com.hfad.search.model.FavouriteDao
import com.hfad.search.model.FavouriteDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentMovieDetailsBinding? = null
    private lateinit var favouriteDao: FavouriteDao

    @Inject
    lateinit var omdbApi: OmdbApi
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding in MovieDetailsBinding of MovieDetailsFragment must not be null")
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel.setBooleanFavourite(binding.likeButton.text.toString())
        Log.e("getmovieError1","${viewModel.searchText}")
        var chosenMovieId = ""
        favouriteDao = FavouriteDatabase.getDatabase(requireContext()).favouriteDao()

        viewModel.movieTitle.observe(viewLifecycleOwner) { title ->
            chosenMovieId = title

            binding.tvDetailsScreen.text = chosenMovieId
            Log.e("getmovieError","chosenMovieId = title$chosenMovieId")
            lifecycleScope.launch {
                try {
                    Log.e("findError", "44 str")
                    val result = omdbApi.searchById(chosenMovieId)
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
                        .append(result.title+"\n")
                        .bold { append(imdbRating)}
                        .append(result.imdbRating+"\n")
                        .bold { append(movieYear) }
                        .append(result.year + "\n")
                        .bold{append(director)}
                        .append(result.director+"\n")
                        .bold { append(actors) }
                        .append(result.actors+"\n")
                        .bold { append(plot) }
                        .append(result.plot + "\n")
                        .bold { append(awards) }
                        .append(result.awards+"\n")
                    viewModel.setChosenImbdId(result.imdbID)

//                    val existingFavouriteLiveData = favouriteDao.getFavouriteByImdbId(result.imdbID)
//                    existingFavouriteLiveData.observe(viewLifecycleOwner) { existingFavourite ->
//                        existingFavourite?.let {
//                            // В этом блоке обрабатываем результат, когда он доступен
//                            if (existingFavourite != null) {
//                                binding.likeButton.text = "Уже в избранном"
//                            }
//                        }
//                    }


                    with(binding) {
                        tvDetailsScreen.text = text
                        btnShowEpisodes.setOnClickListener { val request = NavDeepLinkRequest.Builder
                            .fromUri("android-app://com.hfad.show_episodes/ShowEpisodesFragment".toUri())
                            .build()
                            findNavController().navigate(request)
                        }
                        ivPoster.setOnClickListener {
                            val request = NavDeepLinkRequest.Builder
                                .fromUri("android-app://com.hfad.poster/PosterFragment".toUri())
                                .build()
                            findNavController().navigate(request)
                        }
                        likeButton.setOnClickListener {
                            if (viewModel.btnBooleanFavourite.value == "Добавить в избранное") {
                                viewModel.setBooleanFavourite("Добавлено в избранное")
                                binding.likeButton.text = "Добавлено в избранное"}
                            else {binding.likeButton.text = "Добавить в избранное"
                                viewModel.setBooleanFavourite("Добавлено в избранное")}
//                            lifecycleScope.launch {
//                                existingFavouriteLiveData.value?.let { existingFavourite ->
//                                    if (existingFavourite != null) {
//                                        favouriteDao.removeFavourite(existingFavourite)
//                                        Log.d("Favorite", "Removed from favorites: $existingFavourite")
//                                        binding.likeButton.text = "Добавить в избранное"
//                                    } else {
//                                        val favouriteItem = FavouriteItem(
//                                            imdbId = result.imdbID
//                                        )
//                                        favouriteDao.addFavourite(favouriteItem)
//                                        Log.d("Favorite", "Added to favorites: $favouriteItem")
//                                        binding.likeButton.text = "Удалить из избранного"
//                                    }
//                                }
//                            }
                        }
                        /*likeButton.setOnClickListener {

                            lifecycleScope.launch {

                                if (existingFavourite != null) {
                                    favouriteDao.removeFavourite(existingFavourite)
                                    Log.d("Favorite", "Removed from favorites: $existingFavourite")
                                    binding.likeButton.text = "Добавить в избранное"
                                } else {
                                    val favouriteItem = FavouriteItem(
                                        imdbId = result.imdbID
                                    )
                                    favouriteDao.addFavourite(favouriteItem)
                                    Log.d("Favorite", "Added to favorites: $favouriteItem")
                                    binding.likeButton.text = "Удалить из избранного"
                                }
                            }
                        }*/
                    }
                    if (result.type=="series") binding.btnShowEpisodes.visibility= View.VISIBLE

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