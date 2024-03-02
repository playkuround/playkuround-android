package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.umc.playkuround.R
import com.umc.playkuround.data.Badge
import com.umc.playkuround.databinding.DialogBadgeInfoBinding

class BadgeInfoDialog(context : Context, private val badgeId : Int) : Dialog(context) {

    private lateinit var binding : DialogBadgeInfoBinding
    private var isLocked = false
    private var isNew = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogBadgeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        val badge = Badge(badgeId, "", "")
        binding.dialogBadgeInfoTitleTv.text = badge.getTitle()
        if(isLocked) {
            binding.dialogBadgeInfoImg.setImageResource(R.drawable.badge_locked)
            binding.dialogBadgeInfoContextTv.text = badge.getCondition()
        } else {
            binding.dialogBadgeInfoImg.setImageResource(badge.getImageDrawable())
            binding.dialogBadgeInfoContextTv.text = badge.description
        }
        if(isNew)
            binding.dialogBadgeInfoNewTv.visibility = View.VISIBLE
        else
            binding.dialogBadgeInfoNewTv.visibility = View.INVISIBLE

    }

    fun setStatus(isLocked : Boolean, isNew : Boolean) {
        this.isLocked = isLocked
        this.isNew = isNew
    }

}