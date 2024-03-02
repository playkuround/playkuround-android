package com.umc.playkuround.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context : Context) {

    private val prefs : SharedPreferences = context.getSharedPreferences("storage_pk", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getStringSet(key: String, defValue: Set<String>): MutableSet<String>? {
        return prefs.getStringSet(key, defValue)
    }

    fun setStringSet(key: String, strSet: Set<String>) {
        prefs.edit().putStringSet(key, strSet).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }

    fun setInt(key: String, num: Int) {
        prefs.edit().putInt(key, num).apply()
    }

    fun clearData() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

}