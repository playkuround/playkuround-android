package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.DialogPlaceInfoBinding

class DialogPlaceInfoActivity : AppCompatActivity() {

    lateinit var binding : DialogPlaceInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPlaceInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}