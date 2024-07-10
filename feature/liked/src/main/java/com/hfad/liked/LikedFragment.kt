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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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

    private val auth = FirebaseAuth.getInstance()
    private val userKey = auth.currentUser?.uid ?: ""

    val myRef = database.getReference("users").child(userKey)

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


        binding.btnSendToCloud.setOnClickListener {
            if (viewModel.doesUserAgreeToSendToCloud){
                val jsonLikedMediaToCloud = viewModel.getFavouritesJson()
                myRef.child("favourites").setValue(jsonLikedMediaToCloud)
                if (viewModel.doesUserAgreeToSendToCloud) {Toast.makeText(context, context?.getResources()?.getString(R.string.toast_sent_to_cloud), Toast.LENGTH_SHORT).show()
                }
            } else {
                viewModel.doesUserAgreeToSendToCloud = true
                Toast.makeText(context, context?.getResources()?.getString(R.string.toast_agree_to_send_to_cloud), Toast.LENGTH_LONG).show()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favouriteItems.collect { items ->
                adapter.updateItems(items)
            }
        }
        binding.btnDownloadFromCloud.setOnClickListener {
            fetchFavouritesFromCloud()

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

    private fun fetchFavouritesFromCloud() {
        myRef.child("favourites").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jsonData = snapshot.value as String?
                if (!jsonData.isNullOrEmpty()) {
                    if (viewModel.doesUserAgreeToReplaceFromCloud){
                        viewModel.updateRoomDatabaseFromJson(jsonData)
                    }
                    else Toast.makeText(
                        context,
                        context?.getResources()?.getString(R.string.toast_agry_to_replace_from_cloud),
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.doesUserAgreeToReplaceFromCloud = true
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("myError42", "$error in LikedFragment`s fetchFavouritesFromCloud")
            }
        })
    }
}