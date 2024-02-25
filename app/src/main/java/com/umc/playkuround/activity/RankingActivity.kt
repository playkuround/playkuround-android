package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityRankingBinding
import com.umc.playkuround.databinding.ItemRankBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.network.RankItem
import com.umc.playkuround.network.ScoreAPI
import com.umc.playkuround.network.Top100Response
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import java.text.NumberFormat

class RankingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRankingBinding
    private val rankInfo = ArrayList<RankItem>()
    private var myRank = -1
    private var myScore = 0

    inner class RankingRVAdapter(private val rank : ArrayList<RankItem>) : RecyclerView.Adapter<RankingRVAdapter.ViewHolder>() {

        inner class ViewHolder(val binding : ItemRankBinding) : RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
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
    }

    private fun initView() {
        binding.rankingBackgroundIv.visibility = View.VISIBLE

        binding.rankingGoldMedalIv.visibility = View.VISIBLE
        binding.rankingSilverMedalIv.visibility = View.VISIBLE
        binding.rankingBronzeMedalIv.visibility = View.VISIBLE

        binding.rankingGoldMedalNicknameTv.visibility = View.VISIBLE
        binding.rankingSilverMedalNicknameTv.visibility = View.VISIBLE
        binding.rankingBronzeMedalNicknameTv.visibility = View.VISIBLE

        binding.rankingGoldMedalScoreTv.visibility = View.VISIBLE
        binding.rankingSilverMedalScoreTv.visibility = View.VISIBLE
        binding.rankingBronzeMedalScoreTv.visibility = View.VISIBLE

        binding.rankingInfoTitleLl.visibility = View.VISIBLE
        binding.rankingMyInfoLl.visibility = View.VISIBLE
        binding.rankingRankRv.visibility = View.VISIBLE
    }

    private fun getRankData() {
        val loadingDialog = LoadingDialog(this)
        loadingDialog.show()
        val scoreAPI = ScoreAPI()
        scoreAPI.setOnResponseListener(object : ScoreAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                loadingDialog.dismiss()
                if(isSuccess) {
                    if(body is Top100Response) {
                        if(body.responseData.rank.isEmpty()) {
                            showNoData()
                        } else {
                            rankInfo.addAll(body.responseData.rank)
                            myRank = body.responseData.myRank.ranking
                            myScore = body.responseData.myRank.score
                            initView()
                            showTopPlayersInfo()
                            showMyInfo()
                            showRanks()
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, errorLog, Toast.LENGTH_SHORT).show()
                }
            }
        }).getTop100(user.getAccessToken())
    }

    private fun showNoData() {
        binding.rankingBackgroundIv.visibility = View.VISIBLE
        binding.rankingNoRankingTv.visibility = View.VISIBLE
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