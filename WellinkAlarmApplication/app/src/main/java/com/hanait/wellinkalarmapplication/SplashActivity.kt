package com.hanait.wellinkalarmapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.db.PreferenceManager
import com.hanait.wellinkalarmapplication.home.HomeActivity
import com.hanait.wellinkalarmapplication.utils.Constants.isMediaOn
import com.hanait.wellinkalarmapplication.utils.Constants.isVibrationOn
import com.hanait.wellinkalarmapplication.utils.Constants.mAlarmList
import com.hanait.wellinkalarmapplication.utils.Constants.prefs
import com.hanait.wellinkalarmapplication.utils.Constants.userName
import java.util.*


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    //전체 높이값 가져오기
    companion object {
        var _windowHeight = 0f
    }

    private val SPLASH_TIME_OUT:Long = 2000 //2초

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //캘린더 높이 계산을 위한 전체 높이 저장
        val displayMetrics = resources.displayMetrics
        _windowHeight = displayMetrics.heightPixels / displayMetrics.density
        
        mAlarmList = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAll()

        //sharedPreference에 있는 사용자 이름 가져오기
        prefs = PreferenceManager(applicationContext)
        userName = prefs.getString("user_name", "")
        isMediaOn = prefs.getString("media_on", "0")
        isVibrationOn = prefs.getString("vibration_on", "1")

        Handler().postDelayed({
            if(userName == "")
                startActivity(Intent(this, MainActivity::class.java))
            else {
                Toast.makeText(this, "${userName}님 환영합니다.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)
    }
}