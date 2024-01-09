package com.umc.playkuround.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameCardFlippingBinding
import com.umc.playkuround.dialog.CountdownDialog

private const val FLIPPING_DELAY = 150L
private const val SHOWING_TIME = 700L

class MiniGameCardFlippingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameCardFlippingBinding
    private val cards = Array(4) { Array(4) { 0 } }
    private val frontCards = ArrayList<Int>()
    private val isMatched = Array(16) { false }
    private val isFlipping = Array(16) { false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameCardFlippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shuffleCards()
        addListenerToCards()

        val countdownDialog = CountdownDialog(this)
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
            if(!isMatched[n] && !isFlipping[n]) {
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
                        isFlipping[n] = true
                        isFlipping[frontCards[0]] = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            iv.setImageResource(getCardImage(cards[n / 4][n % 4]))

                            if (cards[n / 4][n % 4] == cards[frontCards[0] / 4][frontCards[0] % 4]) {
                                isMatched[n] = true
                                isMatched[frontCards[0]] = true
                                isFlipping[n] = false
                                isFlipping[frontCards[0]] = false
                                frontCards.clear()
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
        when(pos) {
            0 -> {
                binding.cardFlippingCard11.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard11.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            1 -> {
                binding.cardFlippingCard12.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard12.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            2 -> {
                binding.cardFlippingCard13.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard13.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            3 -> {
                binding.cardFlippingCard14.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard14.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            4 -> {
                binding.cardFlippingCard21.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard21.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            5 -> {
                binding.cardFlippingCard22.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard22.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            6 -> {
                binding.cardFlippingCard23.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard23.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            7 -> {
                binding.cardFlippingCard24.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard24.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            8 -> {
                binding.cardFlippingCard31.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard31.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            9 -> {
                binding.cardFlippingCard32.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard32.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            10 -> {
                binding.cardFlippingCard33.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard33.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            11 -> {
                binding.cardFlippingCard34.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard34.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            12 -> {
                binding.cardFlippingCard41.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard41.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            13 -> {
                binding.cardFlippingCard42.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard42.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            14 -> {
                binding.cardFlippingCard43.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard43.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
            }
            15 -> {
                binding.cardFlippingCard44.setImageResource(R.drawable.card_flipping_book_side)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cardFlippingCard44.setImageResource(R.drawable.card_flipping_book_back)
                    isFlipping[pos] = false
                }, FLIPPING_DELAY + 50)
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

}