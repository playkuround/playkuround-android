package com.umc.playkuround.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.Badge
import com.umc.playkuround.databinding.FragmentBadgeBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.dialog.SlideUpDialog
import com.umc.playkuround.util.AdListAdapterGrid
import com.umc.playkuround.util.ListAdapterGrid
import com.umc.playkuround.network.UserAPI

class BadgeFragment : Fragment() {

    lateinit var binding : FragmentBadgeBinding
    lateinit var badgeList : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBadgeBinding.inflate(inflater, container, false)

        //saveBadge()
        getBadgeList()

        return binding.root
    }

    private fun getBadgeList() {
        val loading = LoadingDialog(requireActivity())
        loading.show()

        val userAPI = UserAPI()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    badgeList = body as ArrayList<String>
                    Log.d("xdxd", "getResponseBody from badge: $badgeList")

                    val adapter = ListAdapterGrid(badgeList)
                    binding.badgeAttendanceRv.adapter = adapter

                    val atLayoutManager = GridLayoutManager(context, 3)
                    binding.badgeAttendanceRv.layoutManager = atLayoutManager

                    adapter.setItemClickListener(object : ListAdapterGrid.OnItemClickListener{
                        override fun onClick(v: View, position: Int) {
                            badgeinfoDialog("attendance", position)
                        }
                    })

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

                    val adapterAdventure = AdListAdapterGrid(badgeList)
                    binding.badgeAdventureRv.adapter = adapterAdventure
                    val adLayoutManager = GridLayoutManager(context, 3)
                    binding.badgeAdventureRv.layoutManager = adLayoutManager

                    adapterAdventure.setItemClickListener(object : AdListAdapterGrid.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {
                            badgeinfoDialog("adventure", position)
                        }
                    })

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


                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).getBadgeList(user.getAccessToken())
    }

    private fun badgeinfoDialog(key : String, pos : Int) {
        var contentView: View =
            (activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.dialog_badge_info, null
            )

        val badgeslideupPopup = SlideUpDialog.Builder(requireContext())
            .setContentView(contentView)
            .create()

        var badge = Badge("", "")
        if(key == "attendance") {
            when(pos) {
                0 -> badge = Badge(Badge.ATTENDANCE_1, "")
                1 -> badge = Badge(Badge.ATTENDANCE_3, "")
                2 -> badge = Badge(Badge.ATTENDANCE_7, "")
                3 -> badge = Badge(Badge.ATTENDANCE_30, "")
                4 -> badge = Badge(Badge.ATTENDANCE_100, "")
                5 -> badge = Badge(Badge.ATTENDANCE_FOUNDATION_DAY, "")
            }
        } else if(key == "adventure") {
            when(pos) {
                0 -> badge = Badge(Badge.ADVENTURE_1, "")
                1 -> badge = Badge(Badge.ADVENTURE_5, "")
                2 -> badge = Badge(Badge.ADVENTURE_10, "")
                3 -> badge = Badge(Badge.ADVENTURE_30, "")
                4 -> badge = Badge(Badge.ENGINEER, "")
                5 -> badge = Badge(Badge.ARTIST, "")
                6 -> badge = Badge(Badge.CEO, "")
                7 -> badge = Badge(Badge.NATIONAL_PLAYER, "")
                8 -> badge = Badge(Badge.CONQUEROR, "")
                9 -> badge = Badge(Badge.NEIL_ARMSTRONG, "")
            }
        }

        badgeslideupPopup.findViewById<ImageView>(R.id.dialog_badge_iv).setImageResource(badge.getImageDrawable())
        badgeslideupPopup.findViewById<TextView>(R.id.dialog_badge_title_tv).text = badge.getTitle()
        badgeslideupPopup.findViewById<TextView>(R.id.dialog_badge_description_tv).text = badge.description

        if(badgeList.contains(badge.name))
            badgeslideupPopup.show()
    }

}

