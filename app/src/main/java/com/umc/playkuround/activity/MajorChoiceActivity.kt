package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMajorChoiceBinding
import java.lang.reflect.Field


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

        binding.majorScSpinner.adapter = ArrayAdapter.createFromResource(this,R.array.magjor_array,R.layout.spinner_text)




        binding.majorScSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.agreeNextBtn.isEnabled = true



                when (position) {
                    0 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.major,R.layout.spinner_text)
                        binding.agreeNextBtn.isEnabled = false
                    }

                    1 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_liberal,R.layout.spinner_text)
                    }

                    2 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_sciences,R.layout.spinner_text)
                    }

                    3 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_architrcture,R.layout.spinner_text)
                    }

                    4 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_engineering,R.layout.spinner_text)
                    }

                    5 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_socialsciences,R.layout.spinner_text)
                    }

                    6 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_business,R.layout.spinner_text)
                    }

                    7 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_realestate,R.layout.spinner_text)
                    }

                    8 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_kit,R.layout.spinner_text)
                    }

                    9 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_life,R.layout.spinner_text)
                    }

                    10 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_veterinary,R.layout.spinner_text)
                    }

                    11 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_socialsciences,R.layout.spinner_text)
                    }

                    12 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_design,R.layout.spinner_text)
                    }

                    13 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_education,R.layout.spinner_text)
                    }

                    14 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.spinner_sanghuh,R.layout.spinner_text)
                    }


                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@MajorChoiceActivity, "대학과 학과를 선택해 주세요", Toast.LENGTH_SHORT).show()
            }


        }



    }





}

























