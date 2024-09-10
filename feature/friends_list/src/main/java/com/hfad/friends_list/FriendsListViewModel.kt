package com.hfad.friends_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hfad.search.firebase.FirebaseRepository
import com.hfad.search.model.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FriendsListViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    
    private val _approvedFriends = MutableLiveData<List<Friend>>()
    val approvedFriends: LiveData<List<Friend>> get() = _approvedFriends

    fun loadFriends(userKey: String) {
        val approvedFriendsRef = firebaseRepository.usersRef.child(userKey).child("friends").child("approved")

        approvedFriendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (childSnapshot in snapshot.children) {
                    val friendKey = childSnapshot.key ?: continue
                    val approved = childSnapshot.getValue(Boolean::class.java) ?: false
                    if (approved){
                        getUserEmail(friendKey) { email ->
                            friends.add(Friend(friendKey, email, approved))
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
        firebaseRepository.usersRef.child(userKey).child("friends").child("approved").child(friendKey).removeValue()
            .addOnSuccessListener {
                Log.d("FriendsList", "Friend deleted successfully")
                loadFriends(userKey)
            }
            .addOnFailureListener {
                Log.d("FriendsList", "Failed to delete friend: ${it.message}")
            }
    }

    private fun getUserEmail(userId: String, callback: (String) -> Unit) {
        firebaseRepository.emailsRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
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
}
