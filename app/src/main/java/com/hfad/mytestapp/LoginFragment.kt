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
        Log.d("firebaseEnter","LoginFragment1")
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        Log.d("firebaseEnter","LoginFragment4")

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            signIn(email, password)
        }

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            createAccount(email, password)
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
                    saveEmailToUidMapping(email, userKey)
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
            // Navigate to the main activity
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
    fun saveEmailToUidMapping(email: String, uid: String){
        val emailKey = emailToValidFbKey(email)
        emailToUidRef.child(emailKey).setValue(uid)
    }
    fun saveUidToEmailMapping(uid: String, email: String){
        val emailKey = email
        uidToEmailRef.child(uid).setValue(emailKey)
    }
    fun saveEmailInUserNode(uid: String, email: String){
        usersRef.child(uid).child("email").setValue(email)

    }
    fun emailToValidFbKey(str: String) = str.replace(".", "*")
}
