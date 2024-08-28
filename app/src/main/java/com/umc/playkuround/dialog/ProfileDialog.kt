package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.playkuround.R
import com.umc.playkuround.data.Badge
import com.umc.playkuround.databinding.DialogProfileBinding
import com.umc.playkuround.network.BadgeAPI
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.network.UserBadgeResponse
import com.umc.playkuround.network.UserProfileBadgeRequest
import com.umc.playkuround.util.PlayKuApplication
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.util.SoundPlayer

class ProfileDialog(context : Context) : Dialog(context) {

    interface OnSelectListener {
        fun onSelect(pos: Int)
    }

    inner class BadgeAdapter() : RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>() {

        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : BadgeViewHolder {
            val context = parent.context
            val imageView = ImageView(context).apply {
                val size = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    50f,
                    context.resources.displayMetrics
                ).toInt()
                val margin = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    context.resources.displayMetrics
                ).toInt()

                layoutParams = ViewGroup.MarginLayoutParams(size, size).apply {
                    setMargins(margin, margin, margin, margin)
                }
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            return BadgeViewHolder(imageView)
        }

        override fun onBindViewHolder(holder : BadgeViewHolder, position : Int) {
            holder.bind(badges[position].getImageDrawable(), position)
        }

        override fun getItemCount() = badges.size

        inner class BadgeViewHolder(private val imageView : ImageView) : RecyclerView.ViewHolder(imageView) {
            fun bind(badgeResId : Int, pos : Int) {
                imageView.setImageResource(badgeResId)

                if(selectedItemIdx == pos) {
                    val overlay = ContextCompat.getDrawable(context, R.drawable.dialog_profile_select)
                    val currentDrawable = imageView.drawable
                    val layerDrawable = LayerDrawable(arrayOf(currentDrawable, overlay))
                    imageView.setImageDrawable(layerDrawable)
                }

                imageView.setOnClickListener {
                    selectedItemIdx = pos
                    onSelectListener.onSelect(pos)
                    notifyDataSetChanged()
                }
            }
        }

    }

    private lateinit var binding : DialogProfileBinding
    private var badges = ArrayList<Badge>()
    private var selectedItemIdx = 0
    private var onSelectListener : OnSelectListener = object : OnSelectListener {
        override fun onSelect(pos: Int) {
            binding.dialogProfileProfileIv.setImageResource(badges[pos].getImageDrawable())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        binding.dialogProfileNicknameTv.text = user.nickname
        val userProfileBadge = Badge(-1, user.profileBadgeName, "")
        binding.dialogProfileProfileIv.setImageResource(userProfileBadge.getImageDrawable())

        showBadges()

        binding.dialogProfileChangeBtn.setOnClickListener {
            SoundPlayer(context, R.raw.button_click_sound).play()

            val loadingDialog = LoadingDialog(context)
            loadingDialog.show()

            val userAPI = UserAPI()
            userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
                override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                    loadingDialog.dismiss()
                    if(isSuccess) {
                        this@ProfileDialog.dismiss()
                    } else {
                        Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                        this@ProfileDialog.dismiss()
                    }
                }
            }).setProfileBadge(user.getAccessToken(), UserProfileBadgeRequest(badges[selectedItemIdx].name))
        }
    }

    private fun showBadges() {
        val loadingDialog = LoadingDialog(context)
        loadingDialog.show()

        val badgeAPI = BadgeAPI()
        badgeAPI.setOnResponseListener(object : BadgeAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                loadingDialog.dismiss()
                if(isSuccess) {
                    if(body is UserBadgeResponse) {
                        body.badges.forEach {
                            val badge = Badge(-1, it.name, "")
                            badges.add(badge)
                            if(it.name == user.profileBadgeName)
                                selectedItemIdx = badges.size - 1
                        }

                        // init badges recycler view
                        binding.dialogProfileBadgeListRv.layoutManager = GridLayoutManager(context, 4)
                        binding.dialogProfileBadgeListRv.adapter = BadgeAdapter()
                    }
                } else {
                    Toast.makeText(context, errorLog, Toast.LENGTH_SHORT).show()
                }
            }
        }).getUserBadges(PlayKuApplication.user.getAccessToken())
    }

}