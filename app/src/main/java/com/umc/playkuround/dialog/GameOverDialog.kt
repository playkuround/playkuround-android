package com.umc.playkuround.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import com.umc.playkuround.R
import com.umc.playkuround.databinding.DialogGameOverBinding

class GameOverDialog(context : Context) : Dialog(context) {

    private lateinit var binding : DialogGameOverBinding
    private var gameName = ""
    private var score = 0
    private var bestScore = 0
    private var adventureScore = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        binding.dialogGameOverGameNameTv.text = gameName
        binding.dialogGameOverScoreTv.text = "$score 점"
        binding.dialogGameOverBestScoreTv.text = "최고 점수 $bestScore 점"
        binding.dialogGameOverAdventureScoreTv.text = "모험 점수 $adventureScore 점"

        val quitBtn = findViewById<Button>(R.id.dialog_game_over_quit)
        quitBtn.setOnClickListener {
            this.dismiss()
        }
    }

    fun setInfo(gameName : String, score : Int, bestScore : Int, adventureScore : Int) {
        this.gameName = gameName
        this.score = score
        this.bestScore = bestScore
        this.adventureScore = adventureScore
    }

}