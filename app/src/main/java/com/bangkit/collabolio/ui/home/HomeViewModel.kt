package com.bangkit.collabolio.ui.home

import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.collabolio.R
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.bangkit.collabolio.adapters.CardsAdapter
import com.bangkit.collabolio.databinding.FragmentHomeBinding
import com.bangkit.collabolio.utilities.ApiRequest
import com.bangkit.collabolio.utilities.UserSwipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {

    private val _homeProfileData = MutableLiveData<UserSwipe?>()
    val homeProfileData: LiveData<UserSwipe?> = _homeProfileData
    private var binding: FragmentHomeBinding? = null

    fun getUserSwipe() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        if (userId != null) {
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val homeProfile = documentSnapshot.toObject(UserSwipe::class.java)
                        _homeProfileData.value = homeProfile
                        Log.d("HomeViewModel", homeProfile.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("HomeViewModel", "error : $exception")
                }
        }
    }
}