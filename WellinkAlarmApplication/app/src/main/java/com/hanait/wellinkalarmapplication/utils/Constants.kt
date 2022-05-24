package com.hanait.wellinkalarmapplication.utils

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.hanait.wellinkalarmapplication.beans.AlarmData
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.db.PreferenceManager

@SuppressLint("StaticFieldLeak")
object Constants {
    lateinit var progressBar: ProgressBar
    lateinit var prevFragment: Fragment

    //저장된 유저 네임
    lateinit var userName: String

    //사용자 이름 저장을 위한 프리퍼런스
    lateinit var prefs: PreferenceManager

    //알림 설정 시 사용하는 임시 데이터
    lateinit var tempAlarmData: AlarmData
}