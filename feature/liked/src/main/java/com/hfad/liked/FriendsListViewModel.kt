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

class FriendsListViewModel : ViewModel() {

    private val database = Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")
    val usersRef = database.getReference("users")

    private val _friendsList = MutableLiveData<List<Friend>>()
    val friendsList: LiveData<List<Friend>> get() = _friendsList

    private val _approvedFriends = MutableLiveData<List<Friend>>()
    val approvedFriends: LiveData<List<Friend>> get() = _approvedFriends

    fun loadFriends(userKey: String) {
        val requestedFriendsRef = usersRef.child(userKey).child("friends").child("requested")
        val approvedFriendsRef = usersRef.child(userKey).child("friends").child("approved")

        requestedFriendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (childSnapshot in snapshot.children) {
                    val friendKey = childSnapshot.key ?: continue
                    val approved = childSnapshot.getValue(Boolean::class.java) ?: false
                    getUserEmail(friendKey) { email ->
                        friends.add(Friend(friendKey, email, approved))
                        _friendsList.value = friends
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FriendsList", "Database error: $error")
            }
        })

        approvedFriendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (childSnapshot in snapshot.children) {
                    val friendKey = childSnapshot.key ?: continue
                    val approved = childSnapshot.getValue(Boolean::class.java) ?: false
                    getUserEmail(friendKey) { email ->
                        friends.add(Friend(friendKey, email, approved))
                        _approvedFriends.value = friends
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FriendsList", "Database error: $error")
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
                Log.d("FriendsList", "Database error: $error")
                callback("")
            }
        })
    }

    data class Friend(val id: String, val email: String, val approved: Boolean)
}
