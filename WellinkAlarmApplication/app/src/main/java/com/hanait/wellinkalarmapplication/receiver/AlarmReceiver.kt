package com.hanait.wellinkalarmapplication.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.service.AlarmService
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmPopupActivity.Companion.takenFlag
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT
import java.util.*

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
                        Log.d("로그", "AlarmReceiver - onReceive : pendingId : $pendingId")
                        //알람 주기 및 만기일 체크 후 울리게 하기
                        //DB에서 해당하는 약 데이터 가져오기
                        val cal = Calendar.getInstance()
                        val strDate = cal.time.let { Constants.sdf.format(it) }
                        var mAlarmList: ArrayList<AlarmData> = ArrayList()
                        mAlarmList = context?.let { DatabaseManager.getInstance(it, "Alarms.db").selectCalendarItemAlarm(strDate) }!!
                        for(i in 0 until mAlarmList.size) {
                            Log.d("로그", "AlarmReceiver - onReceive : 오늘 등록돼있는 알람 리스트 : ${mAlarmList[i]}")
                        }

                        //해당 주기에 맞는 알람이 맞는지 체크
                        var alarmSkipCheck = true
                        for(i in 0 until mAlarmList.size) {
                            //만기일이 지났을 경우 알람 삭제
                            if(mAlarmList[i].expired != "" && strDate > mAlarmList[i].expired) {
                                Log.d("로그", "AlarmReceiver - onReceive : 만기일이 지났습니다.")
                                val alarmIntent = PendingIntent.getBroadcast(context, pendingId, Intent(context, AlarmReceiver::class.java), 0)
                                val alarmManager = context.let { ContextCompat.getSystemService(it, AlarmManager::class.java) }
                                alarmManager?.cancel(alarmIntent)
                            }

                            //오늘 해당하는 알람이 아니면 check = false
                            if(mAlarmList[i].id == (pendingId / 4)) {
                                alarmSkipCheck = false
                            }
                        }
                        if(alarmSkipCheck) {
                            Log.d("로그", "AlarmReceiver - onReceive : 알람 울리는 날이 아닙니다.")
                            return
                        }

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
                    }
                    OFF_INTENT -> {
                        Log.d("로그", "AlarmReceiver - onReceive : Reciever Off_intent 호출됨")
                        val pendingId = intent.extras?.getInt("PendingId")
                        intentToService.putExtra("ON_OFF", OFF_INTENT)
                        intentToService.putExtra("PendingId", pendingId)

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