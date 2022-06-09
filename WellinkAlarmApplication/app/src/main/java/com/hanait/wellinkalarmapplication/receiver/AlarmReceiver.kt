package com.hanait.wellinkalarmapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import com.hanait.wellinkalarmapplication.SplashActivity
import com.hanait.wellinkalarmapplication.service.AlarmService
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT

class AlarmReceiver : BroadcastReceiver(){
    var context : Context? = null

    companion object {
        var pendingId = 0
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("로그", "AlarmReceiver - onReceive : 리시버 호출됨")
        Log.d("로그", "AlarmReceiver - onReceive : intent : $intent")
        if(intent != null)     {
            val intentToService = Intent("android.intent.action.sec")
            try {
                val intentType = intent.extras?.getString("intentType")
                when(intentType) {
                    ADD_INTENT -> {
                        //데이터 전달받기
                        pendingId = intent.extras?.getInt("PendingId")!!
                        Log.d("로그", "AlarmReceiver - onReceive : AAAAAA")
                        //화면 깨우기
                        this.context = context
                        turnOnScreen()
                        Log.d("로그", "AlarmReceiver - onReceive : BBBBBB")
                        //서비스 인텐트 구성

                        intentToService.setClass(context!!, AlarmService::class.java)
                        intentToService.putExtra("ON_OFF", ADD_INTENT)
                        intentToService.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        Log.d("로그", "AlarmReceiver - onReceive : CCCCC")
                        //버전 체크 후 서비스 불러오기
                        startService(intentToService)
                        Log.d("로그", "AlarmReceiver - onReceive : DDDDDD")
                    }
                    OFF_INTENT -> {
                        val alarmId = intent.extras?.getInt("AlarmId")
                        intentToService.putExtra("ON_OFF", OFF_INTENT)
                        intentToService.putExtra("AlarmId", alarmId)

                        //버전 체크 후 서비스 불러오기
                        startService(intentToService)
                    }
                }
            } catch (e: Exception) {
                Log.d("로그", "AlarmReceiver - onReceive : ${e}")
                e.printStackTrace()
            }
        }
    }

    //서비스 버전 체크하기
    private fun startService(intentToService: Intent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) this.context!!.startForegroundService(intentToService)
        else this.context!!.startService(intentToService)
    }

    //화면 깨우기
    fun turnOnScreen() {
        val powerManager = this.context?.getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "app:alarm"
        )
        wakeLock.acquire(10*60*1000L /*10 minutes*/)
    }
}