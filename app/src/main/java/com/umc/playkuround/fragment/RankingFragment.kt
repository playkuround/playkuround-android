package com.umc.playkuround.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.playkuround.activity.RankingInfoActivity
import com.umc.playkuround.databinding.FragmentRankingBinding
import com.umc.playkuround.service.RankingRVAdapter

class RankingFragment : Fragment() {

    lateinit var binding : FragmentRankingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingBinding.inflate(inflater, container, false)

        binding.rankingRecyclerView.adapter = RankingRVAdapter()

        binding.rankingInfoIb.setOnClickListener {
            val intent = Intent(context, RankingInfoActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}