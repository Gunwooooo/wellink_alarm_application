package com.hanait.wellinkalarmapplication.utils

import android.annotation.SuppressLint
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.hanait.wellinkalarmapplication.db.PreferenceManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.Item
import java.text.SimpleDateFormat

@SuppressLint("StaticFieldLeak")
object Constants {
    lateinit var progressBar: ProgressBar
    lateinit var prevFragment: Fragment

    //저장된 유저 네임
    lateinit var userName: String

    //사용자 이름 저장을 위한 프리퍼런스
    lateinit var prefs: PreferenceManager

    //알람 진동 및 벨소리 설정 프리퍼런스
    lateinit var isMediaOn: String
    lateinit var isVibrationOn: String

    //알림 설정 시 사용하는 임시 데이터
    lateinit var tempAlarmData: AlarmData
    //알람 수정 시 사용하는 임시 데이터
    var tempAlarmData2: AlarmData? = null
    //simpleDateFormat
    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("yyyy-MM-dd")

    // 알람 서비스 스위치 상수
    const val ADD_INTENT = "ADD_INTENT"
    const val OFF_INTENT = "OFF_INTENT"

    var mAlarmList: ArrayList<AlarmData> = ArrayList()

    //서비스 list
    var mPendingIdList: ArrayList<Int> = ArrayList()
    var startServiceFlag = true
}

enum class CompletionResponse {
    OK,
    FAIL
}

object API {
    //공공데이터 사용자 인증키

    const val BASE_URL = "https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/"
    //////////////////////////////////////////////////////////////
    const val LOAD_SEARCH_DATA = "getDrbEasyDrugList"


}