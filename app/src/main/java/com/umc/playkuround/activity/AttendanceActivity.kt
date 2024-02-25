package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityAttendanceBinding
import com.umc.playkuround.util.GpsTracker
import java.text.SimpleDateFormat
import java.util.*

class AttendanceActivity : AppCompatActivity() {

    lateinit var binding : ActivityAttendanceBinding
    private val attendanceActivity = this

    private var width = 0 // attendance container width

    private lateinit var today : String
    private lateinit var todayTv : TextView
    private var attendanceDates : ArrayList<String>? = null

    private var nowLocation : android.location.Location? = null

    private lateinit var gpsTracker : GpsTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val format = SimpleDateFormat("yyyy-MM-dd")
        today = format.format(Calendar.getInstance().time)

        if(width == 0)
            getAttendanceDates()

        binding.attendanceBtn.setOnClickListener {
            attendanceToday()
        }

        binding.attendanceBackBtn.setOnClickListener {
            this.finish()
        }

//        val loading = LoadingDialog(this)
//        loading.show()
//        gpsTracker = GpsTracker(applicationContext, object : GpsTracker.OnLocationUpdateListener {
//            override fun onLocationUpdated(location: android.location.Location) {
//                nowLocation = location
//                if(loading.isShowing)
//                    loading.dismiss()
//            }})
//        gpsTracker.startLocationUpdates()
    }

    private fun getAttendanceDates() {
        attendanceDates = ArrayList()
        attendanceDates!!.add("2023-12-23")
        attendanceDates!!.add("2023-12-24")
        attendanceDates!!.add("2023-12-29")
        attendanceDates!!.add("2024-01-03")
//        val loading = LoadingDialog(this)
//        loading.show()
//
//        val userService = UserService()
//        userService.setOnResponseListener(object : UserService.OnResponseListener() {
//            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
//                if(isSuccess) {
//                    if(body is ArrayList<*>) {
//                        attendanceDates = body as ArrayList<String>
//                        initDates()
//                        loading.dismiss()
//                    }
//                } else {
//                    loading.dismiss()
//                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }).getAttendanceDates(user.getAccessToken())
    }

    private fun attendanceToday() {
        todayTv.background = ContextCompat.getDrawable(applicationContext, R.drawable.green_rec_filled)
        todayTv.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#86B6CE"))
        binding.attendanceBtn.isEnabled = false
        binding.attendanceBtn.text = "출석완료!"
        binding.attendanceBtn.setTextColor(ActivityCompat.getColor(this, R.color.text_color))
        attendanceDates!!.add(attendanceActivity.today)
//        val loading = LoadingDialog(this)
//        loading.show()
//
//        gpsTracker.requestLastLocation()
//
//        val userService = UserService()
//        userService.setOnResponseListener(object : UserService.OnResponseListener() {
//            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
//                if(isSuccess) {
//                    val userService2 = UserService()
//
//                    userService2.setOnResponseListener(object : UserService.OnResponseListener() {
//                        override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
//                            if(isSuccess) {
//                                todayTv.background = ContextCompat.getDrawable(applicationContext, R.drawable.green_rec_filled)
//                                binding.attendanceBtn.isEnabled = false
//                                binding.attendanceBtn.text = "오늘 출석 완료!"
//                                attendanceDates!!.add(attendanceActivity.today)
//
//                                loading.dismiss()
//                            } else {
//                                Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }).updateUserScore(user.getAccessToken(), Ranking.scoreType.ATTENDANCE)
//                } else {
//                    loading.dismiss()
//                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }).attendanceToday(user.getAccessToken(), Location(nowLocation!!.latitude, nowLocation!!.longitude))
    }

    private fun initDates() {
        Log.d("test1", "initDates: start ${this.today}")
        if(attendanceDates == null) {
            attendanceDates = ArrayList<String>()
            this.finish()
        }
        Log.d("test1", "initDates: " + attendanceDates)
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
        Log.d("test2", "onWindowFocusChanged: start")
        this.width = binding.attendanceContainer.width
        binding.attendanceCalendarContainerTl.removeAllViews()
        initDates()
    }

    private fun toPX(dp : Int) : Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun makeTextView(today : Calendar) : TextView {
        val a = toPX(280)
        val tv = TextView(applicationContext)
        if(today.get(Calendar.DATE) == 1)
            tv.text = (today.get(Calendar.MONTH) + 1).toString() + "/" + today.get(Calendar.DATE).toString()
        else
            tv.text = today.get(Calendar.DATE).toString()
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
        tv.typeface = ResourcesCompat.getFont(applicationContext, R.font.pretendard_medium)
        tv.gravity = Gravity.CENTER
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER

        val tvParam = TableRow.LayoutParams(a * 43 / 343, a * 43 / 343)
        tvParam.setMargins(a * 3 / 343, a * 8 / 343, a * 3 / 343, a * 8 / 343)
        tv.layoutParams = tvParam

        val format = SimpleDateFormat("yyyy-MM-dd")
        Log.d("today test", "makeTextView: ${format.format(today.time) == this.today}")
        if(attendanceDates!!.contains(format.format(today.time))) {
            tv.background = ContextCompat.getDrawable(applicationContext, R.drawable.green_rec_filled)
            tv.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#86B6CE"))
            tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
        } else {
            tv.background = ContextCompat.getDrawable(applicationContext, R.color.transparent)
            tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.light_gray))
        }

        if(format.format(today.time) == this.today) {
            if(attendanceDates!!.contains(this.today)) {
                tv.background = ContextCompat.getDrawable(applicationContext, R.drawable.green_rec_filled)
                tv.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#86B6CE"))
                tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                binding.attendanceBtn.isEnabled = false
                binding.attendanceBtn.text = "출석완료"
                binding.attendanceBtn.setTextColor(ActivityCompat.getColor(this, R.color.text_color))
            } else {
                tv.background = ContextCompat.getDrawable(applicationContext, R.drawable.green_rec_empty)
                tv.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#86B6CE"))
                tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                binding.attendanceBtn.isEnabled = true
            }
            this.todayTv = tv
        }

        return tv
    }

}