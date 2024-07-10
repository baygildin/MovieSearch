package com.hfad.liked

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.hfad.core.BaseFragment
import com.hfad.liked.databinding.FragmentFriendRequestsBinding
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
            val friendButton = Button(context).apply {
                setTextColor(resources.getColor(R.color.main_text_color))
                setBackgroundColor(Color.TRANSPARENT)
                text = friend.email
                setOnClickListener {
                    // Handle friend request click
                }
            }
            binding.friendRequestsContainer.addView(friendButton)
        }
    }
}
