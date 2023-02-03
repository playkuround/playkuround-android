package com.umc.playkuround.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.data.Badge
import com.umc.playkuround.databinding.ListItemBadgeAttendanceBinding

class ListAdapterGrid(badgeList : ArrayList<String>): RecyclerView.Adapter<ListAdapterGrid.ViewHolder>() {

    val badgeList = badgeList

    inner class ViewHolder(val binding : ListItemBadgeAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos : Int) {
            binding.listItemBadgeLoked.setImageResource(R.drawable.badge_locked)
            when(pos) {
                0 -> {
                    if(badgeList.contains(Badge.ATTENDANCE_1))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_attendance_1)
                }
                1 -> {
                    if(badgeList.contains(Badge.ATTENDANCE_3))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_attendance_3)
                }
                2 -> {
                    if(badgeList.contains(Badge.ATTENDANCE_7))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_attendance_7)
                }
                3 -> {
                    if(badgeList.contains(Badge.ATTENDANCE_30))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_attendance_30)
                }
                4 -> {
                    if(badgeList.contains(Badge.ATTENDANCE_100))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_attendance_100)
                }
                5 -> {
                    if(badgeList.contains(Badge.ATTENDANCE_FOUNDATION_DAY))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_attendance_foundation_day)
                }
            }
        }

    }

//    class GridAdapter(val layout: View): RecyclerView.ViewHolder(layout)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBadgeAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = 6

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        holder.bind(position)

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

