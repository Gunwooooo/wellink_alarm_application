package com.hanait.wellinkalarmapplication.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.ActivityHomeAccountBinding
import com.hanait.wellinkalarmapplication.databinding.ActivitySetAlarmBinding

class HomeAccountActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //toolbar 표시
        setSupportActionBar(binding.homeAccountToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
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