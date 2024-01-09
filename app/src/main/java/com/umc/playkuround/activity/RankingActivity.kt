package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.data.Ranking
import com.umc.playkuround.databinding.ActivityRankingBinding
import com.umc.playkuround.databinding.ItemRankBinding
import java.text.NumberFormat

class RankingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRankingBinding
    private val rankInfo = ArrayList<RankInfo>()
    private var myRank = -1
    private var myScore = 0

    data class RankInfo(
        val nickname : String,
        val score : Int
    )

    inner class RankingRVAdapter(private val rank : ArrayList<RankInfo>) : RecyclerView.Adapter<RankingRVAdapter.ViewHolder>() {

        inner class ViewHolder(val binding : ItemRankBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(pos : Int) {
                binding.itemRankIndex.text = (pos + 1).toString()
                binding.itemRankNickname.text = rank[pos].nickname

                val formatter = NumberFormat.getNumberInstance()
                binding.itemRankScore.text = formatter.format(rank[pos].score)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
            val binding = ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount() : Int = rank.size

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rankingBackBtn.setOnClickListener {
            finish()
        }

        binding.rankingInfoBtn.setOnClickListener {
            openRankingInfoDialog()
        }

        getRankData()
        if(rankInfo.isEmpty()) {
            showNoData()
        } else {
            showTopPlayersInfo()
            showMyInfo()
            showRanks()
        }
    }

    private fun getRankData() {
        for(i in 1..2) {
            rankInfo.add(RankInfo("덕쿠$i", 10100 - i*100))
        }
    }

    private fun showNoData() {
        binding.rankingNoRankingTv.visibility = View.VISIBLE

        binding.rankingGoldMedalIv.visibility = View.INVISIBLE
        binding.rankingSilverMedalIv.visibility = View.INVISIBLE
        binding.rankingBronzeMedalIv.visibility = View.INVISIBLE

        binding.rankingGoldMedalNicknameTv.visibility = View.INVISIBLE
        binding.rankingSilverMedalNicknameTv.visibility = View.INVISIBLE
        binding.rankingBronzeMedalNicknameTv.visibility = View.INVISIBLE

        binding.rankingGoldMedalScoreTv.visibility = View.INVISIBLE
        binding.rankingSilverMedalScoreTv.visibility = View.INVISIBLE
        binding.rankingBronzeMedalScoreTv.visibility = View.INVISIBLE

        binding.rankingInfoTitleLl.visibility = View.INVISIBLE
        binding.rankingMyInfoLl.visibility = View.INVISIBLE
        binding.rankingRankRv.visibility = View.INVISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun showTopPlayersInfo() {
        val formatter = NumberFormat.getNumberInstance()
        if(rankInfo.size > 0) {
            binding.rankingGoldMedalNicknameTv.text = rankInfo[0].nickname
            if(rankInfo[0].score >= 10000) {
                binding.rankingGoldMedalScoreTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
            }
            binding.rankingGoldMedalScoreTv.text = formatter.format(rankInfo[0].score) + "점"
        }
        if(rankInfo.size > 1) {
            binding.rankingSilverMedalNicknameTv.text = rankInfo[1].nickname
            if(rankInfo[1].score >= 10000) {
                binding.rankingSilverMedalScoreTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
            }
            binding.rankingSilverMedalScoreTv.text = formatter.format(rankInfo[1].score) + "점"
        } else {
            binding.rankingSilverMedalNicknameTv.text = ""
            binding.rankingSilverMedalScoreTv.text = ""
        }
        if(rankInfo.size > 2) {
            binding.rankingBronzeMedalNicknameTv.text = rankInfo[2].nickname
            if(rankInfo[2].score >= 10000) {
                binding.rankingBronzeMedalScoreTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
            }
            binding.rankingBronzeMedalScoreTv.text = formatter.format(rankInfo[2].score) + "점"
        } else {
            binding.rankingBronzeMedalNicknameTv.text = ""
            binding.rankingBronzeMedalScoreTv.text = ""
        }
    }

    private fun showMyInfo() {
        if(myRank == -1) binding.rankingMyRankTv.text = "-"
        else binding.rankingMyRankTv.text = myRank.toString()

        binding.rankingMyScoreTv.text = myScore.toString()
    }

    private fun showRanks() {
        binding.rankingRankRv.adapter = RankingRVAdapter(rankInfo)
        binding.rankingRankRv.layoutManager = LinearLayoutManager(this)
        binding.rankingRankRv.isVerticalScrollBarEnabled = rankInfo.size > 5
        binding.rankingRankRv.isScrollbarFadingEnabled = false
    }

    private fun openRankingInfoDialog() {
        val infoDialog = Dialog(this, R.style.TransparentDialogTheme)
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        infoDialog.setContentView(R.layout.dialog_ranking_info)
        infoDialog.show()
    }

}