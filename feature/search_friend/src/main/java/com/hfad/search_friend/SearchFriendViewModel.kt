package com.hfad.search_friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchFriendViewModel : ViewModel() {

    private val database = Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")
    private val usersRef = database.getReference("users")
    private val emailToUidRef = database.getReference("emailToUid")
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
        val email = friendEmail.value?: return
        emailToUidRef.child(emailToValidFbKey(email.lowercase())).addListenerForSingleValueEvent(object : ValueEventListener {
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

        usersRef.child(friendUid).child("friends").child("requested").child(userKey).setValue(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                usersRef.child(userKey).child("friends").child("approved").child(friendUid).setValue(true)
                _friendInfo.value = "Friend request sent to ${friendEmail.value}"
                isFriendAdded = true
            } else {
                _friendInfo.value = "Failed to send friend request"
                isFriendAdded = false
            }
        }
    }

    private fun emailToValidFbKey(email: String): String {
        return email.replace(".", "*")
    }
}
