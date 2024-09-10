package com.hfad.search_friend

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hfad.search.firebase.FirebaseRepository
import com.hfad.search.model.FriendRequestsInfo
import com.hfad.search.utils.encodeEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class SearchFriendViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    var friendEmail = MutableStateFlow<String>("")
    private val _friendInfo = MutableStateFlow<FriendRequestsInfo>(FriendRequestsInfo("", ""))
    val friendInfo: StateFlow<FriendRequestsInfo> get() = _friendInfo
    var isFriendAdded = false

    private val _isFriendFound = MutableStateFlow<Boolean>(false)
    val isFriendFound: StateFlow<Boolean> get() = _isFriendFound

    private val _favouritesListString = MutableStateFlow<String>("")
    val favouritesListString: StateFlow<String> get() = _favouritesListString
    private val auth = FirebaseAuth.getInstance()
    private val userKey = auth.currentUser?.uid ?: ""
    var friendUid = ""

    companion object {
        const val FRIEND_FOUND = "friend_found_with_email"
        const val NO_USER_FOUND = "no_user_found_with_email"
        const val CANNOT_ADD_YOURSELF = "cannot_add_yourself"
        const val FRIEND_REQUEST_SENT = "friend_request_sent"
        const val FRIEND_REQUEST_ALREADY_SENT = "friend_request_already_sent"
        const val FAILED_TO_SEND_FRIEND_REQUEST = "failed_to_send_friend_request"
    }


    fun searchFriend() {
        val email = friendEmail.value
        firebaseRepository.uidsRef.child(encodeEmail(email.lowercase()))
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        friendUid = snapshot.getValue(String::class.java) ?: ""
                        if (friendUid.isNotEmpty()) {
                            _friendInfo.value = FriendRequestsInfo(FRIEND_FOUND, email)
                            _isFriendFound.value = true
                        } else {
                            _friendInfo.value = FriendRequestsInfo(NO_USER_FOUND, email)
                            _isFriendFound.value = false
                        }
                    } else {
                        _friendInfo.value = FriendRequestsInfo(NO_USER_FOUND, email)
                        _isFriendFound.value = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("SearchFriend", "Database error: $error")
                }
            })
    }

    fun sendConnectionRequest() {
        if (friendUid.isEmpty()) return
        if (friendUid == userKey) {
            _friendInfo.value = FriendRequestsInfo(CANNOT_ADD_YOURSELF, "")
            return
        }

        firebaseRepository.usersRef.child(friendUid).child("friends").child("requested").child(userKey)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        _friendInfo.value = FriendRequestsInfo(FRIEND_REQUEST_ALREADY_SENT, friendEmail.value)
                    } else {
                        firebaseRepository.usersRef.child(friendUid).child("friends").child("requested").child(userKey)
                            .setValue(true).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    firebaseRepository.usersRef.child(userKey).child("friends").child("approved")
                                        .child(friendUid).setValue(true)
                                    _friendInfo.value = FriendRequestsInfo(FRIEND_REQUEST_SENT, friendEmail.value)
                                    isFriendAdded = true
                                } else {
                                    _friendInfo.value = FriendRequestsInfo(FAILED_TO_SEND_FRIEND_REQUEST, "")
                                    isFriendAdded = false
                                }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("SearchFriend", "Database error: $error")
                }
            }
            )
    }
}