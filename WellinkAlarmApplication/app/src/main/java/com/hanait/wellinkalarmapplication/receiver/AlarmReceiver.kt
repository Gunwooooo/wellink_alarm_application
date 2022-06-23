package com.hanait.wellinkalarmapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import com.hanait.wellinkalarmapplication.service.AlarmService
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmPopupActivity.Companion.takenFlag
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT

class AlarmReceiver : BroadcastReceiver(){
    var context : Context? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context
        Log.d("로그", "AlarmReceiver - onReceive : 리시버 호출됨")
        // this hold information to service
        val intentToService = Intent(context, AlarmService::class.java)
        if(intent != null)     {
            try {
                val intentType = intent.extras?.getString("intentType")
                when(intentType) {
                    ADD_INTENT -> {
                        //데이터 전달받기
                        val pendingId = intent.extras?.getInt("PendingId")!!
    
                        //복용 여부 확인을 위한 변수
                        takenFlag = false
                        
                        //화면 깨우기
                        turnOnScreen()

                        //서비스 인텐트 구성
//                        intentToService.putExtra("PendingId", pendingId)
                        intentToService.putExtra("ON_OFF", ADD_INTENT)
                        intentToService.putExtra("PendingId", pendingId)

                        //버전 체크 후 서비스 불러오기
                        startService(intentToService)
                        Log.d("로그", "AlarmReceiver - onReceive : #######")
                    }
                    OFF_INTENT -> {
                        Log.d("로그", "AlarmReceiver - onReceive : Reciever Off_intent 호출됨")
                        val alarmId = intent.extras?.getInt("PendingId")
                        intentToService.putExtra("ON_OFF", OFF_INTENT)
                        intentToService.putExtra("AlarmId", alarmId)

                        //버전 체크 후 서비스 불러오기
                        startService(intentToService)
                    }
                }
            } catch (e: Exception) {
                Log.d("로그", "AlarmReceiver - onReceive : $e")
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
    private fun turnOnScreen() {
        val powerManager = this.context?.getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "app:alarm"
        )
        wakeLock.acquire(10*60*1000L /*10 minutes*/)
    }
}