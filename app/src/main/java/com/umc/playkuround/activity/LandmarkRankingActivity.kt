package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.databinding.ActivityLandmarkRankingBinding
import com.umc.playkuround.databinding.ItemRankBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.network.RankItem
import com.umc.playkuround.network.ScoreAPI
import com.umc.playkuround.network.Top100Response
import com.umc.playkuround.util.PlayKuApplication
import java.text.NumberFormat

class LandmarkRankingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLandmarkRankingBinding
    private var landmarkId = -1
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
        binding = ActivityLandmarkRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        landmarkId = intent.getIntExtra("landmarkId", -1)

        binding.landmarkRankingBackBtn.setOnClickListener {
            finish()
        }

        getRankData()
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
                            showMyInfo()
                            showRanks()
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, errorLog, Toast.LENGTH_SHORT).show()
                }
            }
        }).getLandmarkTop100(PlayKuApplication.user.getAccessToken(), landmarkId)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.landmarkRankingBackgroundIv.visibility = View.VISIBLE

        binding.landmarkRankingImgIv.visibility = View.VISIBLE
        binding.landmarkRankingTopScoreTv.visibility = View.VISIBLE
        binding.landmarkRankingLandmarkNameTv.visibility = View.VISIBLE
        binding.landmarkRankingMiddleTv.visibility = View.VISIBLE
        binding.landmarkRankingTopNicknameTv.visibility = View.VISIBLE

        val formatter = NumberFormat.getNumberInstance()
        binding.landmarkRankingTopScoreTv.text = "+ " + formatter.format(rankInfo[0].score)

        val landmark = LandMark(landmarkId, 0.0,0.0,"",0.0,"")
        binding.landmarkRankingImgIv.setImageResource(landmark.getImageDrawable())

        val spannableString = SpannableString(landmark.name + "을")
        val colorSpan = ForegroundColorSpan(Color.parseColor("#84BB74"))
        spannableString.setSpan(colorSpan, 0, landmark.name.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
        binding.landmarkRankingLandmarkNameTv.text = spannableString

        val spannableString2 = SpannableString(rankInfo[0].nickname + "님입니다")
        spannableString2.setSpan(colorSpan, 0, rankInfo[0].nickname!!.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
        binding.landmarkRankingTopNicknameTv.text = spannableString2

        binding.landmarkRankingInfoTitleLl.visibility = View.VISIBLE
        binding.landmarkRankingMyInfoLl.visibility = View.VISIBLE
        binding.landmarkRankingRankRv.visibility = View.VISIBLE
    }

    private fun showNoData() {
        binding.landmarkRankingBackgroundIv.visibility = View.VISIBLE
        binding.landmarkRankingNoRankingTv.visibility = View.VISIBLE
    }

    private fun showMyInfo() {
        if(myRank == -1) binding.landmarkRankingMyRankTv.text = "-"
        else binding.landmarkRankingMyRankTv.text = myRank.toString()

        binding.landmarkRankingMyScoreTv.text = myScore.toString()
    }

    private fun showRanks() {
        binding.landmarkRankingRankRv.adapter = RankingRVAdapter(rankInfo)
        binding.landmarkRankingRankRv.layoutManager = LinearLayoutManager(this)
        binding.landmarkRankingRankRv.isVerticalScrollBarEnabled = rankInfo.size > 5
        binding.landmarkRankingRankRv.isScrollbarFadingEnabled = false
    }

}