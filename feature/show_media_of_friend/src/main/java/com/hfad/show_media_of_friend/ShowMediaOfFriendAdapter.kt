package com.hfad.show_media_of_friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.search.model.FavouriteItem

class ShowMediaOfFriendAdapter(
    var items: List<FavouriteItem>,
    private val onItemClick: (FavouriteItem) -> Unit
) : RecyclerView.Adapter<ShowMediaOfFriendAdapter.ShowMediaOfFriendViewHolder>() {
    inner class ShowMediaOfFriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imdbTextView: TextView = view.findViewById(R.id.imdbIdTextView)

        init {
            view.setOnClickListener {
                onItemClick(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowMediaOfFriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite, parent, false)
        return ShowMediaOfFriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowMediaOfFriendViewHolder, position: Int) {
        val item = items[position]
        holder.imdbTextView.text = "${item.title} (${item.year})"
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<FavouriteItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}