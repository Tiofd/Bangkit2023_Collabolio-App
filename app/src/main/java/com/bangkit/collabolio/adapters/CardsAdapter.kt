package com.bangkit.collabolio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.collabolio.utilities.UserSwipe
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bangkit.collabolio.R
import com.bangkit.collabolio.ui.home.HomeViewModel

class CardsAdapter(context: Context?, resourceId: Int, users: List<UserSwipe>): ArrayAdapter<UserSwipe>(
    context!!, resourceId, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get the current user
        val user = getItem(position)
        val profile = user?.profile ?: emptyMap()
        val displayName = profile["displayName"] as? String
        val photoURL = profile["photoURL"] as? String
        val skills = profile["skills"] as ArrayList<Map<String, String>>
        for (skill in skills) {
            val skillName = skill["name"]
        }


        // get the layout if any or inflate the layout
        val finalView = convertView?: LayoutInflater.from(context).inflate(R.layout.item, parent, false)

        // get the ids of the fields
        val nameTV = finalView.findViewById<TextView>(R.id.tv_name)
        val emailTV = finalView.findViewById<TextView>(R.id.tv_email)
        val skillTV = finalView.findViewById<TextView>(R.id.tv_skills)
        val image = finalView.findViewById<ImageView>(R.id.iv_user)

        // add content in the layout
        nameTV.text = "$displayName"
        emailTV.text = "${user?.email}"
        skillTV.text = skills.joinToString(", ") { it["name"].toString() }
        val into = Glide.with(context)
            .load(photoURL)
            .into(image)

        return finalView
    }
}
