package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityPolicyAgreeBinding

class PolicyAgreeActivity : AppCompatActivity() {

    lateinit var binding : ActivityPolicyAgreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyAgreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //전체동의 누르면 넘어가짐
        binding.agreeSumCb.setOnClickListener{ onCheckChanged(binding.agreeSumCb)
            isAllChecked()}
        binding.agree01Cb.setOnClickListener{onCheckChanged(binding.agree01Cb)
            isAllChecked()}
        binding.agree02Cb.setOnClickListener{onCheckChanged(binding.agree02Cb)
            isAllChecked()}
        binding.agree03Cb.setOnClickListener{onCheckChanged(binding.agree03Cb)
            isAllChecked()}



        //다음 누르면 activity전환
        binding.agreeNextBtn.setOnClickListener{
            val intent = Intent(this, MajorChoiceActivity::class.java)
            startActivity(intent)
        }

        binding.agree01Btn.setOnClickListener {
            val intent = Intent(this, DetailAgree01Activity::class.java)
            startActivity(intent)
        }
        binding.agree02Btn.setOnClickListener {
            val intent = Intent(this, DetailAgree02Activity::class.java)
            startActivity(intent)
        }
        binding.agree03Btn.setOnClickListener {
            val intent = Intent(this, DetailAgree03Activity::class.java)
            startActivity(intent)
        }





    }

    private fun isAllChecked() {
        binding.agreeNextBtn.isEnabled = binding.agreeSumCb.isChecked
    }



//    private fun onclick(view: View){
//        when(view.id){
//            R.id.agree_next_btn -> {
//                if (binding.agreeNextBtn.isEnabled){
//                    binding.agree01Cb.isChecked = true
//                    binding.agree02Cb.isChecked = true
//                    binding.agree03Cb.isChecked = true
//                }
//                else {
//                    binding.agree01Cb.isChecked = false
//                    binding.agree02Cb.isChecked = false
//                    binding.agree03Cb.isChecked = false
//                }
//            }
//            else -> { binding.agreeNextBtn.isEnabled = (
//                    binding.agree01Cb.isChecked
//                            && binding.agree02Cb.isChecked
//                            &&binding.agree03Cb.isChecked)
//            }
//        }
//
//    }


    private fun onCheckChanged(compoundButton: CompoundButton) {
        when(compoundButton.id) {
            R.id.agree_sum_cb -> {
                if (binding.agreeSumCb.isChecked){
                    binding.agree01Cb.isChecked = true
                    binding.agree02Cb.isChecked = true
                    binding.agree03Cb.isChecked = true

                } else {
                    binding.agree01Cb.isChecked = false
                    binding.agree02Cb.isChecked = false
                    binding.agree03Cb.isChecked = false
                }
            }
            else -> { binding.agreeSumCb.isChecked = (
                    binding.agree01Cb.isChecked
                            && binding.agree02Cb.isChecked
                            &&binding.agree03Cb.isChecked)
            }
        }

        if(binding.agree01Cb.isChecked) {
            binding.agree01Cb.setBackgroundResource(R.drawable.button_blue_default)
            binding.agree01Btn.setImageResource(R.drawable.img_next_disable)
            binding.agree01Btn.isEnabled = false
        } else {
            binding.agree01Cb.setBackgroundResource(R.drawable.button_blue_disabled)
            binding.agree01Btn.setImageResource(R.drawable.img_next)
            binding.agree01Btn.isEnabled = true
        }

        if(binding.agree02Cb.isChecked) {
            binding.agree02Cb.setBackgroundResource(R.drawable.button_blue_default)
            binding.agree02Btn.setImageResource(R.drawable.img_next_disable)
            binding.agree02Btn.isEnabled = false
        } else {
            binding.agree02Cb.setBackgroundResource(R.drawable.button_blue_disabled)
            binding.agree02Btn.setImageResource(R.drawable.img_next)
            binding.agree02Btn.isEnabled = true
        }

        if(binding.agree03Cb.isChecked) {
            binding.agree03Cb.setBackgroundResource(R.drawable.button_blue_default)
            binding.agree03Btn.setImageResource(R.drawable.img_next_disable)
            binding.agree03Btn.isEnabled = false
        } else {
            binding.agree03Cb.setBackgroundResource(R.drawable.button_blue_disabled)
            binding.agree03Btn.setImageResource(R.drawable.img_next)
            binding.agree03Btn.isEnabled = true
        }

        if(binding.agreeSumCb.isChecked) {
            binding.agreeSumCb.setBackgroundResource(R.drawable.button_blue_default)
        } else {
            binding.agreeSumCb.setBackgroundResource(R.drawable.button_blue_disabled)
        }
    }




}

