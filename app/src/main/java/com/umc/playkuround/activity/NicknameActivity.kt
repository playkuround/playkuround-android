package com.umc.playkuround.activity

import android.R
import android.app.appsearch.AppSearchSchema
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityNicknameBinding
import java.util.regex.Pattern


class NicknameActivity : AppCompatActivity() {

    lateinit var binding : ActivityNicknameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        





//        var lengthFilter = InputFilter.LengthFilter(8)
//
//        var filterAlphaNumSpace = InputFilter { source, start, end, dest, dstart, dend ->
//
//            val ps = Pattern.compile("^[ㄱ-ㅣ가-힣a-zA-Z]+$")
//            if (!ps.matcher(source).matches()) {
//                ""
//            } else source
//        }
//
//        binding.edittext.filters = arrayOf(filterAlphaNumSpace,lengthFilter)



    }








}