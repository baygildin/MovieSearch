package com.hfad.friend_requests

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.hfad.core.BaseFragment
import com.hfad.friend_requests.databinding.FragmentFriendRequestsBinding
import com.hfad.search.model.Friend
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendRequestsFragment : BaseFragment(R.layout.fragment_friend_requests) {
    private val viewModel: FriendRequestsViewModel by viewModels()
    private lateinit var binding: FragmentFriendRequestsBinding
    private val auth = FirebaseAuth.getInstance()
    private val userKey = auth.currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendRequestsBinding.inflate(inflater, container, false)
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

    private fun updateFriendRequestsList(friends: List<Friend>) {
        binding.friendRequestsContainer.removeAllViews()
        for (friend in friends) {
            val itemView = layoutInflater.inflate(R.layout.item_friend_request, null)
            val friendEmailTextView = itemView.findViewById<TextView>(R.id.tvFriendEmail)
            val addFriendButton = itemView.findViewById<Button>(R.id.btnAddFriend)

            friendEmailTextView.text = friend.email
            addFriendButton.setOnClickListener {
                viewModel.approveFriend(userKey, friend.id)
            }
            friendEmailTextView.setOnClickListener {
                Log.d("28.02.24", "${friend.email}    ${friend.id}")
                (activity as com.hfad.navigation.Navigator).navigateFriendRequestToShowMediaOfFriendId(
                    friend.id
                )
            }
            binding.friendRequestsContainer.addView(itemView)
        }
    }
}