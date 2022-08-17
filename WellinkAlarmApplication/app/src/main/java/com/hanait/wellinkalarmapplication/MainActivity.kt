package com.hanait.wellinkalarmapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.databinding.ActivityMainBinding
import com.hanait.wellinkalarmapplication.utils.MainViewPagerFragmentAdapter
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var waitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainBtnStart.setOnClickListener(this)

        val viewPager = binding.mainViewPager
        viewPager.adapter = MainViewPagerFragmentAdapter(this)
        binding.mainIndicator.setViewPager(viewPager)
        binding.mainIndicator.createIndicators(5, 0)
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
                val intent = Intent(this, SetUserNameActivity::class.java)
                intent.putExtra("SkipSetAlarm", false)
                startActivity(intent)
                return
            }
        }
    }
}