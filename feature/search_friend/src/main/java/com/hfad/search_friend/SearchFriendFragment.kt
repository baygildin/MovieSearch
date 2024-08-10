package com.hfad.search_friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hfad.core.BaseFragment
import com.hfad.search_friend.databinding.FragmentSearchFriendBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFriendFragment : BaseFragment(R.layout.fragment_search_friend) {
    private val viewModel: SearchFriendViewModel by viewModels()
    private lateinit var binding: FragmentSearchFriendBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchFriendButton.setOnClickListener {
            val email = binding.searchFriendEditText.text.toString().lowercase()
            viewModel.friendEmail.value = email
            if (email.isNullOrEmpty()) {
                Toast.makeText(context, context?.getResources()?.getString(R.string.toast_empty_edit_text_value), Toast.LENGTH_LONG).show()

            }
            else viewModel.searchFriend()
        }

        viewModel.friendEmail.observe(viewLifecycleOwner, Observer { email ->
            binding.searchFriendEditText.setText(email)
        })

        viewModel.friendInfo.observe(viewLifecycleOwner, Observer { info ->
            binding.friendInfoTextView.text = info
        })

        viewModel.isFriendFound.observe(viewLifecycleOwner, Observer { isFound ->
            binding.connectFriendButton.visibility = if (isFound) View.VISIBLE else View.GONE
        })

        binding.connectFriendButton.setOnClickListener {
            viewModel.sendConnectionRequest()
        }

        viewModel.favouritesListString.observe(viewLifecycleOwner, Observer { favouritesListString ->
            binding.tvFriendsFavouriteMediaList.text = favouritesListString
        })
    }
    fun emailToValidFbKey(str: String) = str.replace(".", "*")
}

