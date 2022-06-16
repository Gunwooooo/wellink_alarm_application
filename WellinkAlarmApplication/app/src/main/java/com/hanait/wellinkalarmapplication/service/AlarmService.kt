package com.hanait.wellinkalarmapplication.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver.Companion.pendingId
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmPopupActivitiy
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT
import java.text.SimpleDateFormat
import java.util.*


class AlarmService: Service() {

    companion object {
        const val CHANNEL_ID = "primary_notification_channel"
        const val NOTIFICATION_ID = 1001
    }

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("로그", "AlarmService - onStartCommand : 서비스 호출됨")
        val onOff = intent?.extras?.getString("ON_OFF")

        when(onOff) {
            ADD_INTENT -> {
                //알람 id 받기
                val alarmData = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAsId(pendingId / 4)
                Log.d("로그", "AlarmReceiver - onReceive : $pendingId : $alarmData")

                //팝업 인텐트 설정
                val popupIntent = Intent(this, SetAlarmPopupActivitiy::class.java).apply {
                    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                popupIntent.putExtra("alarmId", pendingId)
                val popupPendingIntent = PendingIntent.getActivity(this, 0, popupIntent, 0)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                    val title = makeNotificationTitle(alarmData)
                    val message = "배너를 클릭하고 '복용' 버튼을 꼭 눌러주세요."
                    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSmallIcon(R.drawable.ic_alarm)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(popupPendingIntent)
                        .setAutoCancel(true)
                        .setStyle(
                            NotificationCompat.BigTextStyle().bigText(message)
                        )
                    Log.d("Test", "start foreground")
                    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(NOTIFICATION_ID, notification.build())
                }

                //알람 소리 플레이
                val uri = Settings.System.DEFAULT_ALARM_ALERT_URI
                mediaPlayer = MediaPlayer.create(this, uri)
                mediaPlayer.start()
            }
            OFF_INTENT -> {
                Log.d("로그", "AlarmReceiver - onReceive : Service Off_intent 호출됨")
                val alarmId = intent.extras?.getInt("AlarmId")
                if(mediaPlayer.isPlaying && alarmId == pendingId) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.reset()
    }
    override fun onBind(intent: Intent?): IBinder? { return null}

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
    fun makeNotificationTitle(alarmData: AlarmData):  String {
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