package com.hanait.wellinkalarmapplication.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData

import com.hanait.wellinkalarmapplication.service.AlarmService
import com.hanait.wellinkalarmapplication.service.AlarmService.Companion.takenFlag

import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.isMediaOn
import com.hanait.wellinkalarmapplication.utils.Constants.isVibrationOn
import com.hanait.wellinkalarmapplication.utils.Constants.mAlarmList
import com.hanait.wellinkalarmapplication.utils.Constants.mPendingIdList
import com.hanait.wellinkalarmapplication.utils.Constants.startServiceFlag
import com.hanait.wellinkalarmapplication.utils.CustomAlarmManager
import java.util.*

class AlarmReceiver : BroadcastReceiver(){
    var context : Context? = null
    private val handler = Handler()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {

        this.context = context
        var pendingId = intent?.extras?.getInt("PendingId")!!
        val intentToService = Intent(context, AlarmService::class.java)

        //부팅시 알람 재등록 필요
        if("android.intent.action.BOOT_COMPLETED" == intent.action) {
            //DB 알람 조회 후 모든 알람 재등록
            val alarmList = DatabaseManager.getInstance(context!!, "Alarms.db").selectAlarmAll()
            for(i in 0 until alarmList.size) {
                setAlarmManager(alarmList[i])
            }
        }

        try {
            val intentType = intent.extras?.getString("intentType")
            when(intentType) {
                ADD_INTENT -> {
                    //복용 여부 확인을 위한 변수

                    takenFlag = false

                    //알람 주기 및 기간 스킵 체크
                    if(alarmSkipCheck(pendingId)) return

                    //들어오는 모든 pendingId 저장
                    mPendingIdList.add(pendingId)

                    //서비스 인텐트 구성
                    intentToService.putExtra("ON_OFF", ADD_INTENT)
                    intentToService.putExtra("PendingId", pendingId)

                    //버전 체크 후 서비스 불러오기
                    handler.postDelayed({
                        //서비스는 한번만 호출하기
                        if(startServiceFlag && mPendingIdList.size != 0) {
                            startServiceFlag = false
                            startService(intentToService)
                        }
                    }, 7000) //10초동안 들어오는 서비스 모두 가져오기
                }
                OFF_INTENT -> {
                    pendingId = intent.extras?.getInt("PendingId")!!
                    intentToService.putExtra("ON_OFF", OFF_INTENT)
                    intentToService.putExtra("PendingId", pendingId)
                    //버전 체크 후 서비스 불러오기
                    startService(intentToService)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 스위치에 따른 알람 울리게 설정 및 취소 설정
    @RequiresApi(Build.VERSION_CODES.S)
    private fun setAlarmManager(alarmData: AlarmData) {
        val pendingId = alarmData.id * 4
        if(alarmData.mswitch == 1)
            CustomAlarmManager.getInstance(context).setAlarmManager(pendingId, alarmData.mampm, alarmData.mhour, alarmData.mminute, isMediaOn, isVibrationOn)
        if(alarmData.aswitch == 1)
            CustomAlarmManager.getInstance(context).setAlarmManager(pendingId + 1, alarmData.aampm, alarmData.ahour, alarmData.aminute, isMediaOn, isVibrationOn)
        if(alarmData.eswitch == 1)
            CustomAlarmManager.getInstance(context).setAlarmManager(pendingId + 2, alarmData.eampm, alarmData.ehour, alarmData.eminute, isMediaOn, isVibrationOn)
        if(alarmData.nswitch == 1)
            CustomAlarmManager.getInstance(context).setAlarmManager(pendingId + 3, alarmData.nampm, alarmData.nhour, alarmData.nminute, isMediaOn, isVibrationOn)
    }

    //알림이 울리는 날인지 체크
    @RequiresApi(Build.VERSION_CODES.S)
    private fun alarmSkipCheck(pendingId: Int) : Boolean {
        val cal = Calendar.getInstance()
        val strDate = cal.time.let { Constants.sdf.format(it) }

        //알람 주기 및 만기일 체크 후 울리게 하기
        val alarmData = DatabaseManager.getInstance(context!!, "Alarms.db").selectAlarmAsId(pendingId / 4)!!

        //만기일이 지났을 경우 알람 삭제
        if(alarmData.expired != "" && strDate > alarmData.expired) {
            val alarmIntent = PendingIntent.getBroadcast(context, pendingId, Intent(context, AlarmReceiver::class.java), 0)
            val alarmManager = context.let { ContextCompat.getSystemService(context!!, AlarmManager::class.java) }
            alarmManager?.cancel(alarmIntent)

            //DB에서 알람 삭제
            DatabaseManager.getInstance(context!!, "Alarms.db").deleteAlarm(alarmData.name)
            mAlarmList = DatabaseManager.getInstance(context!!, "Alarms.db").selectAlarmAll()
        }

        //DB에서 해당하는 약 데이터 가져오기
        val mAlarmList: ArrayList<AlarmData>
        mAlarmList = context?.let { DatabaseManager.getInstance(it, "Alarms.db").selectCalendarItemAsDate(strDate) }!!

        //해당 주기에 맞는 알람이 맞는지 체크
        var alarmSkipCheck = true
        for(i in 0 until mAlarmList.size) {
            //오늘 해당하는 알람이 아니면 check = false
            if(mAlarmList[i].id == (pendingId / 4)) {
                alarmSkipCheck = false
            }
        }
        if(alarmSkipCheck) {
            //주기에 해당안되는 알람 재설정 DB에서 알람 데이터 가져와서 알람 재설정
            when(pendingId % 4) {
                0 -> CustomAlarmManager.getInstance(context).setAlarmManager(pendingId, alarmData.mampm, alarmData.mhour, alarmData.mminute, isMediaOn, isVibrationOn)
                1 -> CustomAlarmManager.getInstance(context).setAlarmManager(pendingId, alarmData.aampm, alarmData.ahour, alarmData.aminute, isMediaOn, isVibrationOn)
                2 -> CustomAlarmManager.getInstance(context).setAlarmManager(pendingId, alarmData.eampm, alarmData.ehour, alarmData.eminute, isMediaOn, isVibrationOn)
                3 -> CustomAlarmManager.getInstance(context).setAlarmManager(pendingId, alarmData.nampm, alarmData.nhour, alarmData.nminute, isMediaOn, isVibrationOn)
            }
            return true
        }
        return false
    }

    //서비스 버전 체크하기
    private fun startService(intentToService: Intent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) this.context!!.startForegroundService(intentToService)
        else this.context!!.startService(intentToService)
    }
}