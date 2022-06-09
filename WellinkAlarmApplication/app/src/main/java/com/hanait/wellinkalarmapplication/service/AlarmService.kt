package com.hanait.wellinkalarmapplication.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmPopupActivitiy
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT

class AlarmService: Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("로그", "AlarmService - onStartCommand : 서비스 호출됨")

        val onOff = intent?.extras?.getString("ON_OFF")
        when(onOff) {
            ADD_INTENT -> {
                val uri = Settings.System.DEFAULT_ALARM_ALERT_URI
                mediaPlayer = MediaPlayer.create(this, uri)
                mediaPlayer.start()

                val powerManager = getSystemService(POWER_SERVICE) as PowerManager
                val isScreenOn = powerManager.isInteractive

                if(isScreenOn) {
                    Log.d("로그", "AlarmService - onStartCommand : screen on")
                } else {
                    Log.d("로그", "AlarmService - onStartCommand : screen off")
                }
                val popupIntent = Intent(this, SetAlarmPopupActivitiy::class.java )
                popupIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                startActivity(popupIntent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    stopForeground(true);
                } else {
                    stopSelf();
                }
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
}