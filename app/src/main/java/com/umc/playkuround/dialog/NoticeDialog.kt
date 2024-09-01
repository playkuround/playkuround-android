package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.playkuround.R
import com.umc.playkuround.databinding.DialogNoticeBinding
import com.umc.playkuround.network.NoticeAPI
import com.umc.playkuround.network.NoticeInfo
import com.umc.playkuround.util.PlayKuApplication.Companion.pref
import com.umc.playkuround.util.PlayKuApplication.Companion.user


class NoticeDialog(context : Context) : Dialog(context) {

    inner class IndicatorAdapter : RecyclerView.Adapter<IndicatorAdapter.IndicatorViewHolder>() {

        inner class IndicatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : IndicatorViewHolder {
            val size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                context.resources.displayMetrics
            ).toInt()

            val margin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                3f,
                context.resources.displayMetrics
            ).toInt()

            val itemView = View(context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(size, size).apply {
                    marginStart = margin
                    marginEnd = margin
                }
                setBackgroundColor(Color.parseColor("#96CE86"))
            }
            return IndicatorViewHolder(itemView)
        }

        override fun onBindViewHolder(holder : IndicatorViewHolder, position : Int) {
            holder.itemView.setBackgroundColor(
                if (position == currentPosition) Color.parseColor("#6F9B63")
                else Color.parseColor("#96CE86")
            )
        }

        override fun getItemCount() : Int = noticeList.size

    }

    private lateinit var binding : DialogNoticeBinding
    private var noticeList = ArrayList<NoticeInfo>()
    private var currentPosition = 0
    private var latestId = -1
    private var newLatestId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        setOnDismissListener {
            pref.setInt("latestNoticeId", newLatestId)
        }

        binding.dialogNoticeLeftBtnBig.setOnClickListener {
            currentPosition--
            if(currentPosition < 0)
                currentPosition = 0
            setView()
        }

        binding.dialogNoticeLeftBtnSmall.setOnClickListener {
            currentPosition--
            if(currentPosition < 0)
                currentPosition = 0
            setView()
        }

        binding.dialogNoticeRightBtnBig.setOnClickListener {
            currentPosition++
            if(currentPosition >= noticeList.size)
                currentPosition = noticeList.size - 1
            setView()
        }

        binding.dialogNoticeRightBtnSmall.setOnClickListener {
            currentPosition++
            if(currentPosition >= noticeList.size)
                currentPosition = noticeList.size - 1
            setView()
        }

        getNotice()
    }

    private fun getNotice() {
        val loadingDialog = LoadingDialog(context)
        loadingDialog.show()

        val noticeAPI = NoticeAPI()
        noticeAPI.setOnResponseListener(object : NoticeAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                loadingDialog.dismiss()
                if(isSuccess) {
                    if(body is List<*>) {
                        body.forEach {
                            if(it is NoticeInfo)
                                noticeList.add(it)
                        }
                    }
                    //noticeList.add(NoticeInfo(3, "건국대 축제", "https://picsum.photos/200", "건국대 축제에 오신걸 환영해요!", "https://github.com/Renzzle/Renzzle_BE"))
                    //noticeList.add(NoticeInfo(6, "취업 설명회", null, "취업 설명회가 다음주 수요일에 열립니다.", null))
                    //noticeList.add(NoticeInfo(7, "플쿠 업데이트", "https://picsum.photos/200/300", null, "https://github.com/Renzzle/Renzzle_BE"))
                    //noticeList.add(NoticeInfo(5, "수강신청", null, "수강신청 기간은 다음주 월요일부터 수요일까지입니다. 잊지 말고 꼭 신청해주세요.", "https://www.google.com/"))
                    //noticeList.sortByDescending { it.id }
                    initView()
                } else {
                    Toast.makeText(context, errorLog, Toast.LENGTH_SHORT).show()
                }
            }
        }).getNotice(user.getAccessToken())
    }

    private fun initView() {
        latestId = pref.getInt("latestNoticeId", -1)
        if(noticeList.isEmpty()) {
            val infoDialog = Dialog(context, R.style.TransparentDialogTheme)
            infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            infoDialog.setContentView(R.layout.dialog_ranking_info)
            infoDialog.findViewById<TextView>(R.id.dialog_ranking_info_title).text = "알림"
            infoDialog.findViewById<TextView>(R.id.dialog_ranking_info_context).text = "공지가 없습니다."
            infoDialog.show()
            this.dismiss()
            return
        }
        initIndicator()
        setView()
    }

    private fun setView() {
        // change indicator
        binding.dialogNoticeIndicatorRvBig.adapter?.notifyDataSetChanged()
        binding.dialogNoticeIndicatorRvSmall.adapter?.notifyDataSetChanged()

        // change arrow status
        binding.dialogNoticeLeftBtnBig.visibility = View.VISIBLE
        binding.dialogNoticeLeftBtnSmall.visibility = View.VISIBLE
        binding.dialogNoticeRightBtnBig.visibility = View.VISIBLE
        binding.dialogNoticeRightBtnSmall.visibility = View.VISIBLE
        if(currentPosition == 0) {
            binding.dialogNoticeLeftBtnBig.visibility = View.INVISIBLE
            binding.dialogNoticeLeftBtnSmall.visibility = View.INVISIBLE
        }
        if(currentPosition == noticeList.size - 1) {
            binding.dialogNoticeRightBtnBig.visibility = View.INVISIBLE
            binding.dialogNoticeRightBtnSmall.visibility = View.INVISIBLE
        }

        // set new textview visibility
        if(noticeList[currentPosition].id > latestId) {
            binding.dialogNoticeNewTvSmall.visibility = View.VISIBLE
            binding.dialogNoticeNewTvBig.visibility = View.VISIBLE
        } else {
            binding.dialogNoticeNewTvSmall.visibility = View.INVISIBLE
            binding.dialogNoticeNewTvBig.visibility = View.INVISIBLE
        }
        if(noticeList[currentPosition].id > newLatestId) {
            newLatestId = noticeList[currentPosition].id
        }

        val nowNotice = noticeList[currentPosition]
        if(nowNotice.imageUrl == null && nowNotice.referenceUrl == null) {
            // set layout visibility
            binding.dialogNoticeSmallCl.visibility = View.VISIBLE
            binding.dialogNoticeBigCl.visibility = View.INVISIBLE

            // set link button visibility
            binding.dialogNoticeLinkBtnSmall.visibility = View.INVISIBLE

            // set height
            val layoutParams = binding.dialogNoticeSmallBg.layoutParams
            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 227f, context.resources.displayMetrics).toInt()
            binding.dialogNoticeSmallBg.layoutParams = layoutParams

            // set info
            binding.dialogNoticeTitleTvSmall.text = nowNotice.title
            binding.dialogNoticeContentTvSmall.text = nowNotice.description
        } else if(nowNotice.imageUrl == null) {
            // set layout visibility
            binding.dialogNoticeSmallCl.visibility = View.VISIBLE
            binding.dialogNoticeBigCl.visibility = View.INVISIBLE

            // set link button visibility
            binding.dialogNoticeLinkBtnSmall.visibility = View.VISIBLE

            // set height
            val layoutParams = binding.dialogNoticeSmallBg.layoutParams
            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 281f, context.resources.displayMetrics).toInt()
            binding.dialogNoticeSmallBg.layoutParams = layoutParams

            // set info
            binding.dialogNoticeTitleTvSmall.text = nowNotice.title
            binding.dialogNoticeContentTvSmall.text = nowNotice.description
            binding.dialogNoticeLinkBtnSmall.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(nowNotice.referenceUrl))
                context.startActivity(intent)
            }
        } else if(nowNotice.description == null) {
            // set layout visibility
            binding.dialogNoticeSmallCl.visibility = View.INVISIBLE
            binding.dialogNoticeBigCl.visibility = View.VISIBLE

            // set content visibility
            binding.dialogNoticeContentTvBig.visibility = View.INVISIBLE

            // set image size
            val layoutParams = binding.dialogNoticeImageIvBig.layoutParams
            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 249f, context.resources.displayMetrics).toInt()
            binding.dialogNoticeImageIvBig.layoutParams = layoutParams

            // set info
            binding.dialogNoticeTitleTvBig.text = nowNotice.title
            binding.dialogNoticeLinkBtnBig.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(nowNotice.referenceUrl))
                context.startActivity(intent)
            }
            Glide.with(context).load(nowNotice.imageUrl).into(binding.dialogNoticeImageIvBig)
        } else {
            // set layout visibility
            binding.dialogNoticeSmallCl.visibility = View.INVISIBLE
            binding.dialogNoticeBigCl.visibility = View.VISIBLE

            // set content visibility
            binding.dialogNoticeContentTvBig.visibility = View.VISIBLE

            // set image size
            val layoutParams = binding.dialogNoticeImageIvBig.layoutParams
            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 183f, context.resources.displayMetrics).toInt()
            binding.dialogNoticeImageIvBig.layoutParams = layoutParams

            // set info
            binding.dialogNoticeTitleTvBig.text = nowNotice.title
            binding.dialogNoticeContentTvBig.text = nowNotice.description
            binding.dialogNoticeLinkBtnBig.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(nowNotice.referenceUrl))
                context.startActivity(intent)
            }
            Glide.with(context).load(nowNotice.imageUrl).into(binding.dialogNoticeImageIvBig)
        }
    }

    private fun initIndicator() {
        binding.dialogNoticeIndicatorRvSmall.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.dialogNoticeIndicatorRvSmall.adapter = IndicatorAdapter()

        binding.dialogNoticeIndicatorRvBig.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.dialogNoticeIndicatorRvBig.adapter = IndicatorAdapter()
    }

}