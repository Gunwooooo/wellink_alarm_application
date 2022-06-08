package com.hanait.wellinkalarmapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.hanait.wellinkalarmapplication.service.AlarmService
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT

class AlarmReceiver : BroadcastReceiver(){
    companion object {
        var pendingId = 0
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("로그", "AlarmReceiver - onReceive : 리시버 호출됨")
        Log.d("로그", "AlarmReceiver - onReceive : intent : $intent")
        if(intent != null)     {
            val intentToService = Intent(context, AlarmService::class.java)
            try {
                val intentType = intent.extras?.getString("intentType")
                when(intentType) {
                    ADD_INTENT -> {
                        //화면 깨우기
                        val powerManager = context?.getSystemService(POWER_SERVICE) as PowerManager
                        val wakeLock = powerManager.newWakeLock(
                            PowerManager.FULL_WAKE_LOCK or
                                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                                    PowerManager.ON_AFTER_RELEASE, "My:Tag"
                        )
                        wakeLock.acquire(5000)

                        pendingId = intent.extras?.getInt("PendingId")!!
                        intentToService.putExtra("ON_OFF", ADD_INTENT)

                        context.startService(intentToService)
                        Log.d("로그", "AlarmReceiver - onReceive : DDDD")
                    }
                    OFF_INTENT -> {
                        val alarmId = intent.extras?.getInt("AlarmId")
                        intentToService.putExtra("ON_OFF", OFF_INTENT)
                        intentToService.putExtra("AlarmId", alarmId)
                        context?.startService(intentToService)
                    }
                }
            } catch (e: Exception) {
                Log.d("로그", "AlarmReceiver - onReceive : $^^^^^^")
                e.printStackTrace()
            }
        }
    }
}