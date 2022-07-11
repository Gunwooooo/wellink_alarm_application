package com.hanait.wellinkalarmapplication.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.CalendarData
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT
import java.util.*


class AlarmService: Service() {
    private val handler = Handler()
    lateinit var alarmData: AlarmData
    private var calendarData: CalendarData? = null


    companion object {
        const val SERVICE_TIME_OUT: Long = 20000 //1분
        const val CHANNEL_ID = "primary_notification_channel"
    }

    override fun onCreate() {
        super.onCreate()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("로그", "AlarmService - onStartCommand : 서비스 호출됨")
        val onOff = intent?.extras?.getString("ON_OFF")
        val pendingId = intent?.extras?.getInt("PendingId")!!
        val takenFlag = intent.extras?.getBoolean("takenFlag")
        when(onOff) {
            ADD_INTENT -> {

//              알람 id 받기
                Log.d("로그", "AlarmService - onStartCommand : pendingId : $pendingId")

                handler.postDelayed({
                    Log.d("로그", "AlarmService - onStartCommand : pendingId : $pendingId   takenFlag : $takenFlag")
                    if(!takenFlag!!) {
                        Log.d("로그", "AlarmService - onStartCommand : 알람 시간 종료!")
                        Toast.makeText(this, "약을 미복용했어요", Toast.LENGTH_SHORT).show()
                        //캘린더 DB에 미복용 정보 저장
                        Log.d("로그", "AlarmService - onStartCommand : 111111111111111111")
                        alarmData =
                            DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAsId(pendingId / 4)!!
                        //DB에서 캘린더 데이터 가져오기
                        val cal = Calendar.getInstance()
                        val date = cal.time.let { Constants.sdf.format(it) }
                        if (DatabaseManager.getInstance(applicationContext, "Alarms.db")
                                .selectCalendarAsDateAndName(date, alarmData.name) != null
                        ) {
                            calendarData = DatabaseManager.getInstance(applicationContext, "Alarms.db")
                                .selectCalendarAsDateAndName(date, alarmData.name)
                        }
                        Log.d("로그", "AlarmService - initView : calendarData : $calendarData")

                        //서비스 시간 정해놓기 (미복용)
                        var tmpCalendarData = CalendarData()
                        if(calendarData != null) tmpCalendarData = calendarData as CalendarData
                        tmpCalendarData.name = alarmData.name
                        when(pendingId % 4) {
                            0 -> tmpCalendarData.mtaken = 2
                            1 -> tmpCalendarData.ataken = 2
                            2 -> tmpCalendarData.etaken = 2
                            3 -> tmpCalendarData.ntaken = 2
                        }
                        Log.d("로그", "AlarmService - onStartCommand : 22222222222222222222222222")
                        //insert or modify 처리하기
                        //해당 날짜와 이름에 복용 정보가 0이 아니면 modify
                        if(calendarData == null) {
                            Log.d("로그", "AlarmService - onStartCommand : 33333333333333333333")
                            DatabaseManager.getInstance(this, "Alarms.db").insertCalendar(tmpCalendarData)
                        } else {
                            Log.d("로그", "AlarmService - onStartCommand : 4444444444444444444444444")
                            DatabaseManager.getInstance(this, "Alarms.db").updateCalendar(tmpCalendarData, alarmData.name)
                        }
                        Log.d("로그", "AlarmReceiver - cancelNofitication : cancelNotification호출됨   $pendingId")
                        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancel(pendingId)
                        stopSelf()
                    }
                }, SERVICE_TIME_OUT)
            }
            OFF_INTENT -> {
                Log.d("로그", "Alarmeceiver - onReceive : Service Off_intent 호출됨")
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("로그", "AlarmService - onDestroy : 서비스 디스트로이 호출")
//        mediaPlayer.stop()
//        mediaPlayer.reset()
    }

    override fun onBind(intent: Intent?): IBinder? { return null}
}