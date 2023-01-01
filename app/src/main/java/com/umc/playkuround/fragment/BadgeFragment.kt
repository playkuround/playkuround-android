package com.umc.playkuround.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.activity.DialogPlaceInfoActivity
import com.umc.playkuround.activity.DialogPlaceRankActivity
import com.umc.playkuround.databinding.FragmentBadgeBinding
import com.umc.playkuround.dialog.SlideUpDialog
import com.umc.playkuround.dialog.SmallSlideUpDialog
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

        val adapter = ListAdapterGrid()
        binding.badgeAttendanceRv.adapter = adapter

        val atLayoutManager = GridLayoutManager(context, 3)
        binding.badgeAttendanceRv.layoutManager = atLayoutManager

        adapter.setItemClickListener(object : ListAdapterGrid.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                badgeinfoDialog()
            }
        })

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

    private fun badgeinfoDialog() {
        var contentView: View =
            (activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.dialog_badge_info, null
            )

        val badgeslideupPopup = SlideUpDialog.Builder(requireContext())
            .setContentView(contentView)
            .create()

        badgeslideupPopup.show()
    }


    private fun placeinfoDialog() {
        var contentView: View =
            (activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.dialog_map_place, null
            )

        val slideupPopup = SmallSlideUpDialog.Builder(requireContext())
            .setContentView(contentView)
            .create()

        val maprankBtn = slideupPopup.findViewById<Button>(R.id.map_place_rank_bt)
        maprankBtn.setOnClickListener {
            val intent = Intent(context,DialogPlaceRankActivity::class.java)
            startActivity(intent)
        }

        val mapinfoBtn = slideupPopup.findViewById<Button>(R.id.map_place_info_bt)
        mapinfoBtn.setOnClickListener {
            val intent = Intent(context,DialogPlaceInfoActivity::class.java)
            startActivity(intent)

        }
        



        slideupPopup.show()
    }



}

