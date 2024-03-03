package com.umc.playkuround.dialog

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.animation.doOnEnd
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.databinding.DialogRandomGameBinding
import com.umc.playkuround.util.SoundPlayer
import kotlin.random.Random

class RandomGameDialog(context : Context, private val landmarkId : Int) : Dialog(context) {

    interface OnStartListener {
        fun onStart(selected : Int)
    }

    private var onStartListener : OnStartListener? = null

    private lateinit var binding : DialogRandomGameBinding

    private val gameNames = listOf("책 뒤집기", "덕쿠를 잡아라", "수강신청 All 클릭", "덕큐피트", "문을 점령해", "일감호에서 살아남기", "건쏠지식", "10초를 맞춰봐")
    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0
    private var delayMillis : Long = 50
    private var speedUpInterval: Long = 250
    private var isSpeedingUp = true
    private var lastIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRandomGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)
        setCancelable(false)

        binding.dialogRandomStartBtn.setOnClickListener {
            SoundPlayer(context, R.raw.button_click_sound).play()
            dismiss()
            onStartListener?.onStart(lastIndex + 1)
            //onStartListener?.onStart(3)
        }

        val landmark = LandMark(landmarkId, 0.0, 0.0, "", 0.0, "")
        binding.dialogRandomLandmarkImg.setImageResource(landmark.getImageDrawable())
        binding.dialogRandomLandmarkNameTv.text = landmark.name

        startChangingText()
    }

    @SuppressLint("Recycle")
    private fun blinkText() {
        val blinkAnimator = ObjectAnimator.ofFloat(binding.dialogRandomGameNameTv, "alpha", 0f, 1f)
        blinkAnimator.repeatCount = 1
        blinkAnimator.duration = 1000

        blinkAnimator.addUpdateListener {
            val alpha = it.animatedValue as Float
            binding.dialogRandomGameNameTv.alpha = alpha
        }

        blinkAnimator.start()
        blinkAnimator.doOnEnd {
            binding.dialogRandomStartBtn.isEnabled = true
        }
    }

    private fun startChangingText() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                binding.dialogRandomGameNameTv.text = gameNames[currentIndex]
                currentIndex++

                if (currentIndex >= gameNames.size) {
                    if (isSpeedingUp) {
                        delayMillis += 40
                        if (delayMillis >= speedUpInterval) {
                            isSpeedingUp = false
                            handler.postDelayed({
                                lastIndex = Random.nextInt(8)
                                binding.dialogRandomGameNameTv.text = gameNames[lastIndex]
                                handler.postDelayed({
                                    blinkText()
                                }, 500)
                            }, 500)
                            return
                        }
                    }
                    currentIndex = 0
                }
                handler.postDelayed(this, delayMillis)
            }
        }, delayMillis)
    }

    fun setOnStartListener(listener : OnStartListener) {
        onStartListener = listener
    }

}