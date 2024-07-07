package com.hfad.liked

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.hfad.core.BaseFragment
import com.hfad.liked.databinding.FragmentFriendsListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsListFragment : BaseFragment(R.layout.fragment_friends_list) {
    private val viewModel: FriendsListViewModel by viewModels()
    private lateinit var binding: FragmentFriendsListBinding
    private val auth = FirebaseAuth.getInstance()
    private val userKey = auth.currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadFriends(userKey)

        viewModel.friendsList.observe(viewLifecycleOwner, Observer { friends ->
            updateFriendsList(friends)
        })

        viewModel.approvedFriends.observe(viewLifecycleOwner, Observer { approvedFriends ->
            updateApprovedFriendsList(approvedFriends)
        })
    }

    private fun updateFriendsList(friends: List<FriendsListViewModel.Friend>) {
        binding.requestedFriendsContainer.removeAllViews()
        for (friend in friends) {
            val button = Button(context).apply {
                text = friend.email
                setOnClickListener {
                    if (friend.approved) {
                        // Navigate to friend's favourites
                    } else {
                        // Show message that friend is not approved
                    }
                }
            }
            binding.requestedFriendsContainer.addView(button)
        }
    }

    private fun updateApprovedFriendsList(friends: List<FriendsListViewModel.Friend>) {
        binding.approvedFriendsContainer.removeAllViews()
        for (friend in friends) {
            val button = Button(context).apply {
                text = friend.email
                setOnClickListener {
                    // Navigate to friend's favourites
                }
            }
            binding.approvedFriendsContainer.addView(button)
        }
    }
}
