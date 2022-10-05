package com.hanait.wellinkalarmapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.databinding.ActivitySetAlarmVibrationAndMediaBinding
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.utils.Constants.isMediaOn
import com.hanait.wellinkalarmapplication.utils.Constants.isVibrationOn
import com.hanait.wellinkalarmapplication.utils.Constants.prefs
import com.hanait.wellinkalarmapplication.utils.Constants.userName

class SetAlarmVibrationAndMediaActivity : AppCompatActivity(), View.OnClickListener{


    private lateinit var binding: ActivitySetAlarmVibrationAndMediaBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlarmVibrationAndMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setAlarmVibrationAndMediaTextViewQuestion.text = "${userName}님 환영합니다!\n알림의 진동과 소리를 설정해주세요."
        binding.setAlarmVibrationAndMediaBtnNext.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmVibrationAndMediaBtnNext -> {
                if(binding.setAlarmVibrationAndMediaSwitchMedia.isChecked) {
                    isMediaOn = "1"
                    prefs.setString("media_on", "1")
                } else {
                    isMediaOn = "0"
                    prefs.setString("media_on", "0")
                }
                if(binding.setAlarmVibrationAndMediaSwitchVibration.isChecked) {
                    isVibrationOn = "1"
                    prefs.setString("vibration_on", "1")
                } else {
                    isVibrationOn = "0"
                    prefs.setString("vibration_on", "0")
                }

                val intent = Intent(this, SetAlarmActivity::class.java)
                startActivity(intent)
            }
        }
    }
}