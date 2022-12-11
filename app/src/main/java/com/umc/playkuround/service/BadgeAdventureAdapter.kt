package com.umc.playkuround.service

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ListItemBadgeAttendanceBinding
import com.umc.playkuround.dialog.SlideUpDialog

class AdListAdapterGrid(): RecyclerView.Adapter<AdListAdapterGrid.ViewHolder>() {

    inner class ViewHolder(val binding : ListItemBadgeAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.listItemBadgeLoked.setImageResource(R.drawable.badge_locked)
        }

    }

//    class GridAdapter(val layout: View): RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBadgeAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()

    }
}