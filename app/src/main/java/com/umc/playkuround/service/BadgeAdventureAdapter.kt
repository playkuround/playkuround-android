package com.umc.playkuround.service

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.data.Badge
import com.umc.playkuround.databinding.ListItemBadgeAttendanceBinding
import com.umc.playkuround.dialog.SlideUpDialog

class AdListAdapterGrid(badgeList : ArrayList<String>): RecyclerView.Adapter<AdListAdapterGrid.ViewHolder>() {

    val badgeList = badgeList

    inner class ViewHolder(val binding : ListItemBadgeAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos : Int) {
            binding.listItemBadgeLoked.setImageResource(R.drawable.badge_locked)
            when(pos) {
                0 -> {
                    if(badgeList.contains(Badge.ADVENTURE_1))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_adventure_1)
                }
                1 -> {
                    if(badgeList.contains(Badge.ADVENTURE_5))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_adventure_5)
                }
                2 -> {
                    if(badgeList.contains(Badge.ADVENTURE_10))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_adventure_10)
                }
                3 -> {
                    if(badgeList.contains(Badge.ADVENTURE_30))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_adventure_30)
                }
                4 -> {
                    if(badgeList.contains(Badge.ENGINEER))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_engineer)
                }
                5 -> {
                    if(badgeList.contains(Badge.ARTIST))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_artist)
                }
                6 -> {
                    if(badgeList.contains(Badge.CEO))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_ceo)
                }
                7 -> {
                    if(badgeList.contains(Badge.NATIONAL_PLAYER))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_national_player)
                }
                8 -> {
                    if(badgeList.contains(Badge.CONQUEROR))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_conqueror)
                }
                9 -> {
                    if(badgeList.contains(Badge.NEIL_ARMSTRONG))
                        binding.listItemBadgeLoked.setImageResource(R.drawable.badge_neil_armstrong)
                }
            }
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