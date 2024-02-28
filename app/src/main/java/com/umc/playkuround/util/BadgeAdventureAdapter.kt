package com.umc.playkuround.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.data.Badge
import com.umc.playkuround.databinding.ListItemBadgeAttendanceBinding

class AdListAdapterGrid(badgeList : ArrayList<String>): RecyclerView.Adapter<AdListAdapterGrid.ViewHolder>() {

    val badgeList = badgeList

    inner class ViewHolder(val binding : ListItemBadgeAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos : Int) {
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
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        holder.bind(position)

    }

    interface OnItemClickListener {

        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}