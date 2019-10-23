package com.languagexx.simplenotes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.languagexx.simplenotes.R
import com.languagexx.simplenotes.entity.Note


class UserAdapter(val note: List<Note>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return note.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = note.get(position)
        holder.txtTitle?.text = note.title
        holder.cardView?.setBackgroundColor(Color.parseColor(note.color))
    }


    inner class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {

        var txtTitle: TextView? = null
        var cardView: TextView? = null

        init {
            this.txtTitle = row.findViewById(R.id.txtTitle)
            this.cardView = row.findViewById(R.id.cardView)
        }
    }


}