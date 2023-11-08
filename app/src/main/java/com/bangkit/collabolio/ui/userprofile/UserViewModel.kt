package com.bangkit.collabolio.ui.userprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.collabolio.response.UserProfileResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {

    private val _userProfileData = MutableLiveData<UserProfileResponse?>()
    val userProfileData : LiveData<UserProfileResponse?> = _userProfileData

    fun getUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        if (userId != null) {
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val userProfile = documentSnapshot.toObject(UserProfileResponse::class.java)
                        _userProfileData.value = userProfile
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("UserViewModel", "error : $exception")
                }
        }
    }
}