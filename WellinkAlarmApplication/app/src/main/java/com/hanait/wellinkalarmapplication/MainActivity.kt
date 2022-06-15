package com.hanait.wellinkalarmapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.databinding.ActivityMainBinding
import com.hanait.wellinkalarmapplication.db.PreferenceManager
import com.hanait.wellinkalarmapplication.home.HomeActivity
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.userName
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var waitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainBtnStart.setOnClickListener(this)

        //sharedPreference에 있는 사용자 이름 가져오기
        Constants.prefs = PreferenceManager(applicationContext)
    }

    @Override
    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime >=1500 ) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            moveTaskToBack(true); // 태스크를 백그라운드로 이동
            finishAffinity()
            exitProcess(0)
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.mainBtnStart -> {
                userName = Constants.prefs.getString("user_name", "")
                if(userName == "") {
                    val intent = Intent(this, SetUserNameActivity::class.java)
                    startActivity(intent)
                    return
                }
                val intent = Intent(this, HomeActivity::class.java)
                Toast.makeText(this, "${userName}님 환영합니다.", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        }
    }
}