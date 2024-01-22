package com.umc.playkuround.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import com.umc.playkuround.databinding.FragmentTimerBinding
import java.util.Timer
import kotlin.concurrent.timer
class MiniGameTimerFragment : Fragment() {

    interface OnTimeProgressListener {
        fun timeUp()
        fun timeProgress(leftTime : Int)
    }

    private lateinit var binding : FragmentTimerBinding
    private var timeLimit = 30 * 100
    private var leftTime = timeLimit
    private var timer : Timer? = null
    private var progressWidth = 0
    private var onTimeProgressListener : OnTimeProgressListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setTime(time : Int) {
        timeLimit = time * 100
        leftTime = timeLimit
    }

    fun getLeftTime() : Int = leftTime / 100

    fun start() {
        val ratio = binding.timerFragmentDuckIv.width.toFloat() / binding.timerFragmentProgressBg.width

        if(leftTime == timeLimit)
            progressWidth = binding.timerFragmentProgress.width
        timer = timer(period = 10) {
            if(leftTime % 100 == 0) {
                requireActivity().runOnUiThread {
                    onTimeProgressListener?.timeProgress(leftTime / 100)
                }
            }

            leftTime--
            if(leftTime < 0) {
                binding.timerFragmentProgress.visibility = View.INVISIBLE
                timer?.cancel()
                requireActivity().runOnUiThread {
                    onTimeProgressListener?.timeUp()
                }
            }
            requireActivity().runOnUiThread {
                val lp = binding.timerFragmentProgress.layoutParams
                lp.width = (progressWidth.toFloat() * (leftTime.toFloat() / timeLimit)).toInt()
                binding.timerFragmentProgress.layoutParams = lp

                val mlp = binding.timerFragmentDuckIv.layoutParams
                if(leftTime <= (timeLimit * ratio / 2).toInt()) {
                    if(mlp is MarginLayoutParams) {
                        mlp.setMargins(0,0,-1 * (progressWidth * ratio - lp.width).toInt(),0)
                    }
                } else if (leftTime >= (timeLimit * (1 - ratio / 2)).toInt()) {
                    if(mlp is MarginLayoutParams) {
                        mlp.setMargins(0,0,-1 * (progressWidth - lp.width),0)
                    }
                }
            }
        }
    }

    fun pause() {
        timer?.cancel()
        timer = null
    }

    fun setOnTimeProgressListener(listener : OnTimeProgressListener) {
        onTimeProgressListener = listener
    }

    fun setThemeColor(color : Int) {
        binding.timerFragmentTimeTv.setTextColor(color)
        binding.timerFragmentProgressBg.imageTintList = ColorStateList.valueOf(color)
        binding.timerFragmentMiniBox1.backgroundTintList = ColorStateList.valueOf(color)
        binding.timerFragmentMiniBox2.backgroundTintList = ColorStateList.valueOf(color)
    }

}