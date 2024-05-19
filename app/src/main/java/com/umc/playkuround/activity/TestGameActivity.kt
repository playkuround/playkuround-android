package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityTestGameBinding

class TestGameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTestGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.testAvoidBtn.setOnClickListener {
            val intent = Intent(this, MiniGameAvoidActivity::class.java)
            startActivity(intent)
        }

        binding.testBridgeBtn.setOnClickListener {
            val intent = Intent(this, MiniGameBridgeActivity::class.java)
            startActivity(intent)
        }

        binding.testCardBtn.setOnClickListener {
            val intent = Intent(this, MiniGameCardFlippingActivity::class.java)
            startActivity(intent)
        }

        binding.testCatchBtn.setOnClickListener {
            val intent = Intent(this, MiniGameCatchActivity::class.java)
            startActivity(intent)
        }

        binding.testMoonBtn.setOnClickListener {
            val intent = Intent(this, MiniGameMoonActivity::class.java)
            startActivity(intent)
        }

        binding.testTimerBtn.setOnClickListener {
            val intent = Intent(this, MiniGameTimerActivity::class.java)
            startActivity(intent)
        }

        binding.testTypingBtn.setOnClickListener {
            val intent = Intent(this, MiniGameTypingActivity::class.java)
            startActivity(intent)
        }

        binding.testQuizBtn.setOnClickListener {
            val intent = Intent(this, MiniGameQuizActivity::class.java)
            startActivity(intent)
        }
    }

}