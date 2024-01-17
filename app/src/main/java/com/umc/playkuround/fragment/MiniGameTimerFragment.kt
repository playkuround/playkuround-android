package com.umc.playkuround.fragment

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

    interface OnTimeUpListener {
        fun timeUp()
    }

    private lateinit var binding : FragmentTimerBinding
    private var timeLimit = 30 * 100
    private var leftTime = timeLimit
    private var timer : Timer? = null
    private var progressWidth = 0
    private var onTimeUpListener : OnTimeUpListener? = null

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

    fun start() {
        if(leftTime == timeLimit)
            progressWidth = binding.timerFragmentProgress.width
        timer = timer(period = 10) {
            leftTime--
            if(leftTime < 0) {
                binding.timerFragmentProgress.visibility = View.INVISIBLE
                timer?.cancel()
                requireActivity().runOnUiThread {
                    onTimeUpListener?.timeUp()
                }
            }
            requireActivity().runOnUiThread {
                val lp = binding.timerFragmentProgress.layoutParams
                lp.width = (progressWidth.toFloat() * (leftTime.toFloat() / timeLimit)).toInt()
                binding.timerFragmentProgress.layoutParams = lp

                val mlp = binding.timerFragmentDuckIv.layoutParams
                if(leftTime <= (timeLimit * 0.1).toInt()) {
                    if(mlp is MarginLayoutParams) {
                        mlp.setMargins(0,0,-1 * (progressWidth * 0.2 - lp.width).toInt(),0)
                    }
                } else if (leftTime >= (timeLimit * 0.9).toInt()) {
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

    fun setOnTimeUpListener(listener : OnTimeUpListener) {
        onTimeUpListener = listener
    }

}