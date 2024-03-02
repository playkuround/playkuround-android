package com.umc.playkuround.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.umc.playkuround.R
import com.umc.playkuround.databinding.DialogStoryBinding
import com.umc.playkuround.util.PlayKuApplication.Companion.exploredLandmarks

class StoryDialog(context : Context) : Dialog(context) {

    private lateinit var binding : DialogStoryBinding
    private var page = 1

    inner class StoryCut(private val id : Int) {
        fun getImageDrawable() : Int {
            return when(id) {
                0 -> R.drawable.story1
                1 -> R.drawable.story2
                2 -> R.drawable.story3
                3 -> R.drawable.story4
                4 -> R.drawable.story5
                5 -> R.drawable.story6
                else -> R.drawable.story_locked
            }
        }

        fun getStoryTitle() : String {
            return when(id) {
                0 -> "덕쿠의 꿈"
                1 -> "소원을 들어줘"
                2 -> "모험가 덕쿠"
                3 -> "보물이 없어"
                4 -> "성신의의 진실"
                5 -> "모험의 시작"
                else -> ""
            }
        }

        fun getStoryContext() : String {
            return when(id) {
                0 -> "일감호에 사는 INFP 소심한 오리 덕쿠. 오늘도 캠퍼스와 학생들을 바라보며 상상을 한다. “학교를 다니는 건 어떤 기분일까?” “나도 건국대학교 학생이고 싶어!”"
                1 -> "어느날 잠에 들어 꿈을 꾸는데... 유석창 박사님 동상이 이야기를 한다..!.. “건국대학교의 학생이 되고싶다면.. 성신의를 찾아라...” “헉! 성신의라고 불리는 보물을 찾아오면 내 소원을 들어주겠다고?!”"
                2 -> "퍼뜩 깨어난 덕쿠, “성신의? 까짓거 찾아주겠어.” 건대생을 꿈꾸며 보물 성신의를 찾기 위해 캠퍼스를 돌아다니며 열심히 모험을 떠난다."
                3 -> "건국대학교 캠퍼스를 전부 돌아다니며 산전수전을 겪은 덕쿠... 하지만.. 아무리 찾아봐도 성신의는 없다...“도대체 성신의란 보물은 어디있는거지?! 아니 애초에 어떻게 생긴거지?!?!?”"
                4 -> "”설마..!” 덕쿠는 성신의는 물질적인 것이 아니라 모험하는 과정에서 얻을 수 있는 내적인 가치라는 것을 깨달았다."
                5 -> "덕쿠의 건국대학교 학생이 되기위한 모험은 이제 시작이다. 오늘도 성신의를 함양하며 어엿한 건대인 되기 위해 덕쿠는 모험을 떠난다."
                else -> ""
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        page = exploredLandmarks.size
        if(page <= 0) page = 1
        if(page > 6) page = 6
        initPageView()

        binding.dialogStoryLeftIv.setOnClickListener {
            page--
            if(page < 1) page = 1
            initPageView()
        }

        binding.dialogStoryRightIv.setOnClickListener {
            page++
            if(page > 6) page = 6
            initPageView()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initPageView() {
        val storyCut = StoryCut(page - 1)
        if(page == 1)
            binding.dialogStoryLeftIv.visibility = View.INVISIBLE
        else if(page == 6)
            binding.dialogStoryRightIv.visibility = View.INVISIBLE
        else {
            binding.dialogStoryLeftIv.visibility = View.VISIBLE
            binding.dialogStoryRightIv.visibility = View.VISIBLE
        }

        for(i in 0..5) {
            if(i < exploredLandmarks.size)
                binding.dialogStoryIndexContainerGl.getChildAt(i).setBackgroundColor(Color.parseColor("#96CE86"))
            else
                binding.dialogStoryIndexContainerGl.getChildAt(i).setBackgroundColor(Color.WHITE)
        }
        binding.dialogStoryIndexContainerGl.getChildAt(page - 1).setBackgroundColor(Color.parseColor("#6F9B63"))

        if(page == exploredLandmarks.size)
            binding.dialogBadgeInfoNewTv.visibility = View.VISIBLE
        else
            binding.dialogBadgeInfoNewTv.visibility = View.INVISIBLE

        if(page > exploredLandmarks.size) {
            binding.dialogStoryTitleTv.text = "#$page. ???"
            binding.dialogStoryImg.setImageResource(R.drawable.story_locked)
            binding.dialogStoryContextBg.setImageResource(R.drawable.story_context_locked)
            binding.dialogStoryContextTv.text = ""
        } else {
            binding.dialogStoryTitleTv.text = "#$page. ${storyCut.getStoryTitle()}"
            binding.dialogStoryImg.setImageResource(storyCut.getImageDrawable())
            binding.dialogStoryContextBg.setImageResource(R.drawable.story_context_bg)
            binding.dialogStoryContextTv.text = storyCut.getStoryContext()
        }
    }

}