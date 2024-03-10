package com.umc.playkuround.util

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.umc.playkuround.R
import com.umc.playkuround.data.User

class PlayKuApplication : Application() {

    companion object {
        lateinit var pref : PreferenceUtil
        var user = User.getDefaultUser()
        lateinit var exploredLandmarks : MutableSet<String>
        var userTotalScore = 0
    }

    private var bgm = SoundPlayer(this, R.raw.background_bgm)

    override fun onCreate() {
        super.onCreate()
        pref = PreferenceUtil(applicationContext)
        user.load(pref)
        Log.d("userInfo", "onCreate: $user")

        exploredLandmarks = pref.getStringSet("exploredLandmarks", HashSet())!!
        userTotalScore = pref.getInt("score", 0)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                if(bgm.isPlaying() == null || !bgm.isPlaying()!!)
                    bgm.repeat()
            }
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                if(!isAppInForeground())
                    bgm.stop()
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    fun isAppInForeground(): Boolean {
        val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return appProcess.processName == this.packageName
            }
        }
        return false
    }

}