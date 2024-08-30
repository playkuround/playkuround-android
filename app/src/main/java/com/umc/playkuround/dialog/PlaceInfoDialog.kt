package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.databinding.DialogPlaceInfoBinding
import com.umc.playkuround.databinding.ItemTagBinding
import com.umc.playkuround.util.LandmarkUtil
import com.umc.playkuround.util.SoundPlayer


class PlaceInfoDialog(context : Context, private val landmarkId : Int) : Dialog(context) {

    inner class AmenityTagAdapter : RecyclerView.Adapter<AmenityTagAdapter.AmenityTagViewHolder>() {

        inner class AmenityTagViewHolder(val binding : ItemTagBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(pos : Int) {
                binding.itemTagNameTv.text = amenityTags[pos].first
                binding.itemTagIconIv.setImageResource(amenityTags[pos].second)
            }
        }

        override fun onCreateViewHolder(parent : ViewGroup, viewType: Int) : AmenityTagViewHolder {
            val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AmenityTagViewHolder(binding)
        }

        override fun getItemCount() : Int = amenityTags.size

        override fun onBindViewHolder(holder : AmenityTagViewHolder, position : Int) {
            holder.bind(position)
        }

    }

    private lateinit var binding : DialogPlaceInfoBinding
    private lateinit var landmarkUtil : LandmarkUtil
    private lateinit var amenityTags : List<Pair<String, Int>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPlaceInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)
        setCancelable(false)

        landmarkUtil = LandmarkUtil(context)
        val landmark = LandMark(landmarkId, 0.0, 0.0, "")
        binding.dialogPlaceInfoImg.setImageResource(landmark.getImageDrawable())
        binding.dialogPlaceInfoTitleTv.text = landmark.name
        binding.dialogPlaceInfoContextTv.text = landmarkUtil.getDescription(landmarkId)

        binding.dialogPlaceInfoCloseBtn.setOnClickListener {
            SoundPlayer(context, R.raw.button_click_sound).play()
            this.dismiss()
        }

        setLandmarkInfo()
        setAmenityTags()
    }

    private fun setLandmarkInfo() {
        val info = landmarkUtil.getInformation(landmarkId)

        if(info.isEmpty()) {
            binding.dialogPlaceInfoContext2Tv.text = "없음"
            return
        }

        var resultText = ""
        info.forEach {
            resultText += it.title
            resultText += "\n"
            resultText += it.content
            resultText += "\n"
        }

        val spannableStringBuilder = SpannableStringBuilder(resultText)

        info.forEach {
            val title = it.title
            val startTitle = resultText.indexOf(title)
            val endTitle = startTitle + title.length
            spannableStringBuilder.setSpan(
                StyleSpan(Typeface.BOLD),
                startTitle,
                endTitle,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.dialogPlaceInfoContext2Tv.text = spannableStringBuilder
        binding.dialogPlaceInfoContext2Tv.isLongClickable = true;
        binding.dialogPlaceInfoContext2Tv.setTextIsSelectable(true);
    }

    private fun setAmenityTags() {
        amenityTags = landmarkUtil.getAmenity(landmarkId)
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        binding.dialogPlaceInfoTagRv.layoutManager = layoutManager
        binding.dialogPlaceInfoTagRv.adapter = AmenityTagAdapter()
    }

}