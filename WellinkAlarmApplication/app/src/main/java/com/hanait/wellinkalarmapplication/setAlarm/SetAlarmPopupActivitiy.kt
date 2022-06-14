package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.R

class SetAlarmPopupActivitiy : AppCompatActivity() {

    private val decorView: View? = null
    private var uiOption = 0

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        var newUiOptions = window.decorView.systemUiVisibility
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = newUiOptions


        setContentView(R.layout.activity_set_alarm_popup_activitiy)





        Log.d("로그", "SetAlarmPopupActivitiy - onCreate : 팝업 액티비티 호출됨!")
    }

    override fun onBackPressed() {
//        return
    }
}