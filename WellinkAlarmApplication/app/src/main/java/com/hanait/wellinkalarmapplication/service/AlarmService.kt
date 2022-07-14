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
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.CalendarData
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmPopupActivity
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.mPendingIdList
import com.hanait.wellinkalarmapplication.utils.Constants.startServiceFlag
import java.text.SimpleDateFormat
import java.util.*


class AlarmService: Service() {
    private val handler = Handler()
    lateinit var alarmData: AlarmData
    private lateinit var mediaPlayer: MediaPlayer

    companion object {
        const val SERVICE_TIME_OUT: Long = 20000 //1분
        const val CHANNEL_ID = "primary_notification_channel"
        var takenFlag = false
    }

    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val onOff = intent?.extras?.getString("ON_OFF")
        val pendingId = intent?.extras?.getInt("PendingId")!!

        Log.d("로그", "AlarmService - onStartCommand : $pendingId 서비스 호출됨")
        when(onOff) {
            ADD_INTENT -> {

//              알람 id 받기
                alarmData = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAsId(pendingId / 4)!!

                //들어온 모든 서비스 갯수
                Log.d("로그", "AlarmService - onStartCommand : 들어온 모든 서비스 갯수 : ${mPendingIdList.size}")

                //노티 띄우기
                startNotification(pendingId)

                //벨소리 울리기
                startMedia()
                handler.postDelayed({
                    Log.d("로그", "AlarmService - onStartCommand : pendingId : $pendingId   takenFlag : $takenFlag")
                    if(!takenFlag) {
                        Log.d("로그", "AlarmService - onStartCommand : 알람 시간 종료!")

                        Toast.makeText(this, "약을 미복용했어요", Toast.LENGTH_SHORT).show()

                        //서비스 갯수만큼 반복
                        for(i in 0 until mPendingIdList.size) {
                            val alarmData = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAsId(
                                mPendingIdList[i] / 4)!!
                            //DB에서 캘린더 데이터 가져오기
                            val calendarData = getCalendarData(alarmData.name)
                            //DB에 복용 정보 저장 or 수정 하기
                            setCalendarData(mPendingIdList[i], alarmData.name, calendarData)
                        }
                        //서비스 종료
                        stopSelf()
                    }
                }, SERVICE_TIME_OUT)
            }
            OFF_INTENT -> {
                Log.d("로그", "Alarmeceiver - onReceive : Service Off_intent 호출됨")
                stopMedia(intent, pendingId)
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun setCalendarData(pendingId: Int, alarmName: String, calendarData: CalendarData?) {
        //서비스 시간 정해놓기 (미복용)
        var tmpCalendarData = CalendarData()
        if(calendarData != null) tmpCalendarData = calendarData as CalendarData
        tmpCalendarData.name = alarmName
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
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d("로그", "AlarmService - onDestroy : 서비스 디스트로이 호출")
        
        //음악 제거
        mediaPlayer.stop()
        mediaPlayer.reset()
        
        //서비스 리스트 비우기
        mPendingIdList.clear()
        startServiceFlag = true
    }

    //알림 소리 끄기
    private fun stopMedia(intent: Intent, pendingId: Int) {
        val pendingId2 = intent.extras?.getInt("PendingId")
        if(mediaPlayer.isPlaying && pendingId == pendingId2) {
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

    //노티 만들기
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun startNotification(pendingId: Int) {
        //팝업 인텐트 설정
        val popupIntent = Intent(this, SetAlarmPopupActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        popupIntent.putExtra("PendingId", pendingId)
        popupIntent.putExtra("PendingIdList", mPendingIdList)
        val popupPendingIntent = PendingIntent.getActivity(this, 0, popupIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val title = makeNotificationTitle(alarmData, pendingId)
        val message = "배너를 클릭하고 '복용' 버튼을 꼭 눌러주세요."
        val notification = NotificationCompat.Builder(this, AlarmService.CHANNEL_ID)
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
            createNotificationChannel(pendingId, notification)
        }
        Log.d("로그", "AlarmService - startNotification : 알람 펜딩s 아이디 : $pendingId")
        startForeground(pendingId, notification.build())
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(pendingId: Int, notification: NotificationCompat.Builder) {
        Log.d("로그", "AlarmService - createNotificationChannel : createNotification 호출됨!!!")
        val notificationChannel = NotificationChannel(CHANNEL_ID, "MyApp notification", NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "App Tests"
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
        return "$maen - $ampm ${SimpleDateFormat("H:mm").format(cal.time)} ${makeAlarmNameAsList()} 먹을 시간이에요!"
    }

    override fun onBind(intent: Intent?): IBinder? { return null}
}