package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setMargins
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityAttendanceBinding
import java.text.SimpleDateFormat
import java.util.*

class AttendanceActivity : AppCompatActivity() {

    lateinit var binding : ActivityAttendanceBinding

    private var width = 0 // attendance container width

    lateinit var today : String
    lateinit var todayTv : TextView
    var attendanceDates = arrayOf("2022-08-22", "2022-08-02")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val format = SimpleDateFormat("yyyy-MM-dd")
        today = format.format(Calendar.getInstance().time)

        binding.attendanceBtn.setOnClickListener {
            todayTv.background = ContextCompat.getDrawable(applicationContext, R.drawable.green_circle_filled)
        }

        if(width == 0)
            initDates()

        binding.attendanceBackBtn.setOnClickListener {
            this.finish()
        }
    }

    private fun initDates() {
        val today = Calendar.getInstance()
        today.add(Calendar.DATE, -29)

        var row = ArrayList<TableRow>()
        for(i in 0..29) {
            if(i % 7 == 0) {
                row.add(TableRow(applicationContext))
                val params = TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                params.weight = 1f
                row[i/7].layoutParams = params
            }
            val date = makeTextView(today)
            today.add(Calendar.DATE, 1)
            row[(i/7)].addView(date)
            if(i == 29 || i % 7 == 6) {
                binding.attendanceCalendarContainerTl.addView(row[(i/7)])
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        this.width = binding.attendanceContainer.width
        binding.attendanceCalendarContainerTl.removeAllViews()
        initDates()
    }

    private fun screenWidth() : Int {
        return resources.displayMetrics.widthPixels
    }

    private fun toPX(dp : Int) : Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun makeTextView(today : Calendar) : TextView {
        val a = screenWidth() - toPX(36)
        val tv = TextView(applicationContext)
        if(today.get(Calendar.DATE) == 1)
            tv.text = (today.get(Calendar.MONTH) + 1).toString() + "/" + today.get(Calendar.DATE).toString()
        else
            tv.text = today.get(Calendar.DATE).toString()
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        tv.typeface = ResourcesCompat.getFont(applicationContext, R.font.pretendard_medium)
        tv.gravity = Gravity.CENTER
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER

        val tvParam = TableRow.LayoutParams(a * 37 / 343, a * 37 / 343)
        tvParam.setMargins(a * 6 / 343)
        tv.layoutParams = tvParam

        val format = SimpleDateFormat("yyyy-MM-dd")
        if(attendanceDates.contains(format.format(today.time))) {
            tv.background = ContextCompat.getDrawable(applicationContext, R.drawable.green_circle_filled)
            tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
        } else if(format.format(today.time) == this.today) {
            tv.background = ContextCompat.getDrawable(applicationContext, R.drawable.green_circle_empty)
            tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            this.todayTv = tv
        } else {
            tv.background = ContextCompat.getDrawable(applicationContext, R.color.transparent)
            tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.light_gray))
        }

        return tv
    }

}