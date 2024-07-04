package com.hfad.liked
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hfad.core.BaseFragment
import com.hfad.liked.databinding.FragmentFromFbLikedBinding
import com.hfad.search.model.FavouriteItem
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class FromFbLikedFragment : BaseFragment(R.layout.fragment_from_fb_liked) {
    private val viewModel: FromFbLikedViewModel by viewModels()
    private lateinit var binding: FragmentFromFbLikedBinding
    val database = Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")
    val myRef = database.getReference("DBbaigildinsamatgmailcom")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFromFbLikedBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onChangeListener(myRef)
    }
    private fun onChangeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    txtFromFbDb.append("\n")
//                    Log.d("FromFbLikedFragment", "text ${txtFromFbDb.toString()}")
//                    txtFromFbDb.append("salam ${snapshot.value.toString()}")
//                    txtFromFbDb.append("\n")
                    val favouritesList: List<FavouriteItem> = parseFavouritesJson(snapshot.value.toString())
                    println("JSON123: $snapshot")

                    favouritesList.forEach{it ->
                        txtFromFbDb.append(it.toString())
                        txtFromFbDb.append("\n")
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun parseFavouritesJson(json: String): List<FavouriteItem> {
        val gson = Gson()

        val type = object : TypeToken<List<FavouriteItem>>() {}.type
        return gson.fromJson(json, type)
    }
}