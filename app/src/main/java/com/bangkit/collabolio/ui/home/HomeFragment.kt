package com.bangkit.collabolio.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bangkit.collabolio.R
import com.bangkit.collabolio.adapters.CardsAdapter
import com.bangkit.collabolio.databinding.FragmentHomeBinding
import com.bangkit.collabolio.utilities.ApiRequest
import com.bangkit.collabolio.utilities.UserSwipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    @SuppressLint("StaticFieldLeak")
    val db = FirebaseFirestore.getInstance()

    private var cardsAdapter: ArrayAdapter<UserSwipe>? = null
    private var rowItems = ArrayList<UserSwipe>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDocumentRef = db.collection("users").document(currentUser!!.uid)
        userDocumentRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userDocument = task.result
                userDocument.getString("username")
                userDocument.getString("email")
                userDocument.getString("profile.photoURL")

                fillingTheItemsByApi()
            } else {
                Log.d("PENG", "maaf ada error : ${task.exception}")
            }
        }

        cardsAdapter = CardsAdapter(context, resourceId = R.layout.item, users = rowItems)
        binding.frame.adapter = cardsAdapter
        binding.frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                rowItems.removeAt(0)
                cardsAdapter?.notifyDataSetChanged()
            }

            override fun onLeftCardExit(p0: Any?) {
                //
            }

            override fun onRightCardExit(p0: Any?) {
                val selectedUser = p0 as UserSwipe
                val selectedUserId = selectedUser.uid
                if (!selectedUserId.isNullOrEmpty()) {
                    Toast.makeText(context, "${selectedUser.profile?.get("displayName")} ditambahkan ke favorit",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAdapterAboutToEmpty(p0: Int) {
                fillingTheItemsByApi()
            }

            override fun onScroll(p0: Float) {
                //
            }

        })
    }

    private suspend fun apiReqForUids(): MutableList<String> {
        return withContext(Dispatchers.IO) {
            val apiRequest = ApiRequest()
            val uidsResult = mutableListOf<String>()
            val users = apiRequest.getUsers()
            for (user in users!!) {
                uidsResult.add(user.uid.toString())
            }
            uidsResult
        }
    }

    private fun fillingTheItemsByApi() {
        CoroutineScope(Dispatchers.Main).launch {
            val uids = apiReqForUids()
            val cardsQuery = db.collection("users").whereIn("uid", uids)
            try {
                val documents = withContext(Dispatchers.IO) { cardsQuery.get().await() }
                for (document in documents) {
                    val user = document.toObject(UserSwipe::class.java)
                    if (!rowItems.contains(user)) {
                        rowItems.add(user)
                        cardsAdapter?.notifyDataSetChanged()
                    }
                }
            } catch (exception: Exception) {
                Log.d("KARTU", "get failed with : ", exception)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}