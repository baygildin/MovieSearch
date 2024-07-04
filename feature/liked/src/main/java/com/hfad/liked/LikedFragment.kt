package com.hfad.liked

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hfad.core.BaseFragment
import com.hfad.liked.databinding.FragmentLikedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LikedFragment : BaseFragment(R.layout.fragment_liked) {
    private val viewModel: LikedViewModel by viewModels()
    private lateinit var binding: FragmentLikedBinding
    private var isSortedByDate = false

    val database =
        Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")
    val myRef = database.getReference("DBbaigildinsamatgmailcom")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LikedMediaAdapter(emptyList()) { item ->
            val id = item.imdbId
            (activity as com.hfad.navigation.Navigator).navigateLikedToMediaDetailsWithId(id)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.btnSearchFriend.setOnClickListener {
            (activity as com.hfad.navigation.Navigator).navigateLikedToSearchFriend()
        }

        onChangeListener(myRef)



        binding.btnSendToCloud.setOnClickListener {

            val json_liked_media_to_cloud = viewModel.getFavouritesJson()
            Log.d("likedfragment", " btn send to cloud text ${json_liked_media_to_cloud}")
            myRef.setValue("${json_liked_media_to_cloud}")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favouriteItems.collect { items ->
                adapter.updateItems(items)
            }
        }
        binding.btnReadFriendFavorites.setOnClickListener{
            (activity as com.hfad.navigation.Navigator).navigateLikedToFromFbLiked()
        }
        binding.btnSort.setOnClickListener {
            if (isSortedByDate) {
                Toast.makeText(
                    context,
                    context?.getResources()?.getString(R.string.toast_sorted_by_date),
                    Toast.LENGTH_SHORT
                ).show()
                binding.abcIcon.setImageResource(R.drawable.baseline_abc_24)
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.favouriteItems.collect { items ->
                        adapter.updateItems(items)
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    context?.getResources()?.getString(R.string.toast_sorted_by_title),
                    Toast.LENGTH_SHORT
                ).show()
                binding.abcIcon.setImageResource(R.drawable.baseline_access_time_24)

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.favouriteItemsByName.collect { items ->
                        adapter.updateItems(items)
                    }
                }
            }
            isSortedByDate = !isSortedByDate

        }
    }

    private fun onChangeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    txtFromFbDb.append("\n")
                    Log.d("likedfragment", "text ${txtFromFbDb.toString()}")
                    txtFromFbDb.append("salam ${snapshot.value.toString()}")
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun singleOnChangeListener(dRef: DatabaseReference) {
        dRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    txtFromFbDb.append("\n")
                    Log.d("likedfragment", "text ${txtFromFbDb.toString()}")
                    txtFromFbDb.append("salam ${snapshot.value.toString()}")
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
