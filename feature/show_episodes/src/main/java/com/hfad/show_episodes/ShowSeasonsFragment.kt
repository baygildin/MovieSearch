package com.hfad.show_episodes

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hfad.core.BaseFragment
import com.hfad.show_episodes.databinding.FragmentShowSeasonsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowSeasonsFragment : BaseFragment(R.layout.fragment_show_seasons) {

    private var _binding: FragmentShowSeasonsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShowSeasonsViewModel by viewModels()
    private val args: ShowSeasonsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowSeasonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.seasons.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { seasons ->
                    binding.seasonsContainer.removeAllViews()
                    for (season in 1..seasons.totalSeasons.toInt()) {
                        val button = Button(requireContext()).apply {
                            text = "Season $season"
                            setOnClickListener {
                                (activity as com.hfad.navigation.Navigator).navigateShowSeasonsToShowEpisodes(
                                    seasons.title, season.toString()
                                )
                            }
                        }
                        Log.d("nowornever", "showEpisodes ${seasons.title}")
                        val layoutParams = ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            topMargin = 15
                        }
                        button.layoutParams = layoutParams
                        button.setTextColor(resources.getColor(R.color.c0eacb, null))
                        button.textSize = 15f
                        button.gravity = Gravity.CENTER
                        button.setBackgroundResource(R.drawable.border_background)
                        binding.seasonsContainer.addView(button)
                    }
                },
                onFailure = { error ->
                    Log.e("myError42", "error in show seasons viewModel.seasons.observe(viewLifecycleOwner) { result ->$error")
                }
            )
        }

        val id = args.id
        viewModel.fetchSeasons(id)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}