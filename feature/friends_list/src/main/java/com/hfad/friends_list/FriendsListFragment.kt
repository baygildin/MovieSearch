package com.hfad.friends_list

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
import com.hfad.friends_list.databinding.FragmentFriendsListBinding
import com.hfad.search.model.Friend
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

        viewModel.approvedFriends.observe(viewLifecycleOwner, Observer { approvedFriends ->
            updateFriendsList(approvedFriends)
        })
        binding.friendRequestsTextView.setOnClickListener{
            (activity as com.hfad.navigation.Navigator).navigateFriendsListFragmentToFriendRequestsFragment()
        }
        binding.btnSearchFriend.setOnClickListener {
            (activity as com.hfad.navigation.Navigator).navigateFriendListToSearchFriend()
        }
    }

    private fun updateFriendsList(friends: List<Friend>) {
        binding.approvedFriendsContainer.removeAllViews()
        for (friend in friends) {
            val itemView = layoutInflater.inflate(R.layout.item_friend_list, null)
            val friendButton = itemView.findViewById<TextView>(R.id.tvEmailOfFriend)
            val deleteButton = itemView.findViewById<Button>(R.id.btnAddFriend)

            friendButton.text = friend.email
            deleteButton.text = getString(R.string.btn_unfriend)
            deleteButton.setOnClickListener {
                viewModel.deleteFriend(userKey, friend.id)
            }
            friendButton.setOnClickListener {
                (activity as com.hfad.navigation.Navigator).navigateFriendsListFragmentToShowMediaOfFriendId(friend.id)
            }

            binding.approvedFriendsContainer.addView(itemView)
        }
    }
}
