package com.hfad.friend_requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.hfad.core.BaseFragment
import com.hfad.friend_requests.databinding.FragmentFriendRequestsBinding
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.friendRequests.observe(viewLifecycleOwner, Observer { friendRequests ->
            updateFriendRequestsList(friendRequests)
        })
    }

    private fun updateFriendRequestsList(friends: List<FriendRequestsViewModel.Friend>) {
        binding.friendRequestsContainer.removeAllViews()
        for (friend in friends) {
            val itemView = layoutInflater.inflate(R.layout.item_friend_request, null)
            val friendEmailTextView = itemView.findViewById<TextView>(R.id.tvFriendEmail)
            val addFriendButton = itemView.findViewById<Button>(R.id.btnAddFriend)

            friendEmailTextView.text = friend.email
            addFriendButton.setOnClickListener {
                addFriend(friend)
            }
            binding.friendRequestsContainer.addView(itemView)
        }
    }

    private fun addFriend(friend: FriendRequestsViewModel.Friend) {
        val userRef = viewModel.usersRef.child(userKey).child("friends")
        // Move friend from "requested" to "approved"
        userRef.child("requested").child(friend.id).removeValue().addOnCompleteListener {
            userRef.child("approved").child(friend.id).setValue(true)
        }
    }
}
