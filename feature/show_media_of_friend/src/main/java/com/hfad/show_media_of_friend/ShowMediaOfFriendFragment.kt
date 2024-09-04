package com.hfad.show_media_of_friend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.core.BaseFragment
import com.hfad.show_media_of_friend.databinding.FragmentShowMediaOfFriendBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ShowMediaOfFriendFragment : BaseFragment(R.layout.fragment_show_media_of_friend) {
    private lateinit var binding: FragmentShowMediaOfFriendBinding
    private val viewModel: ShowMediaOfFriendViewModel by viewModels()
    private val args: ShowMediaOfFriendFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowMediaOfFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ShowMediaOfFriendAdapter(emptyList()) { item ->
            val id = item.imdbId
            (activity as com.hfad.navigation.Navigator).navigateShowMediaOfFriendToMediaDetailsWithId(
                id
            )
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d("28.02.24", "${args.userId}")
            viewModel.fetchMediaOfFriend(args.userId)
            viewModel.favouriteItems.collect { items ->
                adapter.updateItems(items)
            }
        }
    }
}