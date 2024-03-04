package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityPolicyAgreeBinding
import com.umc.playkuround.util.SoundPlayer

class PolicyAgreeActivity : AppCompatActivity() {

    lateinit var binding : ActivityPolicyAgreeBinding
    private lateinit var sound : SoundPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyAgreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sound = SoundPlayer(this, R.raw.button_click_sound)

        //전체동의 누르면 넘어가짐
        binding.agreeSumCb.setOnClickListener{
            sound.play()
            onCheckChanged(binding.agreeSumCb)
            isAllChecked()
        }
        binding.agree01Cb.setOnClickListener{
            sound.play()
            onCheckChanged(binding.agree01Cb)
            isAllChecked()
        }
        binding.agree02Cb.setOnClickListener{
            sound.play()
            onCheckChanged(binding.agree02Cb)
            isAllChecked()
        }
        binding.agree03Cb.setOnClickListener{
            sound.play()
            onCheckChanged(binding.agree03Cb)
            isAllChecked()
        }

        //다음 누르면 activity전환
        binding.agreeNextBtn.setOnClickListener{
            sound.play()
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
            binding.agree01Cb.setTextColor(ActivityCompat.getColor(this, R.color.text_color))
        } else {
            binding.agree01Cb.setBackgroundResource(R.drawable.button_blue_disabled)
            binding.agree01Btn.setImageResource(R.drawable.img_next)
            binding.agree01Btn.isEnabled = true
            binding.agree01Cb.setTextColor(ActivityCompat.getColor(this, R.color.white))
        }

        if(binding.agree02Cb.isChecked) {
            binding.agree02Cb.setBackgroundResource(R.drawable.button_blue_default)
            binding.agree02Btn.setImageResource(R.drawable.img_next_disable)
            binding.agree02Btn.isEnabled = false
            binding.agree02Cb.setTextColor(ActivityCompat.getColor(this, R.color.text_color))
        } else {
            binding.agree02Cb.setBackgroundResource(R.drawable.button_blue_disabled)
            binding.agree02Btn.setImageResource(R.drawable.img_next)
            binding.agree02Btn.isEnabled = true
            binding.agree02Cb.setTextColor(ActivityCompat.getColor(this, R.color.white))
        }

        if(binding.agree03Cb.isChecked) {
            binding.agree03Cb.setBackgroundResource(R.drawable.button_blue_default)
            binding.agree03Btn.setImageResource(R.drawable.img_next_disable)
            binding.agree03Btn.isEnabled = false
            binding.agree03Cb.setTextColor(ActivityCompat.getColor(this, R.color.text_color))
        } else {
            binding.agree03Cb.setBackgroundResource(R.drawable.button_blue_disabled)
            binding.agree03Btn.setImageResource(R.drawable.img_next)
            binding.agree03Btn.isEnabled = true
            binding.agree03Cb.setTextColor(ActivityCompat.getColor(this, R.color.white))
        }

        if(binding.agreeSumCb.isChecked) {
            binding.agreeSumCb.setBackgroundResource(R.drawable.button_blue_default)
            binding.agreeSumCb.setTextColor(ActivityCompat.getColor(this, R.color.text_color))
        } else {
            binding.agreeSumCb.setBackgroundResource(R.drawable.button_blue_disabled)
            binding.agreeSumCb.setTextColor(ActivityCompat.getColor(this, R.color.white))
        }
    }

}

