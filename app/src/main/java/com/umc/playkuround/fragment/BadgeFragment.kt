package com.umc.playkuround.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.databinding.FragmentBadgeBinding
import com.umc.playkuround.service.ListAdapterGrid

class BadgeFragment : Fragment() {

    lateinit var binding : FragmentBadgeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBadgeBinding.inflate(inflater, container, false)

        binding.badgeAttendanceRv.adapter = ListAdapterGrid()
        val myLayoutManager = GridLayoutManager(context, 3)
        binding.badgeAttendanceRv.layoutManager = myLayoutManager

        binding.badgeAttendanceRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = 30
                outRect.left = 30
                outRect.top = 30
                outRect.right = 30
            }
        })

        return binding.root
    }

}