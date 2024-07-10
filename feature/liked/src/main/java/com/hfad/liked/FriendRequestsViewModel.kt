package com.hfad.liked

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendRequestsViewModel : ViewModel() {

    val database = Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")
    val usersRef = database.getReference("users")

    private val _friendRequests = MutableLiveData<List<Friend>>()
    val friendRequests: LiveData<List<Friend>> get() = _friendRequests

    fun loadFriendRequests(userKey: String) {
        val requestedFriendsRef = usersRef.child(userKey).child("friends").child("requested")

        requestedFriendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (childSnapshot in snapshot.children) {
                    val friendKey = childSnapshot.key ?: continue
                    val approved = childSnapshot.getValue(Boolean::class.java) ?: false
                    getUserEmail(friendKey) { email ->
                        friends.add(Friend(friendKey, email, approved))
                        _friendRequests.value = friends
                        Log.d("sdsdsd", "${friends.toString()}")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FriendRequests", "Database error: $error")
            }
        })
    }

    private fun getUserEmail(userId: String, callback: (String) -> Unit) {
        usersRef.child(userId).child("email").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.getValue(String::class.java) ?: ""
                callback(email)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FriendRequests", "Database error: $error")
                callback("")
            }
        })
    }

    fun approveFriend(userKey: String, friendId: String) {
        val userRef = usersRef.child(userKey).child("friends")
        userRef.child("requested").child(friendId).removeValue().addOnCompleteListener {
            userRef.child("approved").child(friendId).setValue(true)
        }
    }

    data class Friend(val id: String, val email: String, val approved: Boolean)
}
