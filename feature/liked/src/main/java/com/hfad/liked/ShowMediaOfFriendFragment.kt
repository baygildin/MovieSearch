package com.hfad.liked

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.liked.databinding.FragmentShowMediaOfFriendBinding
import kotlinx.coroutines.launch

class ShowMediaOfFriendFragment : Fragment() {
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
        Log.d("Meefef", "dssdd")
        val adapter = ShowMediaOfFriendAdapter(emptyList()) { item ->
            Log.d("Meefef", "${item.toString()}")

            val id = item.imdbId
            Log.d("Meefef", "${id}")

            (activity as com.hfad.navigation.Navigator).navigateShowMediaOfFriendToMediaDetailsWithId(id)
        }
        binding.recyclerView.adapter =adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchMediaOfFriend(args.friendEmail)
            viewModel.favouriteItems.collect{items ->
                adapter.updateItems(items)
            }
        }


    }

}
