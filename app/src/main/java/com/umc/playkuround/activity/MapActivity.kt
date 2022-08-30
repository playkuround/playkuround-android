package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityMapBinding
import kotlin.random.Random

class MapActivity : AppCompatActivity() {

    lateinit var binding : ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapBackBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.mapClickBtn.setOnClickListener {
            val intent : Intent = when (getGames()) {
                0 -> {
                    Intent(this, MiniGameQuizActivity::class.java)
                }
                1 -> {
                    Intent(this, MiniGameMoonActivity::class.java)
                }
                2 -> {
                    Intent(this, MiniGameTimerActivity::class.java)
                }
                else -> {
                    Intent(this, MiniGameTimerActivity::class.java)
                }
            }
            startActivity(intent)
        }
    }

    private fun getGames() : Int {
        return Random(System.currentTimeMillis()).nextInt(3)
    }

}