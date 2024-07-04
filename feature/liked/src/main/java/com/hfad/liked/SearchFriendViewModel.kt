package com.hfad.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchFriendViewModel : ViewModel() {

    private val database = Firebase.database("https://moviesearchandmatch-60fa6-default-rtdb.europe-west1.firebasedatabase.app")
    private val usersRef = database.getReference("users")

    val friendEmail = MutableLiveData<String>("")
    private val _friendInfo = MutableLiveData<String>("")
    val friendInfo: LiveData<String> get() = _friendInfo

    private val _isFriendFound = MutableLiveData<Boolean>(false)
    val isFriendFound: LiveData<Boolean> get() = _isFriendFound

    fun searchFriend() {
        val email = friendEmail.value ?: return

        // Заменим точки в email для использования в ключах Realtime Database
        val key = email.replace(".", "")
        usersRef.child(key).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                _friendInfo.value = "Friend found: ${snapshot.child("email").value}"
                _isFriendFound.value = true
            } else {
                _friendInfo.value = "Friend not found"
                _isFriendFound.value = false
            }
        }.addOnFailureListener {
            _friendInfo.value = "Error searching friend"
            _isFriendFound.value = false
        }
    }

    fun sendConnectionRequest() {
        val email = friendEmail.value ?: return

        // Текущий пользователь
        val currentUser = "currentuser@gmail.com".replace(".", "") // Получить реальный email текущего пользователя

        // Заменим точки в email для использования в ключах Realtime Database
        val key = email.replace(".", "")

        // Добавляем запрос на подключение в базу данных
        usersRef.child(key).child("connections").child("pending").child(currentUser).setValue(true)
    }
}
