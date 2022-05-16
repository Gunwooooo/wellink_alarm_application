package com.hanait.wellinkalarmapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hanait.wellinkalarmapplication.databinding.ActivityMainBinding
import com.hanait.wellinkalarmapplication.databinding.ActivitySetUserNameBinding
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.utils.Constants

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
                val intent = Intent(this, SetAlarmActivity::class.java)
                startActivity(intent)
            }
        }
    }
}