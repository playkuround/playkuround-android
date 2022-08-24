package com.umc.playkuround.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMajorChoiceBinding


class MajorChoiceActivity : AppCompatActivity() {
        var adspin1: ArrayAdapter<CharSequence>? = null
        var adspin2: ArrayAdapter<CharSequence>? = null

    lateinit var binding: ActivityMajorChoiceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMajorChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)





        adspin1 = ArrayAdapter.createFromResource(this, R.array.magjor_array, android.R.layout.simple_spinner_dropdown_item)
        adspin1!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.majorScSpinner.setAdapter(adspin1)
        binding.majorScSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(this@MajorChoiceActivity, "안녕", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        })



    }





}









