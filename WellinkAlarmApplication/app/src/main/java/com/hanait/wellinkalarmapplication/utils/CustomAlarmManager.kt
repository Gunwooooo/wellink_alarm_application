package com.hanait.wellinkalarmapplication.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.utils.Constants.isMediaOn
import com.hanait.wellinkalarmapplication.utils.Constants.isVibrationOn
import java.util.*

class CustomAlarmManager(context: Context) {
    private var context: Context

    companion object {
        @SuppressLint("StaticFieldLeak")
        var INSTANCE: CustomAlarmManager? = null
        fun getInstance(context: Context?): CustomAlarmManager {
            if (INSTANCE == null) {
                INSTANCE = CustomAlarmManager(context!!)
            }
            return INSTANCE as CustomAlarmManager
        }
    }
    init {
        this.context = context
    }

    //실제 알람 설정
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun setAlarmManager(pendingId: Int, ampm:Int, hour: Int, minute: Int, isMediaOn: String, isVibrationOn: String) {
        val myCalendar = Calendar.getInstance()
        val calendar = myCalendar.clone() as Calendar
        if(ampm == 1 && hour != 12)
            calendar.set(Calendar.HOUR_OF_DAY, hour + 12)
        else calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        if(calendar <= myCalendar) {
            calendar.add(Calendar.DATE, 1)
        }
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("intentType", Constants.ADD_INTENT)
        intent.putExtra("PendingId", pendingId)
        val alarmIntent = PendingIntent.getBroadcast(context, pendingId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        val alarmManager = context.let { ContextCompat.getSystemService(it, AlarmManager::class.java) }
        alarmManager?.cancel(alarmIntent)
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            alarmManager?.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
//        } else {
        val alarmClockInfo = AlarmManager.AlarmClockInfo(calendar.timeInMillis, null)
        alarmManager?.setAlarmClock(alarmClockInfo, alarmIntent)
//            alarmManager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
    }
}