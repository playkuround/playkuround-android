package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMajorChoiceBinding
import com.umc.playkuround.util.SoundPlayer


class MajorChoiceActivity : AppCompatActivity() {

    lateinit var binding: ActivityMajorChoiceBinding


    @SuppressLint("DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMajorChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.agreeNextBtn.setOnClickListener{
            SoundPlayer(this, R.raw.button_click_sound).play()
            val intent = Intent(this, NicknameActivity::class.java)
            startActivity(intent)

            user.major = binding.majorDbSpinner.selectedItem.toString()
            Log.d("major", user.major)
        }

        val items = resources.getStringArray(R.array.magjor_array)
        val myAdapter2 = object : ArrayAdapter<String>(this, R.layout.spinner_text) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                if(position == count) {
                    (v as TextView).text = ""
                    v.hint = getItem(count)
                }
                return v
            }

            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }

        val myAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_text) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                if(position == count) {
                    (v as TextView).text = ""
                    v.hint = getItem(count)
                }
                return v
            }

            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        myAdapter2.addAll(items.toMutableList())
        myAdapter2.add("소속 대학을 선택해주세요.")

        binding.majorScSpinner.adapter = myAdapter2
        binding.majorScSpinner.setSelection(myAdapter2.count)

        binding.majorScSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.agreeNextBtn.isEnabled = false
                when (position + 1) {
                    1 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_liberal).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    2 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_sciences).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    3 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_architrcture).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    4 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_engineering).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    5 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_socialsciences).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    6 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_business).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    7 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_realestate).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    8 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_kit).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    9 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_life).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    10 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_veterinary).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    11 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_design).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    12 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_education).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    13 -> {
                        myAdapter.clear()
                        myAdapter.addAll(resources.getStringArray(R.array.spinner_sanghuh).toMutableList())
                        myAdapter.add("소속 학과를 선택해주세요.")
                        binding.majorDbSpinner.adapter = myAdapter
                        binding.majorDbSpinner.setSelection(myAdapter.count)
                    }

                    14 -> {
                        binding.majorDbSpinner.adapter = ArrayAdapter.createFromResource(this@MajorChoiceActivity,R.array.empty,R.layout.spinner_text)
                        binding.agreeNextBtn.isEnabled = false
                    }


                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@MajorChoiceActivity, "대학을 선택해 주세요", Toast.LENGTH_SHORT).show()
            }

        }

        binding.majorDbSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != binding.majorDbSpinner.adapter.count) binding.agreeNextBtn.isEnabled = true
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@MajorChoiceActivity, "학과를 선택해 주세요", Toast.LENGTH_SHORT).show()
            }
        }

    }

}

























