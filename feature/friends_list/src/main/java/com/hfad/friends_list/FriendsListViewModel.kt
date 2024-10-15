package com.hfad.friends_list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hfad.search.firebase.FirebaseRepository
import com.hfad.search.model.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FriendsListViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _approvedFriends = MutableStateFlow<List<Friend>>(emptyList())
    val approvedFriends: StateFlow<List<Friend>> get() = _approvedFriends
    private val auth = FirebaseAuth.getInstance()
    private val userKey = auth.currentUser?.uid ?: ""

    fun loadFriends() {
        val approvedFriendsRef =
            firebaseRepository.usersRef.child(userKey).child("friends").child("approved")

        approvedFriendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (childSnapshot in snapshot.children) {
                    val friendKey = childSnapshot.key ?: continue
                    val approved = childSnapshot.getValue(Boolean::class.java) ?: false
                    if (approved) {
                        getUserEmail(friendKey) { email ->
                            friends.add(Friend(friendKey, email, approved))
                            if (friends.size == snapshot.childrenCount.toInt()) {
                                _approvedFriends.value = friends
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FriendsList", "Database error: $error")
            }
        })
    }

    fun signOut() {
        firebaseRepository.signOut()
    }

    fun deleteFriend(friendKey: String) {
        firebaseRepository.usersRef.child(userKey).child("friends").child("approved")
            .child(friendKey).removeValue()
            .addOnSuccessListener {
                loadFriends()
            }
            .addOnFailureListener {
            }
        firebaseRepository.usersRef.child(friendKey).child("friends").child("approved")
            .child(userKey).removeValue()
            .addOnSuccessListener {
                loadFriends()
            }
            .addOnFailureListener {
                Log.d(
                    "FriendsList",
                    "Failed to delete user from friend`s friendlist: ${it.message}"
                )
            }
    }

    private fun getUserEmail(userId: String, callback: (String) -> Unit) {
        firebaseRepository.emailsRef.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = snapshot.getValue(String::class.java) ?: ""
                    callback(email)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback("")
                }
            })
    }
}
