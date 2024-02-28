package com.umc.playkuround.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameMoonBinding
import com.umc.playkuround.dialog.PauseDialog


class MiniGameMoonActivity : AppCompatActivity() {

    private var count = 100

    lateinit var binding : ActivityMinigameMoonBinding
    private var gifCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameMoonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moonPauseBtn.setOnClickListener {
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    // resume
                }
                override fun home() {
                    finish()
                }
            })
            pauseDialog.show()
        }

        binding.moonClickIv.setOnClickListener {
            count--
            binding.moonCountTv.text = count.toString()
            if (count == 0) {
                //Toast.makeText(this, "맞춤", Toast.LENGTH_SHORT).show()
                binding.moonClickIv.isEnabled = false
                binding.moonClickIv.setImageResource(R.drawable.moon_four)
                binding.moonClickIv.getLayoutParams().height = 800
                binding.moonClickIv.getLayoutParams().width = 1000
                binding.moonClickIv.requestLayout()

                //saveAdventureLog()
            }
            else if (count <= 50) {
                if(gifCount != 0) return@setOnClickListener
                val handler = Handler(Looper.getMainLooper())
                handler.post(object : Runnable {
                    override fun run() {
                        when(gifCount) {
                            0 -> binding.moonClickIv.setImageResource(R.drawable.moon_three_1)
                            1 -> binding.moonClickIv.setImageResource(R.drawable.moon_three_2)
                            2 -> binding.moonClickIv.setImageResource(R.drawable.moon_three_3)
                            3 -> binding.moonClickIv.setImageResource(R.drawable.moon_three_1)
                        }
                        gifCount++
                        if(gifCount < 4)
                            handler.postDelayed(this, 100)
                        else
                            gifCount = 0
                    }
                })
            }
            else if (count <= 80) {
                if(gifCount != 0) return@setOnClickListener
                val handler = Handler(Looper.getMainLooper())
                handler.post(object : Runnable {
                    override fun run() {
                        when(gifCount) {
                            0 -> binding.moonClickIv.setImageResource(R.drawable.moon_two_1)
                            1 -> binding.moonClickIv.setImageResource(R.drawable.moon_two_2)
                            2 -> binding.moonClickIv.setImageResource(R.drawable.moon_two_3)
                            3 -> binding.moonClickIv.setImageResource(R.drawable.moon_two_1)
                        }
                        gifCount++
                        if(gifCount < 4)
                            handler.postDelayed(this, 100)
                        else
                            gifCount = 0
                    }
                })
            }
            else {
                if(gifCount != 0) return@setOnClickListener
                val handler = Handler(Looper.getMainLooper())
                handler.post(object : Runnable {
                    override fun run() {
                        when(gifCount) {
                            0 -> binding.moonClickIv.setImageResource(R.drawable.moon_1)
                            1 -> binding.moonClickIv.setImageResource(R.drawable.moon_2)
                            2 -> binding.moonClickIv.setImageResource(R.drawable.moon_3)
                            3 -> binding.moonClickIv.setImageResource(R.drawable.moon_1)
                        }
                        gifCount++
                        if(gifCount < 4)
                            handler.postDelayed(this, 100)
                        else
                            gifCount = 0
                    }
                })
            }
        }

        binding.moonPauseBtn.setOnClickListener {
            this.finish()
        }
    }

}
