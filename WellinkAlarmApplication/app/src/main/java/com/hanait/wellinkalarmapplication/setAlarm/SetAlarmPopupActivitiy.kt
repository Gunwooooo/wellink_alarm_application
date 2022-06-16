package com.hanait.wellinkalarmapplication.setAlarm

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.ActivitySetAlarmBinding
import com.hanait.wellinkalarmapplication.databinding.ActivitySetAlarmPopupActivitiyBinding
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.utils.Constants
import kotlin.system.exitProcess

class SetAlarmPopupActivitiy : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivitySetAlarmPopupActivitiyBinding

    private val decorView: View? = null
    private var uiOption = 0

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()
        binding = ActivitySetAlarmPopupActivitiyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.SetAlarmPopupBtnCheck.setOnClickListener(this)
        Log.d("로그", "SetAlarmPopupActivitiy - onCreate : 팝업 액티비티 호출됨!")
    }

    override fun onBackPressed() {
//        return
    }

    //full screen으로 만들기
    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        var newUiOptions = window.decorView.systemUiVisibility
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = newUiOptions
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onClick(v: View?) {
        when(v) {
            binding.SetAlarmPopupBtnCheck -> {
                Toast.makeText(this, "약을 복용하였습니다.", Toast.LENGTH_SHORT).show()

                val alarmId = intent.extras?.getInt("alarmId")
                Log.d("로그", "SetAlarmPopupActivitiy - onClick : $alarmId")
                //알림 끄는 broadcast
                val intent = Intent(this, AlarmReceiver::class.java)
                intent.putExtra("intentType", Constants.OFF_INTENT)
                intent.putExtra("AlarmId", alarmId)
                sendBroadcast(intent)

                moveTaskToBack(true); // 태스크를 백그라운드로 이동
                finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
                exitProcess(0);
            }
        }
    }
}