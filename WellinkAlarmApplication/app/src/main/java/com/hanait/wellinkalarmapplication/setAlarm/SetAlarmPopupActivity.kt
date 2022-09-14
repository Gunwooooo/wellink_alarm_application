package com.hanait.wellinkalarmapplication.setAlarm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.databinding.ActivitySetAlarmPopupActivitiyBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.CalendarData
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.service.AlarmService.Companion.SERVICE_TIME_OUT
import com.hanait.wellinkalarmapplication.service.AlarmService.Companion.takenFlag
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.mPendingIdList
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class SetAlarmPopupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivitySetAlarmPopupActivitiyBinding
    private var pendingId = 0
    private var pendingIdList: ArrayList<Int> = ArrayList()
    private var isMediaOnTmp = ""
    private var isVibrationOnTmp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()
        binding = ActivitySetAlarmPopupActivitiyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //pendingId 가져오기
        pendingId = intent.getIntExtra("PendingId", 0)
        isMediaOnTmp = intent.extras?.getString("IsMediaOn")!!
        isVibrationOnTmp = intent.extras?.getString("IsVibrationOn")!!

        //모든 서비스 pendingId 가져오기
        pendingIdList = intent.getSerializableExtra("PendingIdList") as ArrayList<Int>
        
        initView()

        //서비스 시간 정해놓기 (미복용)
        Handler().postDelayed({
            if(!takenFlag) {

                //서비스 갯수만큼 반복
                for(i in 0 until mPendingIdList.size) {
                    val alarmData = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAsId(
                        mPendingIdList[i] / 4)!!
                    //DB에서 캘린더 데이터 가져오기
                    val calendarData = getCalendarData(alarmData.name)
                    //DB에 복용 정보 저장 or 수정 하기

                    setCalendarData(mPendingIdList[i], alarmData.name, 2, calendarData)
                }
                finishAndRemoveTask() // 액티비티 종료 + 태스크 리스트에서 지우기
            }
        }, SERVICE_TIME_OUT)
    }

    private fun setCalendarData(pendingId: Int, alarmName: String, taken: Int, calendarData: CalendarData?) {
        //서비스 시간 정해놓기 (미복용)
        var tmpCalendarData = CalendarData()
        if(calendarData != null) tmpCalendarData = calendarData
        tmpCalendarData.name = alarmName
        when(pendingId % 4) {
            0 -> tmpCalendarData.mtaken = taken
            1 -> tmpCalendarData.ataken = taken
            2 -> tmpCalendarData.etaken = taken
            3 -> tmpCalendarData.ntaken = taken
        }
        //insert or modify 처리하기
        //해당 날짜와 이름에 복용 정보가 0이 아니면 modify
        if(calendarData == null) {
            DatabaseManager.getInstance(this, "Alarms.db").insertCalendar(tmpCalendarData)
        } else {
            DatabaseManager.getInstance(this, "Alarms.db").updateCalendar(tmpCalendarData, alarmName)
        }
    }

    //DB에서 복용 데이터 가져오기
    private fun getCalendarData(alarmName: String) : CalendarData? {
        val cal = Calendar.getInstance()
        val date = cal.time.let { Constants.sdf.format(it) }
        if (DatabaseManager.getInstance(applicationContext, "Alarms.db")
                .selectCalendarAsDateAndName(date, alarmName) != null
        ) {
            return DatabaseManager.getInstance(applicationContext, "Alarms.db")
                .selectCalendarAsDateAndName(date, alarmName)
        }
        return null
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun initView() {
        binding.setAlarmPopupTextViewExplain.text = "${makeAlarmNameAsList()} 드실 시간이에요!"

        //현재 시간 및 날짜 가져오기
        val myCalendar = Calendar.getInstance()
        binding.setAlarmPopupTextViewTime.text = SimpleDateFormat("HH:mm").format(myCalendar.time)
        binding.setAlarmPopupTextViewDate.text = "${myCalendar.get(Calendar.MONTH) + 1}월 ${myCalendar.get(Calendar.DAY_OF_MONTH)}일 ${getDayOfWeek(myCalendar.get(Calendar.DAY_OF_WEEK))}"
        binding.setAlarmPopupRippleBackgroundImageView.setOnClickListener(this)

        //복용 버튼 애니메이션 실행
        binding.setAlarmPopupRippleBackground.startRippleAnimation()
    }

    //full screen으로 만들기
    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        var newUiOptions = window.decorView.systemUiVisibility
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = newUiOptions
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmPopupRippleBackgroundImageView -> {
                Toast.makeText(this, "약을 복용했습니다.", Toast.LENGTH_SHORT).show()
                takenFlag = true
                for(i in 0 until mPendingIdList.size) {
                    val alarmData = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAsId(
                        mPendingIdList[i] / 4)!!
                    //DB에서 캘린더 데이터 가져오기
                    val calendarData = getCalendarData(alarmData.name)
                    //DB에 복용 정보 저장 or 수정 하기
                    setCalendarData(mPendingIdList[i], alarmData.name, 1, calendarData)
                }

                //알림 끄는 broadcast
                val intent = Intent(this, AlarmReceiver::class.java)
                intent.putExtra("intentType", Constants.OFF_INTENT)
                intent.putExtra("PendingId", pendingId)
                intent.putExtra("IsMediaOn", isMediaOnTmp)
                intent.putExtra("IsVibrationOn", isVibrationOnTmp)
                sendBroadcast(intent)

                finishAndRemoveTask() // 액티비티 종료 + 태스크 리스트에서 지우기
            }
        }
    }

    //요일 가져오기
    private fun getDayOfWeek(dayOfWeek: Int): String {
        return when(dayOfWeek) {
            1 -> "일요일"
            2 -> "월요일"
            3 -> "화요일"
            4 -> "수요일"
            5 -> "목요일"
            6 -> "금요일"
            7 -> "토요일"
            else -> ""
        }
    }

    private fun makeAlarmNameAsList() : String {
        var alarmFullName = ""
        for(i in 0 until mPendingIdList.size) {
            val alarmData = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAsId(
                mPendingIdList[i] / 4)!!
            alarmFullName += " ${alarmData.name}"
        }
        return alarmFullName
    }

    override fun onBackPressed() {
//        return
    }
}