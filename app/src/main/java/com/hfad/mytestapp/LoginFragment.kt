package com.hfad.movie_search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hfad.movie_search.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().lowercase()
            val password = binding.passwordEditText.text.toString()
            if (checkIsTextBlank(email, password)) {
                Toast.makeText(context, "Email and password must not be empty", Toast.LENGTH_SHORT).show()
            }
            else {
                signIn(email, password)
            }
        }
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().lowercase()
            val password = binding.passwordEditText.text.toString()
            if (checkIsTextBlank(email, password)) {
                Toast.makeText(context, "Email and password must not be empty", Toast.LENGTH_SHORT).show()
            }
            else {
                createAccount(email, password)
            }
        }

        return binding.root
    }
    private val database = Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")
    private val emailToUidRef = database.getReference("emailToUid")
    private val uidToEmailRef = database.getReference("uidToEmail")
    private val usersRef = database.getReference("users")



    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val auth = FirebaseAuth.getInstance()
                    val userKey = auth.currentUser?.uid ?: ""
                    saveEmailToUidMapping(emailToValidFbKey(email), userKey)
                    saveUidToEmailMapping(userKey, email)
                    saveEmailInUserNode(userKey, email)
                    updateUI(user)


                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

    fun saveEmailToUidMapping(email: String, uid: String){
        val emailKey = emailToValidFbKey(email.lowercase())
        emailToUidRef.child(emailKey).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    emailToUidRef.child(emailKey).setValue(uid)
                } else {
                    Log.w(TAG, "saveEmailToUidMapping: Email already exists.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "saveEmailToUidMapping: ${error.message}")
            }
        })
    }

    fun saveUidToEmailMapping(uid: String, email: String){
        uidToEmailRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    uidToEmailRef.child(uid).setValue(email.lowercase())
                } else {
                    Log.w(TAG, "saveUidToEmailMapping: UID already exists.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "saveUidToEmailMapping: ${error.message}")
            }
        })
    }

    //    fun saveEmailToUidMapping(email: String, uid: String){
//        val emailKey = emailToValidFbKey(email.lowercase())
//        emailToUidRef.child(emailKey).setValue(uid)
//    }
//    fun saveUidToEmailMapping(uid: String, email: String){
//        val emailKey = email.lowercase()
//        uidToEmailRef.child(uid).setValue(emailKey)
//    }
    fun saveEmailInUserNode(uid: String, email: String){
        usersRef.child(uid).child("email").setValue(email.lowercase())
    }
    fun emailToValidFbKey(str: String) = str.replace(".", "*").lowercase()

    private fun checkIsTextBlank(text1: String?, text2: String?): Boolean{
        if (text1.isNullOrBlank() || text2.isNullOrBlank()) {
            return true
        }
        else return false
    }
}
