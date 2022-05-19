package com.hanait.wellinkalarmapplication.setAlarm

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmTimeBinding
import com.hanait.wellinkalarmapplication.home.HomeActivity
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.dbManager
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class SetAlarmTimeFragment : BaseFragment<FragmentSetAlarmTimeBinding>(FragmentSetAlarmTimeBinding::inflate), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmTimeBtnNext -> {
                if(isAllSwitchUnchecked()) {
                    Toast.makeText(context, "시간을 설정하고 스위치를 켜주세요", Toast.LENGTH_SHORT).show()
                    return
                }

                saveSwitchData()
                Log.d("로그", "SetAlarmTimeFragment - onClick : $tempAlarmData")

                dbManager.insert(tempAlarmData)
                Toast.makeText(context, "약이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }
            //카드뷰 클릭 시 다이어로그 생성
            binding.setAlarmTimeCardViewMorning -> makeDialog(binding.setAlarmTimeTextViewMTime, 7, 0)
            binding.setAlarmTimeCardViewAfternoon -> makeDialog(binding.setAlarmTimeTextViewATime, 12, 30)
            binding.setAlarmTimeCardViewEvening -> makeDialog(binding.setAlarmTimeTextViewETime, 18, 30)
            binding.setAlarmTimeCardViewNight -> makeDialog(binding.setAlarmTimeTextViewNTime, 22, 0)
        }
    }
    //스위치 변화 이벤트 감지
    override fun onCheckedChanged(v: CompoundButton?, isChecked: Boolean) {
        makeExplainText()
    }

    //시간 설정 다이어로그 생성
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun makeDialog(v: TextView, cHour:Int, cMinute: Int){
        Log.d("로그", "SetAlarmTimeFragment - getTime : 다이어로그 호출됨")
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            Log.d("로그", "$hour : $minute")
            var tmpHour = hour
            var tmpAmpm = "오전"
            if(hour >= 12) {
                if(hour >= 13) tmpHour -= 12
                tmpAmpm = "오후"
            }
            cal.set(Calendar.HOUR_OF_DAY, tmpHour)
            cal.set(Calendar.MINUTE, minute)
            v.text = "$tmpAmpm ${SimpleDateFormat("H:mm").format(cal.time)}"

            //tempAlarmData에 시간 저장
            saveTimeData(v, tmpAmpm, tmpHour, minute)

        }
        //스피너형 다이어로그 생성
        val timePickerDialog = TimePickerDialog(context, android.R.style.Theme_Holo_Dialog_NoActionBar, timeSetListener, cHour, cMinute, false)
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    //마지막에 모든 스위치 데이터 tempAlarmData에 저장
    private fun saveSwitchData() {
        if(binding.setAlarmTimeSwitchMorning.isChecked) tempAlarmData.mswitch = 1
        if(binding.setAlarmTimeSwitchAfternoon.isChecked) tempAlarmData.aswitch = 1
        if(binding.setAlarmTimeSwitchEvening.isChecked) tempAlarmData.eswitch = 1
        if(binding.setAlarmTimeSwitchNight.isChecked) tempAlarmData.nswitch = 1
    }

    //시간 데이터 tempAlarmData에 저장
    private fun saveTimeData(v: TextView, tmpAmpm: String, tmpHour: Int, minute: Int) {
        var ampm = 0
        if(tmpAmpm == "오후") ampm = 1
        when(v) {
            binding.setAlarmTimeTextViewMTime -> {
                tempAlarmData.mampm = ampm
                tempAlarmData.mhour = tmpHour
                tempAlarmData.mminute = minute
            }
            binding.setAlarmTimeTextViewATime -> {
                tempAlarmData.aampm = ampm
                tempAlarmData.ahour = tmpHour
                tempAlarmData.aminute = minute
            }
            binding.setAlarmTimeTextViewETime -> {
                tempAlarmData.eampm = ampm
                tempAlarmData.ehour = tmpHour
                tempAlarmData.eminute = minute
            }
            binding.setAlarmTimeTextViewNTime -> {
                tempAlarmData.nampm = ampm
                tempAlarmData.nhour = tmpHour
                tempAlarmData.nminute = minute
            }
        }
    }

    //모든 스위치가 언체크인지 확인
    private fun isAllSwitchUnchecked() : Boolean {
        if(!binding.setAlarmTimeSwitchMorning.isChecked &&
            !binding.setAlarmTimeSwitchAfternoon.isChecked &&
            !binding.setAlarmTimeSwitchEvening.isChecked &&
            !binding.setAlarmTimeSwitchNight.isChecked)
            return true
        return false
    }

    //하단 알림말 설정
    @SuppressLint("SetTextI18n")
    private fun makeExplainText()  {
        if(isAllSwitchUnchecked()) {
            binding.setAlarmTimeTextViewExplain.text = "시간을 설정하고 스위치를 켜주세요!"
            return
        }
        var explainText = ""
        if(binding.setAlarmTimeSwitchMorning.isChecked) explainText += "아침"
        if(binding.setAlarmTimeSwitchAfternoon.isChecked) explainText += " 점심"
        if(binding.setAlarmTimeSwitchEvening.isChecked) explainText += " 저녁"
        if(binding.setAlarmTimeSwitchNight.isChecked) explainText += " 취침전"
        binding.setAlarmTimeTextViewExplain.text = "${explainText}에 알려드릴게요!"
    }

    private fun init() {
        prevFragment = SetAlarmExpiredFragment()
        progressBar.progress = 80

        binding.setAlarmTimeBtnNext.setOnClickListener(this)
        binding.setAlarmTimeCardViewMorning.setOnClickListener(this)
        binding.setAlarmTimeCardViewAfternoon.setOnClickListener(this)
        binding.setAlarmTimeCardViewEvening.setOnClickListener(this)
        binding.setAlarmTimeCardViewNight.setOnClickListener(this)

        binding.setAlarmTimeSwitchMorning.setOnCheckedChangeListener(this)
        binding.setAlarmTimeSwitchAfternoon.setOnCheckedChangeListener(this)
        binding.setAlarmTimeSwitchEvening.setOnCheckedChangeListener(this)
        binding.setAlarmTimeSwitchNight.setOnCheckedChangeListener(this)
    }
}