package com.hanait.wellinkalarmapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.databinding.ActivitySetUserNameBinding
import com.hanait.wellinkalarmapplication.home.HomeActivity
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.utils.Constants.prefs
import com.hanait.wellinkalarmapplication.utils.Constants.userName

class SetUserNameActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySetUserNameBinding
    private var skipSetAlarm = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //알람 설정 스킵 유무 판단
        skipSetAlarm = intent.extras!!.getBoolean("SkipSetAlarm")


        binding.setUserNameBtnNext.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setUserNameBtnNext -> {
                val inputUserName = binding.setUserNameEditTextUserName.text.toString()
                if(inputUserName == "") {
                    Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                    return
                }
                prefs.setString("user_name", inputUserName)
                userName = inputUserName
                
                //초기 이름 설정이 아니면 바로 homeActivity로 이동
                if(skipSetAlarm) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "${userName}님 환영합니다.", Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }
                val intent = Intent(this, SetAlarmVibrationAndMediaActivity::class.java)
                startActivity(intent)
            }
        }
    }
}