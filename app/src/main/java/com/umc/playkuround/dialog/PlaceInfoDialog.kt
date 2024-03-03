package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.databinding.DialogPlaceInfoBinding
import com.umc.playkuround.util.SoundPlayer

class PlaceInfoDialog(context : Context, private val landmarkId : Int) : Dialog(context) {

    private lateinit var binding : DialogPlaceInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPlaceInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)
        setCancelable(false)

        val landmark = LandMark(landmarkId, 0.0, 0.0, "", 0.0, "")
        binding.dialogPlaceInfoImg.setImageResource(landmark.getImageDrawable())
        binding.dialogPlaceInfoTitleTv.text = landmark.name
        binding.dialogPlaceInfoContextTv.text = landmark.getDescription()

        binding.dialogPlaceInfoCloseBtn.setOnClickListener {
            SoundPlayer(context, R.raw.button_click_sound).play()
            this.dismiss()
        }


        val viewTreeObserver = binding.dialogPlaceInfoContextTv.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val lineCount = binding.dialogPlaceInfoContextTv.lineCount
                if(lineCount > 3)
                    binding.dialogPlaceInfoScroll.isScrollbarFadingEnabled = false
                else
                    binding.dialogPlaceInfoScroll.isVerticalScrollBarEnabled = false
                binding.dialogPlaceInfoContextTv.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

}