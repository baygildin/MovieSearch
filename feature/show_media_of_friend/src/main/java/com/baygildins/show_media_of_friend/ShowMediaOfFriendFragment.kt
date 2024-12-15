package com.baygildins.show_media_of_friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.baygildins.core.BaseFragment
import com.baygildins.show_media_of_friend.databinding.FragmentShowMediaOfFriendBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowMediaOfFriendFragment : BaseFragment(R.layout.fragment_show_media_of_friend) {
    private var _binding: FragmentShowMediaOfFriendBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShowMediaOfFriendViewModel by viewModels()
    private val args: ShowMediaOfFriendFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowMediaOfFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ShowMediaOfFriendAdapter(emptyList()) { item ->
            val id = item.imdbId
            (activity as com.baygildins.navigation.Navigator).navigateShowMediaOfFriendToMediaDetailsWithId(
                id
            )
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchMediaOfFriend(args.userId)
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