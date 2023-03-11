package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.databinding.DialogPlaceInfoBinding

class DialogPlaceInfoActivity : AppCompatActivity() {

    lateinit var binding : DialogPlaceInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPlaceInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val landmark = intent.getSerializableExtra("landmark") as LandMark

        binding.dialogPlaceInfoIv.setImageResource(landmark.getImageDrawable())
        binding.dialogPlaceInfoTv.text = landmark.getDescription()
        binding.dialogPlaceInfoTitleTv.text = landmark.name

        binding.dialogPlaceInfoXIv.setOnClickListener {
            finish()
        }
    }

}