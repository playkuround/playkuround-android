package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.umc.playkuround.R
import com.umc.playkuround.databinding.DialogMapPlaceBinding
import com.umc.playkuround.util.SoundPlayer
import java.text.NumberFormat

class MapPlaceDialog(context : Context) : Dialog(context) {

    private lateinit var binding : DialogMapPlaceBinding

    interface OnSelectListener {
        fun ranking()
        fun info()
    }

    private var onSelectListener : OnSelectListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogMapPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        binding.dialogMapPlaceLandmarkRankingBtn.setOnClickListener {
            SoundPlayer(context, R.raw.button_click_sound).play()
            onSelectListener?.ranking()
            this@MapPlaceDialog.dismiss()
        }
        binding.dialogMapPlaceLandmarkInfoBtn.setOnClickListener {
            SoundPlayer(context, R.raw.button_click_sound).play()
            onSelectListener?.info()
            this@MapPlaceDialog.dismiss()
        }
    }

    fun setOnSelectListener(listener : OnSelectListener) {
        onSelectListener = listener
    }

    fun setView(name : String, imgId : Int, nickname : String?, score : Int) {
        binding.dialogMapPlaceTitleTv.text = name
        binding.dialogMapPlaceImg.setImageResource(imgId)

        if(nickname == null) {
            binding.dialogMapPlaceMedalIv.visibility = View.INVISIBLE
            binding.dialogMapPlaceNicknameTv.visibility = View.INVISIBLE
            binding.dialogMapPlaceScoreTv.visibility = View.INVISIBLE
            return
        }

        binding.dialogMapPlaceNicknameTv.text = nickname + "님"

        val formatter = NumberFormat.getNumberInstance()
        binding.dialogMapPlaceScoreTv.text = formatter.format(score) + " 점"
    }

}