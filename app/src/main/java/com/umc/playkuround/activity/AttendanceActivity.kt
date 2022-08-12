package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityAttendanceBinding

class AttendanceActivity : AppCompatActivity() {

    lateinit var binding : ActivityAttendanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}