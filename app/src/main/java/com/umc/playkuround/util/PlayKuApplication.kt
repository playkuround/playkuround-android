package com.umc.playkuround.util

import android.app.Application
import android.util.Log
import com.umc.playkuround.data.User

class PlayKuApplication : Application() {

    companion object {
        lateinit var pref : PreferenceUtil
        var user = User.getDefaultUser()
    }

    override fun onCreate() {
        super.onCreate()
        pref = PreferenceUtil(applicationContext)
        user.load(pref)
        Log.d("userInfo", "onCreate: $user")
    }

}