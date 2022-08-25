package com.umc.playkuround.activity

import android.content.Intent
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

    lateinit var binding: ActivityMajorChoiceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMajorChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.agreeNextBtn.setOnClickListener{
            val intent = Intent(this, NicknameActivity::class.java)
            startActivity(intent)
        }



        binding.majorScSpinner.adapter = ArrayAdapter.createFromResource(this,R.array.magjor_array,android.R.layout.simple_spinner_item)

        binding.majorScSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.agreeNextBtn.isEnabled = true

                when (position) {
                    0 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.major,android.R.layout.simple_spinner_item)
                        binding.agreeNextBtn.isEnabled = false
                    }

                    1 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_liberal,android.R.layout.simple_spinner_item)
                    }

                    2 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_sciences,android.R.layout.simple_spinner_item)
                    }

                    3 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_architrcture,android.R.layout.simple_spinner_item)
                    }

                    4 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_engineering,android.R.layout.simple_spinner_item)
                    }

                    5 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_socialsciences,android.R.layout.simple_spinner_item)
                    }

                    6 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_business,android.R.layout.simple_spinner_item)
                    }

                    7 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_realestate,android.R.layout.simple_spinner_item)
                    }

                    8 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_kit,android.R.layout.simple_spinner_item)
                    }

                    9 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_life,android.R.layout.simple_spinner_item)
                    }

                    10 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_veterinary,android.R.layout.simple_spinner_item)
                    }

                    11 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_socialsciences,android.R.layout.simple_spinner_item)
                    }

                    12 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_design,android.R.layout.simple_spinner_item)
                    }

                    13 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_education,android.R.layout.simple_spinner_item)
                    }

                    14 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_sanghuh,android.R.layout.simple_spinner_item)
                    }


                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@MajorChoiceActivity, "대학과 학과를 선택해 주세요", Toast.LENGTH_SHORT).show()
            }


        }



    }





}









