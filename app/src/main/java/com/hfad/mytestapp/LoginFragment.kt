package com.hfad.movie_search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseUser
import com.hfad.movie_search.databinding.FragmentLoginBinding
import com.hfad.mytestapp.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    companion object {
        private const val TAG = "LoginFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().lowercase()
            val password = binding.passwordEditText.text.toString()
            if (checkIsTextBlank(email, password)) {
                Toast.makeText(context, "Email and password must not be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.signIn(email, password)
            }
        }
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().lowercase()
            val password = binding.passwordEditText.text.toString()
            if (checkIsTextBlank(email, password)) {
                Toast.makeText(context, "Email and password must not be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.createAccount(email, password)
            }
        }

        binding.resetPasswordButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().lowercase()
            if (email.isEmpty()) {
                Toast.makeText(context, "Please enter your email to reset password", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.resetPassword(email)
            }
        }


        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { user ->
            updateUI(user)
        })
        viewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun checkIsTextBlank(text1: String?, text2: String?): Boolean {
        return text1.isNullOrBlank() || text2.isNullOrBlank()
    }
}
