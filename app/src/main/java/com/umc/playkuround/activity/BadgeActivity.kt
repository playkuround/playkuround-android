package com.umc.playkuround.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.data.Badge
import com.umc.playkuround.databinding.ActivityBadgeBinding
import com.umc.playkuround.dialog.BadgeInfoDialog
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.network.BadgeAPI
import com.umc.playkuround.network.UserBadgeResponse
import com.umc.playkuround.util.PlayKuApplication.Companion.user

class BadgeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBadgeBinding
    private val isLocked = BooleanArray(38) { true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBadgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.badgeBackBtn.setOnClickListener {
            this.finish()
        }

        getBadges()
    }

    private fun setBadgeImg() {
        for(i in 0..10) {
            if(!isLocked[i])
                    (binding.badgeAttendanceBadgesGl.getChildAt(i) as ImageView).setImageResource(Badge(i,"","").getImageDrawable())
            binding.badgeAttendanceBadgesGl.getChildAt(i).setOnClickListener {
                val badgeInfoDialog = BadgeInfoDialog(this, i)
                badgeInfoDialog.setStatus(isLocked[i], false)
                badgeInfoDialog.show()
            }
        }
        for(i in 11..37) {
            if(!isLocked[i])
                (binding.badgeAdventureBadgesGl.getChildAt(i-11) as ImageView).setImageResource(Badge(i,"","").getImageDrawable())
            binding.badgeAdventureBadgesGl.getChildAt(i-11).setOnClickListener {
                val badgeInfoDialog = BadgeInfoDialog(this, i)
                badgeInfoDialog.setStatus(isLocked[i], false)
                badgeInfoDialog.show()
            }
        }
    }

    private fun getBadges() {
        val loadingDialog = LoadingDialog(this)
        loadingDialog.show()

        val badgeAPI = BadgeAPI()
        badgeAPI.setOnResponseListener(object : BadgeAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                loadingDialog.dismiss()
                if(isSuccess) {
                    if(body is UserBadgeResponse) {
                        Log.d("isoo", "getResponseBody: $body")
                        body.badges.forEach {
                            val badge = Badge(-1, it.name, "")
                            isLocked[badge.id] = false
                        }
                        setBadgeImg()
                    }
                } else {
                    Toast.makeText(applicationContext, errorLog, Toast.LENGTH_SHORT).show()
                }
            }
        }).getUserBadges(user.getAccessToken())
    }

}