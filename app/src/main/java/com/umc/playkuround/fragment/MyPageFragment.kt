package com.umc.playkuround.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {

    lateinit var binding : FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    private fun initView() {
        binding.mypageNameTv.text = user.nickname
        binding.mypageMajorTv.text = user.major
    }

}