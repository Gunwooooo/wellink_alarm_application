package com.hanait.wellinkalarmapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.utils.Constants.mAlarmList

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {


    private val SPLASH_TIME_OUT:Long = 2000 //2초

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mAlarmList = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAll()
        Log.d("로그", "HomeAlarmFragment - getAlarmList : 알람 갯수 : ${mAlarmList.size}")

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}