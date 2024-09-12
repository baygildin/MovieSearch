package com.hfad.liked

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.hfad.core.BaseFragment
import com.hfad.liked.databinding.FragmentLikedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LikedFragment : BaseFragment(R.layout.fragment_liked) {
    private val viewModel: LikedViewModel by viewModels()
    private var _binding: FragmentLikedBinding? = null
    private val binding get() = _binding!!
    private var isSortedByDate = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = LikedMediaAdapter(emptyList()) { item ->
            val id = item.imdbId
            (activity as com.hfad.navigation.Navigator).navigateLikedToMediaDetailsWithId(id)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)


        binding.btnSendToCloud.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser == null) {
                (activity as com.hfad.navigation.Navigator).navigateLikedToLogin()
            } else {
                if (viewModel.doesUserAgreeToSendToCloud) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val jsonLikedMediaToCloud = viewModel.getFavouritesJson()
                        viewModel.saveFavouritesToClouds(jsonLikedMediaToCloud)
                        if (viewModel.doesUserAgreeToSendToCloud) {
                            Toast.makeText(
                                context,
                                context?.getResources()?.getString(R.string.toast_sent_to_cloud),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    viewModel.doesUserAgreeToSendToCloud = true
                    Toast.makeText(
                        context,
                        context?.getResources()?.getString(R.string.toast_agree_to_send_to_cloud),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        binding.btnDownloadFromCloud.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser == null) {
                (activity as com.hfad.navigation.Navigator).navigateLikedToLogin()
            } else {
                viewModel.fetchFavouritesFromCloud(onSucces = { jsonData ->
                    if (jsonData.isNotEmpty()) {
                        if (viewModel.doesUserAgreeToReplaceFromCloud) {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.updateRoomDatabaseFromJson(jsonData)
                            }
                        } else {
                            Toast.makeText(
                                context, getString(R.string.toast_agree_to_replace_from_cloud),
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.doesUserAgreeToReplaceFromCloud = true
                        }
                    }
                }, onFailure = { error ->
                    Log.e("LikedFragment", "Error fetching favourites:${error.message}")
                }
                )
            }

        }
        binding.btnSort.setOnClickListener {
            if (isSortedByDate) {
                Toast.makeText(
                    context, getString(R.string.toast_sorted_by_date),
                    Toast.LENGTH_SHORT
                ).show()
                binding.abcIcon.setImageResource(R.drawable.baseline_abc_24)
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.favouriteItems.collect { items ->
                        adapter.updateItems(items)
                    }
                }
            } else {
                Toast.makeText(
                    context, getString(R.string.toast_sorted_by_title),
                    Toast.LENGTH_SHORT
                ).show()
                binding.abcIcon.setImageResource(R.drawable.baseline_access_time_24)

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.favouriteItemsByName.collect { items ->
                        adapter.updateItems(items)
                    }
                }
            }
            isSortedByDate = !isSortedByDate

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favouriteItems.collect { items ->
                adapter.updateItems(items)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}