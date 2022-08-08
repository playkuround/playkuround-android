package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityPolicyAgreeBinding

class PolicyAgreeActivity : AppCompatActivity() {

    lateinit var binding : ActivityPolicyAgreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyAgreeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}