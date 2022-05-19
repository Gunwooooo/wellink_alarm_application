package com.hanait.wellinkalarmapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.databinding.ActivitySetUserNameBinding
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.utils.Constants.prefs
import com.hanait.wellinkalarmapplication.utils.Constants.userName

class SetUserNameActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySetUserNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
                val intent = Intent(this, SetAlarmActivity::class.java)
                startActivity(intent)
            }
        }
    }
}