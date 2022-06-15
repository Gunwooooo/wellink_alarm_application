package com.hanait.wellinkalarmapplication.setAlarm

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

    override fun onClick(v: View?) {
        when(v) {
            binding.SetAlarmPopupBtnCheck -> {
                Toast.makeText(this, "약을 복용하였습니다.", Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
        }
    }
}