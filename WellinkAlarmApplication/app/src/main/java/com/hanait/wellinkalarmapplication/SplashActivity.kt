package com.hanait.wellinkalarmapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.home.SearchAdapter
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.Item
import com.hanait.wellinkalarmapplication.retrofit.RetrofitManager
import com.hanait.wellinkalarmapplication.utils.CompletionResponse
import com.hanait.wellinkalarmapplication.utils.Constants.mAlarmList
import com.hanait.wellinkalarmapplication.utils.Constants.mSearchList
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {


    private val SPLASH_TIME_OUT:Long = 2000 //2초

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mAlarmList = DatabaseManager.getInstance(this, "Alarms.db").selectAlarmAll()
        getSearchData()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getSearchData() {
        //공공데이터 가져오기
        RetrofitManager.instance.duplicateCheckUser("한미약품(주)", 1, 1, 3 , completion = {
                completionResponse, s ->
            when(completionResponse) {
                CompletionResponse.OK -> {
                    Log.d("로그", "HomeSearchFragment - getSearchData : $s")
                    mSearchList = (s?.body?.items?.item as ArrayList<Item>?)!!
                }
                CompletionResponse.FAIL -> {
                    Log.d("로그", "HomeSearchFragment - onViewCreated : completion: Fail")
                }
            }
        })
    }
}