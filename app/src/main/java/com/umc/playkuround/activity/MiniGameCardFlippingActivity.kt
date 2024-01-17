package com.umc.playkuround.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameCardFlippingBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.fragment.MiniGameTimerFragment
import kotlin.concurrent.timer

private const val FLIPPING_DELAY = 150L
private const val SHOWING_TIME = 700L

class MiniGameCardFlippingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameCardFlippingBinding
    private lateinit var timerFragment : MiniGameTimerFragment
    private val cards = Array(4) { Array(4) { 0 } }
    private val frontCards = ArrayList<Int>()
    private val isMatched = Array(16) { false }
    private var isFlipping = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameCardFlippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timerFragment = supportFragmentManager.findFragmentById(R.id.card_flipping_timer_fragment) as MiniGameTimerFragment
        timerFragment.setTime(50)
        timerFragment.setOnTimeUpListener(object : MiniGameTimerFragment.OnTimeUpListener {
            override fun timeUp() {
                val gameOverDialog = GameOverDialog(this@MiniGameCardFlippingActivity)
                gameOverDialog.show()
            }
        })

        shuffleCards()
        addListenerToCards()

        binding.cardFlippingPauseBtn.setOnClickListener {
            timerFragment.pause()
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    timerFragment.start()
                }
                override fun home() {
                    finish()
                }
            })
            pauseDialog.show()
        }

        val countdownDialog = CountdownDialog(this)
        countdownDialog.setOnFinishListener(object : CountdownDialog.OnFinishListener {
            override fun onFinish() {
                timerFragment.start()
            }
        })
        countdownDialog.show()
    }

    private fun shuffleCards() {
        val array = ArrayList<Int>()
        for(i in 0..15) {
            array.add(i)
        }
        for(i in 0..15) {
            val num = (0..15-i).random()
            cards[i/4][i%4] = array[num] % 8
            array.removeAt(num)
        }
    }

    private fun addListenerToCards() {
        fun eachCardFunc(iv : ImageView, n : Int) {
            if(!isMatched[n] && !isFlipping) {
                if (frontCards.isEmpty()) {
                    frontCards.add(n)
                    iv.setImageResource(R.drawable.card_flipping_book_side)
                    Handler(Looper.getMainLooper()).postDelayed({
                        iv.setImageResource(getCardImage(cards[n / 4][n % 4]))
                    }, FLIPPING_DELAY)
                } else {
                    if (frontCards[0] != n) {
                        frontCards.add(n)
                        iv.setImageResource(R.drawable.card_flipping_book_side)
                        isFlipping = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            iv.setImageResource(getCardImage(cards[n / 4][n % 4]))

                            if (cards[n / 4][n % 4] == cards[frontCards[0] / 4][frontCards[0] % 4]) {
                                isMatched[n] = true
                                isMatched[frontCards[0]] = true
                                isFlipping = false
                                playDisappearCardMotion(n)
                                playDisappearCardMotion(frontCards[0])
                                frontCards.clear()
                                if(isAllMatched()) {
                                    timerFragment = supportFragmentManager.findFragmentById(R.id.card_flipping_timer_fragment) as MiniGameTimerFragment
                                    timerFragment.pause()
                                    val gameOverDialog = GameOverDialog(this@MiniGameCardFlippingActivity)
                                    gameOverDialog.show()
                                }
                            } else {
                                val tmp = frontCards[0]
                                frontCards.clear()
                                Handler(Looper.getMainLooper()).postDelayed({
                                    playCloseCardMotion(n)
                                    playCloseCardMotion(tmp)
                                }, SHOWING_TIME)
                            }
                        }, FLIPPING_DELAY)
                    }
                }
            }
        }

        binding.cardFlippingCard11.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard11, 0)
        }
        binding.cardFlippingCard12.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard12, 1)
        }
        binding.cardFlippingCard13.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard13, 2)
        }
        binding.cardFlippingCard14.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard14, 3)
        }
        binding.cardFlippingCard21.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard21, 4)
        }
        binding.cardFlippingCard22.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard22, 5)
        }
        binding.cardFlippingCard23.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard23, 6)
        }
        binding.cardFlippingCard24.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard24, 7)
        }
        binding.cardFlippingCard31.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard31, 8)
        }
        binding.cardFlippingCard32.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard32, 9)
        }
        binding.cardFlippingCard33.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard33, 10)
        }
        binding.cardFlippingCard34.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard34, 11)
        }
        binding.cardFlippingCard41.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard41, 12)
        }
        binding.cardFlippingCard42.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard42, 13)
        }
        binding.cardFlippingCard43.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard43, 14)
        }
        binding.cardFlippingCard44.setOnClickListener {
            eachCardFunc(binding.cardFlippingCard44, 15)
        }
    }

    private fun playCloseCardMotion(pos : Int) {
        fun close(iv : ImageView) {
            iv.setImageResource(R.drawable.card_flipping_book_side)
            Handler(Looper.getMainLooper()).postDelayed({
                iv.setImageResource(R.drawable.card_flipping_book_back)
                isFlipping = false
            }, FLIPPING_DELAY + 50)
        }

        when(pos) {
            0 -> {
                close(binding.cardFlippingCard11)
            }
            1 -> {
                close(binding.cardFlippingCard12)
            }
            2 -> {
                close(binding.cardFlippingCard13)
            }
            3 -> {
                close(binding.cardFlippingCard14)
            }
            4 -> {
                close(binding.cardFlippingCard21)
            }
            5 -> {
                close(binding.cardFlippingCard22)
            }
            6 -> {
                close(binding.cardFlippingCard23)
            }
            7 -> {
                close(binding.cardFlippingCard24)
            }
            8 -> {
                close(binding.cardFlippingCard31)
            }
            9 -> {
                close(binding.cardFlippingCard32)
            }
            10 -> {
                close(binding.cardFlippingCard33)
            }
            11 -> {
                close(binding.cardFlippingCard34)
            }
            12 -> {
                close(binding.cardFlippingCard41)
            }
            13 -> {
                close(binding.cardFlippingCard42)
            }
            14 -> {
                close(binding.cardFlippingCard43)
            }
            15 -> {
                close(binding.cardFlippingCard44)
            }
        }
    }

    private fun playDisappearCardMotion(pos : Int) {
        fun disappear(iv : ImageView) {
            val animator = ObjectAnimator.ofFloat(iv, "alpha", 1f, 0f)
            animator.duration = 700
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.start()
        }

        when(pos) {
            0 -> {
                disappear(binding.cardFlippingCard11)
            }
            1 -> {
                disappear(binding.cardFlippingCard12)
            }
            2 -> {
                disappear(binding.cardFlippingCard13)
            }
            3 -> {
                disappear(binding.cardFlippingCard14)
            }
            4 -> {
                disappear(binding.cardFlippingCard21)
            }
            5 -> {
                disappear(binding.cardFlippingCard22)
            }
            6 -> {
                disappear(binding.cardFlippingCard23)
            }
            7 -> {
                disappear(binding.cardFlippingCard24)
            }
            8 -> {
                disappear(binding.cardFlippingCard31)
            }
            9 -> {
                disappear(binding.cardFlippingCard32)
            }
            10 -> {
                disappear(binding.cardFlippingCard33)
            }
            11 -> {
                disappear(binding.cardFlippingCard34)
            }
            12 -> {
                disappear(binding.cardFlippingCard41)
            }
            13 -> {
                disappear(binding.cardFlippingCard42)
            }
            14 -> {
                disappear(binding.cardFlippingCard43)
            }
            15 -> {
                disappear(binding.cardFlippingCard44)
            }
        }
    }

    // type : 0 ~ 7
    private fun getCardImage(type : Int) : Int {
        return when(type) {
            0 -> R.drawable.card_flipping_book_milk
            1 -> R.drawable.card_flipping_book_building
            2 -> R.drawable.card_flipping_book_cat
            3 -> R.drawable.card_flipping_book_cow
            4 -> R.drawable.card_flipping_book_tree
            5 -> R.drawable.card_flipping_book_duck
            6 -> R.drawable.card_flipping_book_flower
            7 -> R.drawable.card_flipping_book_turtle
            else -> R.drawable.card_flipping_book_back
        }
    }

    private fun isAllMatched() : Boolean {
        for(i in 0..15) {
            if(!isMatched[i]) return false
        }
        return true
    }

}