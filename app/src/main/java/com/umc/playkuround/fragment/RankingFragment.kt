package com.umc.playkuround.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.activity.RankingInfoActivity
import com.umc.playkuround.data.Ranking
import com.umc.playkuround.databinding.FragmentRankingBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.service.RankingRVAdapter
import com.umc.playkuround.service.UserService

class RankingFragment : Fragment() {

    lateinit var binding : FragmentRankingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingBinding.inflate(inflater, container, false)

        setDataFromServer()

        binding.rankingInfoIb.setOnClickListener {
            val intent = Intent(context, RankingInfoActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun setDataFromServer() {
        val loading = LoadingDialog(requireActivity())
        loading.show()

        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    val myRank = body as Ranking
                    binding.rankingMyRankTv.text = myRank.ranking.toString()
                    binding.rankingMyScoreTv.text = myRank.points.toString()

                    val userService2 = UserService()
                    userService2.setOnResponseListener(object : UserService.OnResponseListener() {
                        override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                            if(isSuccess) {
                                binding.rankingRecyclerView.adapter = RankingRVAdapter(body as ArrayList<Ranking>)
                                loading.dismiss()
                            } else {
                                loading.dismiss()
                                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }).getTop100Ranking(user.getAccessToken())
                } else {
                    loading.dismiss()
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).getUserRanking(user.getAccessToken())
    }

}