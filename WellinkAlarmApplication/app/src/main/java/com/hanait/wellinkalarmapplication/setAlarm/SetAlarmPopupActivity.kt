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
import com.hanait.wellinkalarmapplication.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class SetAlarmPopupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivitySetAlarmPopupActivitiyBinding
    private var pendingId = 0
    private var alarmData: AlarmData? = null
    private var calendarData: CalendarData? = null
    var takenFlag = false


    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()
        binding = ActivitySetAlarmPopupActivitiyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        //서비스 시간 정해놓기 (미복용)
        Handler().postDelayed({
            if(!takenFlag) {
                Log.d("로그", "SetAlarmPopupActivity - onCreate : 알람 시간 종료 팝업 지우기!")
                var tmpCalendarData = CalendarData()
                if(calendarData != null) tmpCalendarData = calendarData as CalendarData
                tmpCalendarData.name = alarmData!!.name
                when(pendingId % 4) {
                    0 -> tmpCalendarData.mtaken = 2
                    1 -> tmpCalendarData.ataken = 2
                    2 -> tmpCalendarData.etaken = 2
                    3 -> tmpCalendarData.ntaken = 2
                }
                //insert or modify 처리하기
                //해당 날짜와 이름에 복용 정보가 0이 아니면 modify
                Log.d("로그", "SetAlarmPopupActivity - onCreate : postdelay  :  $tmpCalendarData")
                if(calendarData == null ) {
                    DatabaseManager.getInstance(this, "Alarms.db").insertCalendar(tmpCalendarData)
                } else {
                    DatabaseManager.getInstance(this, "Alarms.db").updateCalendar(tmpCalendarData, alarmData!!.name)
                }
                finishAndRemoveTask() // 액티비티 종료 + 태스크 리스트에서 지우기
            }
        }, SERVICE_TIME_OUT)

        Log.d("로그", "SetAlarmPopupActivitiy - onCreate : 팝업 액티비티 호출됨!")
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun initView() {
        //pendingId 가져오기
        pendingId = intent.getIntExtra("PendingId", 0)
        Log.d("로그", "SetAlarmPopupActivitiy - onCreate : create에서 로그 pendingId : $pendingId")

        //DB에서 알림 데이터 가져오기
        if(DatabaseManager.getInstance(applicationContext, "Alarms.db").selectAlarmAsId(pendingId / 4) != null) {
            alarmData = DatabaseManager.getInstance(applicationContext, "Alarms.db").selectAlarmAsId(pendingId / 4)
        }

        Log.d("로그", "SetAlarmPopupActivitiy - onCreate : $alarmData")

        //DB에서 캘린더 데이터 가져오기
        val cal = Calendar.getInstance()
        val date = cal.time.let { Constants.sdf.format(it) }
        if(DatabaseManager.getInstance(applicationContext, "Alarms.db").selectCalendarAsDateAndName(date, alarmData!!.name) != null) {
            calendarData = DatabaseManager.getInstance(applicationContext, "Alarms.db").selectCalendarAsDateAndName(date, alarmData!!.name)
        }
        Log.d("로그", "SetAlarmPopupActivity - initView : calendarData : $calendarData")


        binding.setAlarmPopupTextViewExplain.text = "${alarmData!!.name} 드실 시간이에요!"

        //현재 시간 및 날짜 가져오기
        val myCalendar = Calendar.getInstance()
        binding.setAlarmPopupTextViewTime.text = SimpleDateFormat("HH:mm").format(myCalendar.time)
        binding.setAlarmPopupTextViewDate.text = "${myCalendar.get(Calendar.MONTH) + 1}월 ${myCalendar.get(Calendar.DAY_OF_MONTH)}일 ${getDayOfWeek(myCalendar.get(Calendar.DAY_OF_WEEK))}"
        binding.setAlarmPopupRippleBackgroundImageView.setOnClickListener(this)

        //복용 버튼 애니메이션 실행
        binding.setAlarmPopupRippleBackground.startRippleAnimation()
    }

    override fun onBackPressed() {
//        return
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

                setAlarmTaken()

                //알림 끄는 broadcast
                val intent = Intent(this, AlarmReceiver::class.java)
                intent.putExtra("intentType", Constants.OFF_INTENT)
                intent.putExtra("PendingId", pendingId)
                intent.putExtra("takenFlag", takenFlag)
                sendBroadcast(intent)

                finishAndRemoveTask() // 액티비티 종료 + 태스크 리스트에서 지우기
            }
        }
    }

    //DB 복용정보 수정
    private fun setAlarmTaken() {

        var tmpCalendarData = CalendarData()
        if(calendarData != null) tmpCalendarData = calendarData as CalendarData
        tmpCalendarData.name = alarmData!!.name
        when(pendingId % 4) {
            0 -> tmpCalendarData.mtaken = 1
            1 -> tmpCalendarData.ataken = 1
            2 -> tmpCalendarData.etaken = 1
            3 -> tmpCalendarData.ntaken = 1
        }
        //insert or modify 처리하기
        //해당 날짜와 이름에 복용 정보가 0이 아니면 modify
        Log.d("로그", "SetAlarmPopupActivity - setAlarmTaken : insert calendarData  :  $tmpCalendarData")
        if(calendarData == null) {
            DatabaseManager.getInstance(this, "Alarms.db").insertCalendar(tmpCalendarData)
            return
        }
        DatabaseManager.getInstance(this, "Alarms.db").updateCalendar(tmpCalendarData, alarmData!!.name)

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
}