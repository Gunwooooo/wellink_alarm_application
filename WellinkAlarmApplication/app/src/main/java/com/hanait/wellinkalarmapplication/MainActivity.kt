package com.hanait.wellinkalarmapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.databinding.ActivityMainBinding
import com.hanait.wellinkalarmapplication.utils.MainViewPagerFragmentAdapter
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var waitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //사용 설명서 뷰페이저 연결
        val viewPager = binding.mainViewPager
        viewPager.adapter = MainViewPagerFragmentAdapter(this)
        binding.mainIndicator.setViewPager(viewPager)
        binding.mainIndicator.createIndicators(4, 0)

        binding.mainBtnStart.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.mainBtnStart -> {
                val intent = Intent(this, SetUserNameActivity::class.java)
                //초기 알람 설정 스킵 여부 체크
                intent.putExtra("SkipSetAlarm", false)
                startActivity(intent)
                return
            }
        }
    }

    //뒤로가기 앱 종료
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
}