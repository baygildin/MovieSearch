package com.hfad.search_friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hfad.search.firebase.FirebaseRepository
import com.hfad.search.utils.encodeEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchFriendViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    var friendEmail = MutableLiveData<String>("")
    private val _friendInfo = MutableLiveData<String>("")
    val friendInfo: LiveData<String> get() = _friendInfo
    var isFriendAdded = false

    private val _isFriendFound = MutableLiveData<Boolean>(false)
    val isFriendFound: LiveData<Boolean> get() = _isFriendFound

    private val _favouritesListString = MutableLiveData<String>("")
    val favouritesListString: LiveData<String> get() = _favouritesListString
    private val auth = FirebaseAuth.getInstance()
    private val userKey = auth.currentUser?.uid ?: ""
    var friendUid = ""


    fun searchFriend() {
        val email = friendEmail.value ?: return
        firebaseRepository.emailToUidRef.child(encodeEmail(email.lowercase()))
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        friendUid = snapshot.getValue(String::class.java) ?: ""
                        if (friendUid.isNotEmpty()) {
                            _friendInfo.value = "Friend found with email: $email"
                            _isFriendFound.value = true
                        } else {
                            _friendInfo.value = "No user found with email: $email"
                            _isFriendFound.value = false
                        }
                    } else {
                        _friendInfo.value = "No user found with email: $email"
                        _isFriendFound.value = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("SearchFriend", "Database error: $error")
                }
            })
    }

    fun sendConnectionRequest() {
        if (friendUid.isEmpty()) return
        if (friendUid == userKey) {
            _friendInfo.value = "You cannot add yourself as a friend"
            return
        }

        firebaseRepository.usersRef.child(friendUid).child("friends").child("requested").child(userKey)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        _friendInfo.value =
                            "Friend request has already been sent to ${friendEmail.value}"
                    } else {
                        firebaseRepository.usersRef.child(friendUid).child("friends").child("requested").child(userKey)
                            .setValue(true).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    firebaseRepository.usersRef.child(userKey).child("friends").child("approved")
                                        .child(friendUid).setValue(true)
                                    _friendInfo.value =
                                        "Friend request sent to ${friendEmail.value}"
                                    isFriendAdded = true
                                } else {
                                    _friendInfo.value = "Failed to send friend request"
                                    isFriendAdded = false
                                }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("SearchFriend", "Database error: $error")
                }
            }
            )
    }
}
