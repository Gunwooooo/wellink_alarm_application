package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.ActivityHomeBinding
import com.hanait.wellinkalarmapplication.utils.OnSwipeTouchListener
import kotlin.system.exitProcess


class HomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHomeBinding
    private var waitTime = 0L

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.homeToolbar)
        supportActionBar?.title = ""
        supportFragmentManager.beginTransaction().replace(R.id.home_frameId, HomeCalendarFragment()).commitAllowingStateLoss()


        //바텀 네비게이션 리스너
        binding.homeBottomNav.setOnItemSelectedListener(object: NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.bottomNav_calendar -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.home_frameId, HomeCalendarFragment())
                            .commitAllowingStateLoss()
                        return true
                    }
                    R.id.bottomNav_alarm -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.home_frameId, HomeAlarmFragment())
                            .commitAllowingStateLoss()
                        return true
                    }
                    R.id.bottomNav_search -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.home_frameId, HomeSearchFragment())
                            .commitAllowingStateLoss()
                        return true
                    }
                    else -> return false
                }
            }
        })
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.home_toolbar_account -> {
                val intent = Intent(this, HomeAccountActivity::class.java)
                startActivity(intent)
            }
            R.id.home_toolbar_today -> {
                supportFragmentManager.beginTransaction().replace(R.id.home_frameId, HomeCalendarFragment()).commitAllowingStateLoss()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Override
    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime >=1500 ) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        } else {
            moveTaskToBack(true); // 태스크를 백그라운드로 이동
            finishAffinity()
            exitProcess(0)
        }
    }
}