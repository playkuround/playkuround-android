package com.umc.playkuround.util

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool


class SoundPlayer(private val context : Context, private val soundFile : Int) {

    private var mediaPlayer : MediaPlayer? = null
    private val soundPool = SoundPool.Builder().build()

    fun play() {
        soundPool.load(context, soundFile, 1)
        soundPool.setOnLoadCompleteListener { soundPool, i, _ ->
            soundPool.play(i, 1f, 1f, 1, 0, 1f);
        }
    }

    fun repeat() {
        mediaPlayer = MediaPlayer.create(context, soundFile)
        mediaPlayer?.setVolume(0.1f, 0.1f)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    fun isPlaying() : Boolean? {
        return mediaPlayer?.isPlaying
    }

    fun stop() {
        mediaPlayer?.stop()
    }

}