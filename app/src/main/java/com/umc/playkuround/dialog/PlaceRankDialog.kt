package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.umc.playkuround.R

class PlaceRankDialog(context : Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_place_rank)
        setCancelable(false)
    }

}