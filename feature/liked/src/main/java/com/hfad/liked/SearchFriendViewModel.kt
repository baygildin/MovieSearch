

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchFriendViewModel : ViewModel() {

    private val database = Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")
    private val usersRef = database.getReference("users")
    var registeredEmail: String = ""

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
    var friendKey = ""

    init {
        Log.d("UserKey", "UserKey: $userKey")
        getUserEmail(userKey) { email ->
            if (email != null) {
                Log.d("UserEmail", "Email: $email")
                registeredEmail = email
            } else {
                Log.d("UserEmail", "Failed to get email")
            }
        }
    }

    fun searchFriend() {
        val email = emailToValidFbKey(friendEmail.value ?: return)
        friendKey = email
        val userRef = usersRef.child(friendKey)
        singleOnChangeListener(userRef)
    }

    fun sendConnectionRequest() {
        val email = friendEmail.value ?: return
        val friendKey = emailToValidFbKey(email)
        if (
            usersRef.child(friendKey).child("friends").child("requested").child(userKey) !=null
        ) {
            _friendInfo.value = "A friend request has already been sent to $email"
            isFriendAdded = true
        } else {
            usersRef.child(userKey).child("friends").child("approved").child(friendKey).setValue(true)
            usersRef.child(friendKey).child("friends").child("requested").child(userKey).setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isFriendAdded = true
                        _friendInfo.value = "Friend request sent to $email"
                    } else {
                        isFriendAdded = false
                        _friendInfo.value = "Failed to send friend request"
                    }
                }
        }

    }

    private fun singleOnChangeListener(dRef: DatabaseReference) {
        dRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.value as Map<*, *>
                    _friendInfo.value = "Friend found: ${user["email"]}"
                    _isFriendFound.value = true
                } else {
                    _friendInfo.value = "Friend not found"
                    _isFriendFound.value = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("deleteee", "Database error: $error")
            }
        })
    }

    fun emailToValidFbKey(str: String) = str.replace(".", "*")

    fun getUserEmail(userKey: String, callback: (String?) -> Unit) {
        val userRef = usersRef.child(userKey)
        userRef.child("email").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val email = task.result?.getValue(String::class.java)
                callback(email)
            } else {
                Log.d("getUserEmail", "Error getting email: ${task.exception?.message}")
                callback(null)
            }
        }
    }

}
