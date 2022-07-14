package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.MainActivity
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.SetUserNameActivity
import com.hanait.wellinkalarmapplication.databinding.ActivityHomeAccountBinding
import com.hanait.wellinkalarmapplication.databinding.ActivitySetAlarmBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.prefs
import com.hanait.wellinkalarmapplication.utils.Constants.userName
import com.hanait.wellinkalarmapplication.utils.CustomDialogFragment
import java.util.*

class HomeAccountActivity : AppCompatActivity(), View.OnClickListener{


    private lateinit var binding : ActivityHomeAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeAccountBtnReset.setOnClickListener(this)
        binding.homeAccountBtnRename.setOnClickListener(this)
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
                showDialog()
            }
            binding.homeAccountBtnRename -> {
                //이름 초기화
                prefs.setString("user_name", "")

                //메인 화면으로 이동
                val intent = Intent(applicationContext, SetUserNameActivity::class.java)
                intent.putExtra("SkipSetAlarm", true)
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

    private fun showDialog() {
        val customDialog = CustomDialogFragment(R.layout.home_account_reset_dialog, null)
        customDialog.setDialogListener(object: CustomDialogFragment.AccountResetListener {
            override fun onPositiveClicked() {
                //이름 초기화
                prefs.setString("user_name", "")
                //복용 정보 초기화
                DatabaseManager.getInstance(applicationContext, "Alarms.db").resetCalendar()
                //알람 데이터 초기화
                DatabaseManager.getInstance(applicationContext, "Alarms.db").resetAlarm()

                Toast.makeText(applicationContext, "데이터가 초기화되었습니다.", Toast.LENGTH_SHORT).show()
                
                //메인 화면으로 이동
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onNegativeClicked() {
                customDialog.dismiss()
            }
        })
        fragmentManager?.let { customDialog.show(supportFragmentManager, "home_calendar_dialog") }
    }
}