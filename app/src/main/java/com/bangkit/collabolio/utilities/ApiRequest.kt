package com.bangkit.collabolio.utilities

import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import okhttp3.Request
import okhttp3.OkHttpClient


class ApiRequest : OkHttpClient() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getUsers(): Array<UserApi>? {
        val uid = firebaseAuth.currentUser?.uid

        if (uid == null) {
            return arrayOf()
        }

        val request = Request.Builder()
            .url("https://model-endpoint-onhqnm5xvq-uc.a.run.app/api/users/$uid/30")
            .build()

        val response = newCall(request).execute()

        val user = response.body?.string()?.let { Gson().fromJson(it, Array<UserApi>::class.java)}

        if (user != null) {
            return user
        } else {
            return arrayOf()
        }
    }
}