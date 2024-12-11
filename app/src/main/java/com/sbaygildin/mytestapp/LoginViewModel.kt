package com.sbaygildin.mytestapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.sbaygildin.search.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _loginStatus = MutableStateFlow<FirebaseUser?>(null)
    val loginStatus: StateFlow<FirebaseUser?> get() = _loginStatus

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    companion object {
        private const val TAG = "LoginViewModel"
    }

    fun createAccount(email: String, password: String) {
        firebaseRepository.createUser(email, password, { user ->
            _loginStatus.value = user
        }, { errorMessage ->
            _toastMessage.value = errorMessage
        }
        )
    }
    fun signIn(email: String, password: String) {
        firebaseRepository.signInUser(
            email,
            password,
            { user ->
                _loginStatus.value = user
            },
            { errorMessage ->
                _toastMessage.value = errorMessage
            })
    }

    fun resetPassword(email: String) {
        firebaseRepository.resetPassword(email, {
            _toastMessage.value = "Password reset email sent"
        }, { errorMessage ->
            _toastMessage.value = errorMessage
        })
    }
}

