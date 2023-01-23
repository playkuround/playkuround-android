package com.umc.playkuround.fragment

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.data.Ranking
import com.umc.playkuround.databinding.FragmentMypageBinding
import com.umc.playkuround.service.UserService

class MyPageFragment : Fragment() {

    lateinit var binding : FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        initView()

        binding.mypageNoticeTv.setOnClickListener {
            Log.d("update score", "onCreateView: ")
            val userService = UserService()


            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                    if(isSuccess) {
                        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show()
                    }
                }
            }).updateUserScore(user.getAccessToken(), Ranking.scoreType.ADVENTURE)
        }

        return binding.root
    }

    private fun initView() {
        binding.mypageNameTv.text = user.nickname
        binding.mypageMajorTv.text = user.major
    }


}