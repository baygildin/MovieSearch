package com.hfad.liked

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.search.model.FavouriteItem

class LikedMediaAdapter(
    var items: List<FavouriteItem>
) : RecyclerView.Adapter<LikedMediaAdapter.LikedMediaViewHolder>() {

    inner class LikedMediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imdbIdTextView: TextView = view.findViewById(R.id.imdbIdTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedMediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite, parent, false)
        return LikedMediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikedMediaViewHolder, position: Int) {
        val item = items[position]
        holder.imdbIdTextView.text = item.imdbId
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
