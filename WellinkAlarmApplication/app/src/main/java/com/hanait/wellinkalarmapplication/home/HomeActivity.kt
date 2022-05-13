package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.ActivityHomeBinding
import com.hanait.wellinkalarmapplication.utils.OnSwipeTouchListener


class HomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHomeBinding

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
                    R.id.bottomNav_medication -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.home_frameId, HomeMedicineFragment())
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
        }
        return super.onOptionsItemSelected(item)
    }
}