package com.hfad.search.firebase


import com.google.firebase.auth.FirebaseAuth
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
    val emailToUidRef = database.getReference("emailToUid")

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
    ){
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
}