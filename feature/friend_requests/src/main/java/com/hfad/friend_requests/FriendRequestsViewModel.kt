package com.hfad.friend_requests


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hfad.search.firebase.FirebaseRepository
import com.hfad.search.model.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendRequestsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _friendRequests = MutableStateFlow<List<Friend>>(emptyList())
    val friendRequests: StateFlow<List<Friend>> get() = _friendRequests

    private val _operationStatus = MutableSharedFlow<String>()
    val operationStatus: SharedFlow<String> get() = _operationStatus

    fun loadFriendRequests(userKey: String) {
        val requestedFriendsRef =
            firebaseRepository.usersRef.child(userKey).child("friends").child("requested")

        requestedFriendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (childSnapshot in snapshot.children) {
                    val friendKey = childSnapshot.key ?: continue
                    firebaseRepository.getUserEmail(friendKey, onSuccess = { email ->
                        friends.add(Friend(friendKey, email, approved = false))
                        _friendRequests.value = friends
                    }, onFailure = {
                        viewModelScope.launch {
                            _operationStatus.emit("Error fetching email: ${it.message}")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch {
                    _operationStatus.emit(
                        "Database error: ${error.message}"
                    )
                }
            }
        })
    }

    fun approveFriend(userKey: String, friendId: String) {
        val userRef = firebaseRepository.usersRef.child(userKey).child("friends")
        userRef.child("requested").child(friendId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userRef.child("approved").child(friendId).setValue(true)
                    .addOnCompleteListener { approvalTask ->
                        if (approvalTask.isSuccessful) {
                            _friendRequests.value = _friendRequests.value.filterNot { it.id == friendId }
                            viewModelScope.launch {
                                _operationStatus.emit("Friend request approved successfully")
                            }
                        } else {
                            viewModelScope.launch {
                                _operationStatus.emit("Error approving friend: ${approvalTask.exception?.message}")
                            }
                        }
                    }
            } else {
                viewModelScope.launch {
                    _operationStatus.emit("Error removing friend request: ${task.exception?.message}")
                }
            }
        }
    }
}
