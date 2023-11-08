package com.bangkit.collabolio.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.collabolio.R
import com.bangkit.collabolio.ui.favorite.FavoriteFragment
import com.bangkit.collabolio.utilities.Favorite
import com.bangkit.collabolio.utilities.UserSwipe
import com.bumptech.glide.Glide

class FavoriteAdapter(val context: Context, val users: List<Favorite>): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    /*override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get the current user
        var user = getItem(position)
        val profile = user?.profile ?: emptyMap()
        val displayName = profile["displayName"] as? String
        val photoURL = profile["photoURL"] as? String
        val skills = profile["skills"] as ArrayList<Map<String, String>>
        for (skill in skills) {
            val skillName = skill["name"]
        }

        // get the layout if any or inflate the layout
        val finalView = convertView?: LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false)

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
    }*/

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTV: TextView = itemView.findViewById(R.id.tv_name)
        val emailTV: TextView = itemView.findViewById(R.id.tv_email)
        val skillTV: TextView = itemView.findViewById(R.id.tv_skills)
        val image: ImageView = itemView.findViewById(R.id.iv_user)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        val profile = user.profile ?: emptyMap()
        val displayName = profile["displayName"] as? String
        val photoURL = profile["photoURL"] as? String
        val skills = profile["skills"] as ArrayList<Map<String, String>>
        for (skill in skills) {
            val skillName = skill["name"]
        }
        holder.nameTV.text = "$displayName"
        holder.emailTV.text = "${user.email}"
        holder.skillTV.text = skills.joinToString(", ") { it["name"].toString() }
        val into = Glide.with(context)
            .load(photoURL)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return users.size
    }
}
