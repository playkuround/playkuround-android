package com.umc.playkuround.service

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.databinding.ItemRankBinding

class RankingRVAdapter : RecyclerView.Adapter<RankingRVAdapter.ViewHolder>() {

    private lateinit var parent : ViewGroup
    private val users = ArrayList<RankUser>()

    inner class RankUser(
        val rank : Int,
        val nickname : String,
        val score : Int
    )

    inner class ViewHolder(val binding : ItemRankBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : RankUser) {
            binding.rankingItemRankTv.text = user.rank.toString()
            binding.rankingItemNameTv.text = user.nickname
            binding.rankingItemScoreTv.text = user.score.toString()
            when(user.rank) {
                1-> {
                    binding.rankingItemRankTv.background = makeDrawable("#FFE67E") // gold
                } 2-> {
                    binding.rankingItemRankTv.background = makeDrawable("#DBDBDB") // silver
                } 3-> {
                    binding.rankingItemRankTv.background = makeDrawable("#E0A03F") // bronze
                } else -> {
                    binding.rankingItemRankTv.background = null
                }
            }
        }
    }

    init {
        getUsers()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        this.parent = parent
        val binding = ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() : Int = users.size

    private fun getUsers() {
        for(i in 1..100) {
            users.add(RankUser(i, "건덕이$i", 1000 - i))
        }
    }

    private fun makeDrawable(color : String) : GradientDrawable {
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor(color))
        drawable.shape = GradientDrawable.OVAL
        val size : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, parent.resources.displayMetrics).toInt()
        drawable.setSize(size, size)
        return drawable
    }

}