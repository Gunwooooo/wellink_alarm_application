package com.hanait.wellinkalarmapplication.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.hanait.wellinkalarmapplication.MainActivity
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.CalendarData
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmPopupActivity
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmPopupActivity.Companion.takenFlag
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT
import java.text.SimpleDateFormat
import java.util.*


class AlarmService: Service() {

    lateinit var alarmData: AlarmData
    private var calendarData: CalendarData? = null

    companion object {
        private lateinit var mediaPlayer: MediaPlayer
        const val SERVICE_TIME_OUT: Long = 60000 //1분
        const val CHANNEL_ID = "primary_notification_channel"
        var NOTIFICATION_ID = 0
    }



    override fun onCreate() {
        super.onCreate()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("로그", "AlarmService - onStartCommand : 서비스 호출됨")
        val onOff = intent?.extras?.getString("ON_OFF")
        val pendingId = intent?.extras?.getInt("PendingId")!!
        NOTIFICATION_ID = pendingId

        when(onOff) {
            ADD_INTENT -> {
                //벨소리 실행
                startMedia()
                
                //알람 id 받기
                Log.d("로그", "AlarmService - onStartCommand : pendingId : $pendingId")
                alarmData = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAsId(pendingId / 4)!!
                Log.d("로그", "AlarmService - onStartCommand : ---------------")
                Log.d("로그", "AlarmReceiver - onReceive : NOT_ID : $NOTIFICATION_ID  pendingId : $pendingId : $alarmData")

                //DB에서 캘린더 데이터 가져오기
                val cal = Calendar.getInstance()
                val date = cal.time.let { Constants.sdf.format(it) }
                if(DatabaseManager.getInstance(applicationContext, "Alarms.db").selectCalendarAsDateAndName(date, alarmData.name) != null) {
                    calendarData = DatabaseManager.getInstance(applicationContext, "Alarms.db").selectCalendarAsDateAndName(date, alarmData.name)
                }
                Log.d("로그", "AlarmService - initView : calendarData : $calendarData")

                startNotification(pendingId)

                //서비스 시간 정해놓기 (미복용)
                Handler().postDelayed({
                    if(!takenFlag) {
                        Log.d("로그", "AlarmService - onStartCommand : 알람 시간 종료!!!!")
                        Toast.makeText(this, "약을 미복용했어요", Toast.LENGTH_SHORT).show()

                        var tmpCalendarData = CalendarData()
                        if(calendarData != null) tmpCalendarData = calendarData as CalendarData
                        tmpCalendarData.name = alarmData.name
                        when(pendingId % 4) {
                            0 -> tmpCalendarData.mtaken = 2
                            1 -> tmpCalendarData.ataken = 2
                            2 -> tmpCalendarData.etaken = 2
                            3 -> tmpCalendarData.ntaken = 2
                        }
                        //insert or modify 처리하기
                        //해당 날짜와 이름에 복용 정보가 0이 아니면 modify
                        if(calendarData == null) {
                            DatabaseManager.getInstance(this, "Alarms.db").insertCalendar(tmpCalendarData)
                        } else {
                            DatabaseManager.getInstance(this, "Alarms.db").updateCalendar(tmpCalendarData, alarmData.name)
                        }
                        stopSelf()
                    }
                }, SERVICE_TIME_OUT)
            }
            OFF_INTENT -> {
                Log.d("로그", "Alarmeceiver - onReceive : Service Off_intent 호출됨")
                stopSelf()
                stopMedia(intent, pendingId)
            }
        }
        return START_STICKY
    }

    //알림 소리 끄기
    private fun stopMedia(intent: Intent, pendingId: Int) {
        val alarmId = intent.extras?.getInt("PendingId")
        if(mediaPlayer.isPlaying && alarmId == pendingId) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
    }

    //알림 소리 켜기
    private fun startMedia() {
        //알람 소리 플레이
        val uri = Settings.System.DEFAULT_ALARM_ALERT_URI
        mediaPlayer = MediaPlayer.create(this, uri)
        if(!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d("로그", "AlarmService - onDestroy : 디스트로이 호출됨!!")
        mediaPlayer.stop()
        mediaPlayer.reset()
    }
    override fun onBind(intent: Intent?): IBinder? { return null}

    //노티 만들기
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun startNotification(pendingId: Int) {
        //팝업 인텐트 설정
        val popupIntent = Intent(this, SetAlarmPopupActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        Log.d("로그", "AlarmService - startNotification : pendingid : $pendingId")
        popupIntent.putExtra("PendingId", pendingId)
        val popupPendingIntent = PendingIntent.getActivity(this, 0, popupIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val title = makeNotificationTitle(alarmData, pendingId)
        val message = "배너를 클릭하고 '복용' 버튼을 꼭 눌러주세요."
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.drug)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(popupPendingIntent)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(message)
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            Log.d("로그", "AlarmService - startNotification : 오레오 if문 걸림")
        }
        startForeground(NOTIFICATION_ID, notification.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(CHANNEL_ID, "MyApp notification", NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "AppApp Tests"

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            notificationChannel)
    }

    //노티 알람 타이틀 만들기
    @SuppressLint("SimpleDateFormat")
    fun makeNotificationTitle(alarmData: AlarmData, pendingId: Int):  String {
        var maen = ""
        var ampm = "오전"
        val cal = Calendar.getInstance()
        when(pendingId % 4) {
            0 -> {
                maen = "아침"
                if(alarmData.mampm == 1) ampm = "오후"
                cal.set(Calendar.HOUR_OF_DAY, alarmData.mhour)
                cal.set(Calendar.MINUTE, alarmData.mminute)
            }
            1-> {
                maen = "점심"
                if(alarmData.aampm == 1) ampm = "오후"
                cal.set(Calendar.HOUR_OF_DAY, alarmData.ahour)
                cal.set(Calendar.MINUTE, alarmData.aminute)
            }
            2 -> {
                maen = "저녁"
                if(alarmData.eampm == 1) ampm = "오후"
                cal.set(Calendar.HOUR_OF_DAY, alarmData.ehour)
                cal.set(Calendar.MINUTE, alarmData.eminute)
            }
            3 -> {
                maen = "취침전"
                if(alarmData.nampm == 1) ampm = "오후"
                cal.set(Calendar.HOUR_OF_DAY, alarmData.nhour)
                cal.set(Calendar.MINUTE, alarmData.nminute)
            }
        }
        return "$maen - $ampm ${SimpleDateFormat("H:mm").format(cal.time)} ${alarmData.name} 먹을 시간이에요!"
    }
}