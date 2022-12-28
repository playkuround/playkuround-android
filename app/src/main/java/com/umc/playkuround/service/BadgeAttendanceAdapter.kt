package com.umc.playkuround.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ListItemBadgeAttendanceBinding

class ListAdapterGrid(): RecyclerView.Adapter<ListAdapterGrid.ViewHolder>() {

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

    override fun getItemCount() = 5

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        holder.bind()

    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {


        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}

