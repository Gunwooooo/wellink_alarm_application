package com.hanait.wellinkalarmapplication.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.hanait.wellinkalarmapplication.MainActivity
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.ActivityHomeAccountBinding
import com.hanait.wellinkalarmapplication.databinding.ActivitySetAlarmBinding
import com.hanait.wellinkalarmapplication.utils.Constants.prefs
import com.hanait.wellinkalarmapplication.utils.Constants.userName

class HomeAccountActivity : AppCompatActivity(), View.OnClickListener{


    private lateinit var binding : ActivityHomeAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeAccountBtnReset.setOnClickListener(this)

        //타이틀 사용자 이름으로 설정
        Log.d("로그", "HomeAccountActivity - onCreate :  유저 네임 $userName")
        binding.homeAccountTextViewTitle.text = "${userName}님의 정보"

        //toolbar 표시
        setSupportActionBar(binding.homeAccountToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.homeAccountBtnReset -> {
                prefs.setString("user_name", "")
                Toast.makeText(this, "데이터가 초기화되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    //toolbar 클릭 리스너
    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}