package com.umc.playkuround.fragment

import android.app.Application
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.databinding.FragmentBadgeBinding
import com.umc.playkuround.dialog.SlideUpDialog
import com.umc.playkuround.dialog.smallSlideUpDialog
import com.umc.playkuround.service.AdListAdapterGrid
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
        val atLayoutManager = GridLayoutManager(context, 3)
        binding.badgeAttendanceRv.layoutManager = atLayoutManager

        binding.badgeAttendanceTv.setOnClickListener {
            placeinfoDialog()
        }



        binding.badgeAttendanceRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = 50
                outRect.left = 20
                outRect.top = 20
                outRect.right = 20
            }
        })

        binding.badgeAdventureRv.adapter = AdListAdapterGrid()
        val adLayoutManager = GridLayoutManager(context, 3)
        binding.badgeAdventureRv.layoutManager = adLayoutManager

        binding.badgeAdventureRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = 50
                outRect.left = 20
                outRect.top = 20
                outRect.right = 20
            }
        })

        return binding.root


    }

    private fun placeinfoDialog() {
        var contentView: View =
            (activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.dialog_map_place, null
            )
        val slideupPopup = smallSlideUpDialog.Builder(requireContext())
            .setContentView(contentView)
            .create()
        slideupPopup.setCancelable(false)

        slideupPopup.show()
    }



}

