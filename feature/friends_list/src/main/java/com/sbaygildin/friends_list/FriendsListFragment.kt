package com.sbaygildin.friends_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sbaygildin.core.BaseFragment
import com.sbaygildin.friends_list.databinding.FragmentFriendsListBinding
import com.sbaygildin.friends_list.databinding.ItemFriendListBinding
import com.sbaygildin.search.model.Friend
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FriendsListFragment : BaseFragment(R.layout.fragment_friends_list) {
    private val viewModel: FriendsListViewModel by viewModels()
    private var _binding: FragmentFriendsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadFriends()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.approvedFriends.collect { approvedFriends ->
                updateFriendsList(approvedFriends)
            }
        }
        binding.btnLogOut.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.signOut()
            }
            Toast.makeText(
                context,
                context?.getResources()?.getString(R.string.toast_user_log_out),
                Toast.LENGTH_LONG
            ).show()
            (activity as com.sbaygildin.navigation.Navigator).navigateFriendsListFragmentToSearchFragment()
        }
        binding.friendRequestsTextView.setOnClickListener {
            (activity as com.sbaygildin.navigation.Navigator).navigateFriendsListFragmentToFriendRequestsFragment()
        }
        binding.btnSearchFriend.setOnClickListener {
            (activity as com.sbaygildin.navigation.Navigator).navigateFriendListToSearchFriend()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateFriendsList(friends: List<Friend>) {
        if (friends.isEmpty()) {
            binding.emptyFriendsMessage.visibility = View.VISIBLE
        } else {
            binding.emptyFriendsMessage.visibility = View.GONE
        }
        binding.approvedFriendsContainer.removeAllViews()
        for (friend in friends) {
            val itemBinding = ItemFriendListBinding.inflate(
                layoutInflater,
                binding.approvedFriendsContainer,
                false
            )
            itemBinding.tvEmailOfFriend.text = friend.email
            itemBinding.btnDeleteFriend.text = getString(R.string.btn_unfriend)
            itemBinding.btnDeleteFriend.setOnClickListener {
                viewModel.deleteFriend(friend.id)
            }

            itemBinding.tvEmailOfFriend.setOnClickListener {
                (activity as com.sbaygildin.navigation.Navigator).navigateFriendsListFragmentToShowMediaOfFriendId(
                    friend.id
                )
            }
            binding.approvedFriendsContainer.addView(itemBinding.root)
        }
    }
}
