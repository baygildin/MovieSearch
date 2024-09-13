package com.hfad.friend_requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.hfad.core.BaseFragment
import com.hfad.friend_requests.databinding.FragmentFriendRequestsBinding
import com.hfad.friend_requests.databinding.ItemFriendRequestBinding
import com.hfad.search.model.Friend
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendRequestsFragment : BaseFragment(R.layout.fragment_friend_requests) {
    private val viewModel: FriendRequestsViewModel by viewModels()
    private var _binding: FragmentFriendRequestsBinding? = null
    private val binding get() =  _binding!!
    private val auth = FirebaseAuth.getInstance()
    private val userKey = auth.currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadFriendRequests(userKey)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.friendRequests.collect { request ->
                updateFriendRequestsList(request)
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.operationStatus.collect { statusMessage ->
                    Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.loadFriendRequests(userKey)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateFriendRequestsList(friends: List<Friend>) {
        binding.friendRequestsContainer.removeAllViews()
        for (friend in friends) {
            val itemBinding = ItemFriendRequestBinding.inflate(layoutInflater, binding.friendRequestsContainer, false)

            itemBinding.tvFriendEmail.text =  friend.email
            itemBinding.tvFriendEmail.setOnClickListener{
                (activity as com.hfad.navigation.Navigator).navigateFriendRequestToShowMediaOfFriendId(
                    friend.id
                )
            }
            itemBinding.btnAddFriend.setOnClickListener{
                viewModel.approveFriend(userKey, friend.id)
            }
            itemBinding.btnRejectFriend.setOnClickListener{
                viewModel.rejectFriend(userKey, friend.id)
            }
            binding.friendRequestsContainer.addView(itemBinding.root)
        }
    }
}