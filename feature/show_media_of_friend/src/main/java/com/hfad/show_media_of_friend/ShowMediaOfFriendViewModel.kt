package com.hfad.show_media_of_friend

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.hfad.search.model.FavouriteItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class ShowMediaOfFriendViewModel: ViewModel()
{
    private val database =
        Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")

    private val auth = FirebaseAuth.getInstance()
    private val _favouriteItems = MutableStateFlow<List<FavouriteItem>>(emptyList())
    val favouriteItems: Flow<List<FavouriteItem>> get() = _favouriteItems

    private fun convertJsonToFavouritesList(jsonData: String): List<FavouriteItem> {
        return Gson().fromJson(jsonData, Array<FavouriteItem>::class.java).toList()
    }
    suspend fun fetchMediaOfFriend(id: String) {
        val friendFavsRef = database.getReference("users").child(id).child("favourites")
        friendFavsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jsonData = snapshot.value as String?
                if (!jsonData.isNullOrEmpty()) {
                    _favouriteItems.value = convertJsonToFavouritesList(jsonData)
                } else {
                    Log.d("MyError42", "ShowMediaOfFriendViewModel fetchMediaOfFriend: No data found")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("MyError42", "ShowMediaOfFriendViewModel fetchMediaOfFriend: ${error.message}")
            }
        })
    }
}
