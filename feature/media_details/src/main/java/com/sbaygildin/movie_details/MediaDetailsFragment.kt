package com.sbaygildin.media_details


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sbaygildin.core.BaseFragment
import com.sbaygildin.media_details.databinding.FragmentMediaDetailsBinding
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
        var poster = ""
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
                        poster = it.poster
                        if (it.type == "series") binding.btnShowEpisodes.visibility =
                            View.VISIBLE
                    },
                    onFailure = {
                        Log.e("MediaDetailsFragment", "$it in viewModel.mediaDetails.collect")

                    }
                )
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFavourite.collect { it ->
                binding.likeButtonHeart.setImageResource(
                    if (it) R.drawable.heart_red_in_circle
                    else R.drawable.heart_black
                )
            }
        }
        binding.btnShareMedia.setOnClickListener {
            val text =
                "${binding.tvMediaInfoBodyTitle.text} ${binding.tvMediaInfoBodyYear.text}\n${binding.tvMediaInfoBodyPlot.text}"
            val imageUrl = poster

            Glide.with(requireContext())
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val imagePath = MediaStore.Images.Media.insertImage(
                            requireContext().contentResolver, resource, "Title", null
                        )
                        val imageUri = Uri.parse(imagePath)

                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "*/*"
                            putExtra(Intent.EXTRA_TEXT, text)
                            putExtra(Intent.EXTRA_STREAM, imageUri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        startActivity(Intent.createChooser(shareIntent, "Share via"))
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        binding.btnShowEpisodes.setOnClickListener {
            (activity as com.sbaygildin.navigation.Navigator).navigateMediaDetailsToShowSeasonsWithId(
                chosenMovieId
            )
        }

        binding.ivPoster.setOnClickListener {
            (activity as com.sbaygildin.navigation.Navigator).navigateMediaDetailsToPosterWithId(
                chosenMovieId
            )
        }
        binding.likeButtonHeart.setOnClickListener {
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