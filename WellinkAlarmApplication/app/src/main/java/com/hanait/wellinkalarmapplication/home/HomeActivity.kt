package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.homeToolbar)
        supportActionBar?.title = ""
        supportFragmentManager.beginTransaction().replace(R.id.home_frameId, HomeCalendarFragment()).commitAllowingStateLoss()
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_home, menu)
        return true
    }
}