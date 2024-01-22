package com.umc.playkuround.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameTypingBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.service.TextRainView


class MiniGameTypingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameTypingBinding
    private var life = 3
    private  var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameTypingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.typingTextBox.requestFocus()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.typingTextBox, InputMethodManager.SHOW_IMPLICIT)

        binding.typingPauseBtn.setOnClickListener {
            binding.typingTextRainView.pause()
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    binding.typingTextRainView.start()
                }
                override fun home() {
                    finish()
                }
            })
            pauseDialog.show()
        }

        binding.typingTextRainView.setOnTextRainDropListener(object : TextRainView.OnTextRainDropListener {
            override fun drop() {
                life--
                when(life) {
                    2 -> binding.typingLife1Iv.setImageResource(R.drawable.typing_empty_heart)
                    1 -> binding.typingLife2Iv.setImageResource(R.drawable.typing_empty_heart)
                    0 -> {
                        binding.typingLife3Iv.setImageResource(R.drawable.typing_empty_heart)
                        binding.typingTextRainView.pause()
                        val gameOverDialog = GameOverDialog(this@MiniGameTypingActivity)
                        gameOverDialog.setOnDismissListener {
                            this@MiniGameTypingActivity.finish()
                        }
                        gameOverDialog.setInfo(resources.getString(R.string.typing_game), score, 0, 0)
                        gameOverDialog.show()
                    }
                }
            }
        })

        binding.typingTextBox.setOnEditorActionListener { _, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE) {
                if(binding.typingTextRainView.deleteText(binding.typingTextBox.text.toString())) {
                    score += if(binding.typingTextBox.text.length <= 4) 1
                    else if(binding.typingTextBox.text.length <= 8) 2
                    else 3

                    binding.typingScoreTv.text = score.toString()
                }
                binding.typingTextBox.text.clear()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.typingTextRainView.bringToFront()
        binding.typingTextBox.bringToFront()

        val countdownDialog = CountdownDialog(this)
        countdownDialog.setOnFinishListener(object : CountdownDialog.OnFinishListener {
            override fun onFinish() {
                binding.typingTextRainView.start()
            }
        })
        countdownDialog.show()
    }

}