package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityRankingInfoBinding

class RankingInfoActivity : AppCompatActivity() {

    lateinit var binding : ActivityRankingInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rankingXBt.setOnClickListener {
            val intent = Intent(this, PolicyAgreeActivity::class.java)
            startActivity(intent)
        }
    }

}