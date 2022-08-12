package com.umc.playkuround.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.playkuround.databinding.FragmentBadgeBinding

class BadgeFragment : Fragment() {

    lateinit var binding : FragmentBadgeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBadgeBinding.inflate(inflater, container, false)

        return binding.root
    }

}