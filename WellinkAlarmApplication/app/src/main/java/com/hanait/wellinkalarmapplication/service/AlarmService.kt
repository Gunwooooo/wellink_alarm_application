package com.hanait.wellinkalarmapplication.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.hanait.wellinkalarmapplication.MainActivity
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmPopupActivitiy
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT


class AlarmService: Service() {

    companion object {
        const val CHANNEL_ID = "primary_notification_channel"
        const val NOTIFICATION_ID = 1001
    }

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("로그", "AlarmService - onStartCommand : 서비스 호출됨")

        val onOff = intent?.extras?.getString("ON_OFF")
        when(onOff) {
            ADD_INTENT -> {

                val fullScreenIntent = Intent(this, SetAlarmPopupActivitiy::class.java)
                val fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                val message = "배너를 클릭하고 '복용' 버튼을 꼭 눌러주세요."
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                        .apply {
                            setSmallIcon(R.drawable.ic_alarm)
                            setContentTitle("오전 OO:OO OO약 먹을 시간이에요!")
                            setContentText(message)
                            priority = NotificationCompat.PRIORITY_HIGH
                            setCategory(NotificationCompat.CATEGORY_ALARM)
                            setAutoCancel(true)
                            setStyle(
                                NotificationCompat.BigTextStyle().bigText(message)
                            )
//                            addAction(
//                                R.drawable.ic_baseline_add_24,
//                                "Snooze",
//                                snoo
//                            )
                            setFullScreenIntent(fullScreenPendingIntent, true)
                        }
                        .build()
                    Log.d("Test", "start foreground")
                    startForeground(NOTIFICATION_ID, notification)
                }

                //알람 소리 플레이
                val uri = Settings.System.DEFAULT_ALARM_ALERT_URI
                mediaPlayer = MediaPlayer.create(this, uri)
                mediaPlayer.start()
            }
            OFF_INTENT -> {
                val alarmId = intent.extras?.getInt("AlarmId")
                if(mediaPlayer.isPlaying && alarmId == AlarmReceiver.pendingId) {
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
}