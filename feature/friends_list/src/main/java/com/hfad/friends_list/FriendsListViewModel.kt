package com.hfad.friends_list

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
    val emailsRef = database.getReference("uidToEmail")

    private val _approvedFriends = MutableLiveData<List<Friend>>()
    val approvedFriends: LiveData<List<Friend>> get() = _approvedFriends

    fun loadFriends(userKey: String) {
        val approvedFriendsRef = usersRef.child(userKey).child("friends").child("approved")

        approvedFriendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (childSnapshot in snapshot.children) {
                    val friendKey = childSnapshot.key ?: continue
                    val approved = childSnapshot.getValue(Boolean::class.java) ?: false
                    if (approved){
                        getUserEmail(friendKey) { email ->
                            friends.add(Friend(friendKey, email, approved))
                            Log.d("dasdsVM", "${email}")

                            _approvedFriends.value = friends
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FriendsList", "Database error: $error")
            }
        })
    }

    fun deleteFriend(userKey: String, friendKey: String) {
        usersRef.child(userKey).child("friends").child("approved").child(friendKey).removeValue()
            .addOnSuccessListener {
                Log.d("FriendsList", "Friend deleted successfully")
                loadFriends(userKey) // Reload friends list to reflect changes
            }
            .addOnFailureListener {
                Log.d("FriendsList", "Failed to delete friend: ${it.message}")
            }
    }

    private fun getUserEmail(userId: String, callback: (String) -> Unit) {
        emailsRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.getValue(String::class.java) ?: ""
                Log.d("FriendsList", "Fetched email for userId $userId: $email")
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
