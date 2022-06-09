package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.hanait.wellinkalarmapplication.R

class SetAlarmPopupActivitiy : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm_popup_activitiy)

        window.addFlags(
                    android.R.attr.turnScreenOn or
                    android.R.attr.showWhenLocked
        )
        Log.d("로그", "SetAlarmPopupActivitiy - onCreate : 팝업 액티비티 호출됨!")
    }
}