package com.baygildins.show_media_of_friend

import android.util.Log
import androidx.lifecycle.ViewModel
import com.baygildins.search.firebase.FirebaseRepository
import com.baygildins.search.model.FavouriteItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ShowMediaOfFriendViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _favouriteItems = MutableStateFlow<List<FavouriteItem>>(emptyList())
    val favouriteItems: Flow<List<FavouriteItem>> get() = _favouriteItems

    private fun convertJsonToFavouritesList(jsonData: String): List<FavouriteItem> {
        return Gson().fromJson(jsonData, Array<FavouriteItem>::class.java).toList()
    }

    fun fetchMediaOfFriend(id: String) {
        val friendFavsRef = firebaseRepository.usersRef.child(id).child("favourites")
        friendFavsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jsonData = snapshot.value as String?
                if (!jsonData.isNullOrEmpty()) {
                    _favouriteItems.value = convertJsonToFavouritesList(jsonData)
                } else {
                    Log.d(
                        "ShowMediaOfFriendViewModel",
                        "ShowMediaOfFriendViewModel fetchMediaOfFriend: No data found"
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(
                    "ShowMediaOfFriendViewModel",
                    "ShowMediaOfFriendViewModel fetchMediaOfFriend: ${error.message}"
                )
            }
        })
    }
}
