package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.ActivitySetAlarmBinding

class SetAlarmActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySetAlarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.setAlarmProgressBar
        progressBar.indeterminateDrawable

        //toolbar 표시
        setSupportActionBar(binding.setAlarmToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        //첫 프래그먼트 이름 입력받는 인플레이트
        supportFragmentManager.beginTransaction().replace(R.id.setAlarm_frameId, SetUserNameFragment()).commitAllowingStateLoss()
    }

    //프래그먼트 이동 메서드
    fun changeFragment(fragmentName: String) {
        when(fragmentName) {
            "SetAlarmNameFragment" -> supportFragmentManager.beginTransaction().replace(R.id.setAlarm_frameId, SetAlarmNameFragment()).commitAllowingStateLoss()
            "SetAlarmPeriodFragment" -> supportFragmentManager.beginTransaction().replace(R.id.setAlarm_frameId, SetAlarmPeriodFragment()).commitAllowingStateLoss()
            "SetAlarmExpiredFragment" -> supportFragmentManager.beginTransaction().replace(R.id.setAlarm_frameId, SetAlarmExpiredFragment()).commitAllowingStateLoss()
            "SetAlarmTimeFragment" -> supportFragmentManager.beginTransaction().replace(R.id.setAlarm_frameId, SetAlarmTimeFragment()).commitAllowingStateLoss()
        }
    }

    private fun changePrevFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.setAlarm_frameId, prevFragment).commitAllowingStateLoss()
    }

    //toolbar 클릭 리스너
    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                changePrevFragment()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        changePrevFragment()
    }
}