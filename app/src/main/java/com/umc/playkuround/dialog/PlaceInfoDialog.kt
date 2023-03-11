package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import com.umc.playkuround.R

class PlaceInfoDialog(context : Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_place_info)
        setCancelable(false)





    }
}