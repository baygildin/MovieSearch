package com.hfad.search.firebase


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirebaseRepository @Inject constructor() {
    private val database =
        Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")
    private val auth = FirebaseAuth.getInstance()
    val usersRef = database.getReference("users")
    val emailsRef = database.getReference("uidToEmail")
    val uidsRef = database.getReference("emailToUid")


    companion object {
        private const val TAG = "FirebaseRepository"
    }


    fun saveFavouritesToCloud(
        favouritesJson: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit
    ) {
        val userKey = auth.currentUser?.uid ?: return
        val userRef = usersRef.child(userKey)

        userRef.child("favourites").setValue(favouritesJson).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun fetchFavouritesFromCloud(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val userKey = auth.currentUser?.uid ?: return
        val userRef = usersRef.child(userKey)
        userRef.child("favourites").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jsonData = snapshot.value as String?
                if (!jsonData.isNullOrEmpty()) {
                    onSuccess(jsonData)
                } else {
                    onFailure(Exception("No data found"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.toException())
            }
        })
    }

    fun getUserEmail(
        userId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        emailsRef.child(userId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.getValue(String::class.java) ?: ""
                onSuccess(email)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.toException())
            }
        })
    }

    fun saveEmailInUserNode(uid: String, email: String) {
        usersRef.child(uid).child("email").setValue(email.lowercase())
    }


    fun saveEmailToUidMapping(email: String, uid: String) {
        val emailKey = email.replace(".", "*").lowercase()
        uidsRef.child(emailKey).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    uidsRef.child(emailKey).setValue(uid)
                } else {
                    Log.w(TAG, "saveEmailToUidMapping: Email already exists.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "saveEmailToUidMapping: ${error.message}")
            }
        })
    }

    fun saveUidToEmailMapping(uid: String, email: String) {
        emailsRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    emailsRef.child(uid).setValue(email.lowercase())
                } else {
                    Log.w(TAG, "saveUidToEmailMapping: UID already exists.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "saveUidToEmailMapping: ${error.message}")
            }
        })
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun createUser(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUid = auth.uid
                    if (userUid != null) {
                        saveEmailToUidMapping(
                            email.replace(".", "*").lowercase(),
                            userUid
                        )
                        saveUidToEmailMapping(userUid, email)
                        saveEmailInUserNode(userUid, email)
                        onSuccess(auth.currentUser)
                    } else {
                        onFailure("Failed to get user UID.")
                    }
                } else {
                    onFailure(task.exception?.message ?: "User registration failed.")
                }
            }
    }


    fun signInUser(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onFailure(task.exception?.message ?: "Authentication failed")
                }
            }
    }
}