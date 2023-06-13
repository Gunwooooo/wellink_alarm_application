package com.hanait.wellinkalarmapplication.setAlarm

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmTimeBinding
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData2
import java.text.SimpleDateFormat
import java.util.*

class SetAlarmTimeFragment : BaseFragment<FragmentSetAlarmTimeBinding>(FragmentSetAlarmTimeBinding::inflate), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private val glide by lazy { Glide.with(this) }

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
                //스위치 정보 전역변수 저장
                saveSwitchData()

                //만기일 설정으로 이동
                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmExpiredFragment")
            }
            //카드뷰 클릭 시 다이어로그 생성
            binding.setAlarmTimeCardViewMorning -> makeDialog(binding.setAlarmTimeTextViewMTime, 7, 0, binding.setAlarmTimeSwitchMorning)
            binding.setAlarmTimeCardViewAfternoon -> makeDialog(binding.setAlarmTimeTextViewATime, 12, 30, binding.setAlarmTimeSwitchAfternoon)
            binding.setAlarmTimeCardViewEvening -> makeDialog(binding.setAlarmTimeTextViewETime, 18, 30, binding.setAlarmTimeSwitchEvening)
            binding.setAlarmTimeCardViewNight -> makeDialog(binding.setAlarmTimeTextViewNTime, 22, 0, binding.setAlarmTimeSwitchNight)
        }
    }

    //스위치 변화 이벤트 감지
    override fun onCheckedChanged(v: CompoundButton?, isChecked: Boolean) {
        makeExplainText()
    }

    //시간 설정 다이어로그 생성
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun makeDialog(v: TextView, cHour:Int, cMinute: Int, switch: Switch){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
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
            switch.isChecked = true
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
    //아침 점심 저녁 취침전
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
        if(binding.setAlarmTimeSwitchNight.isChecked) explainText += " 취침 전"
        binding.setAlarmTimeTextViewExplain.text = "${explainText}에 알려드릴게요!"
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun init() {
        prevFragment = SetAlarmPeriodFragment()
        progressBar.progress = 60

        //이미지 넣기
        glide.load(R.drawable.baseline_access_alarm_24).into(binding.setAlarmTimeImageViewMorning)
        glide.load(R.drawable.baseline_wb_sunny_24).into(binding.setAlarmTimeImageViewAfternoon)
        glide.load(R.drawable.baseline_cloud_24).into(binding.setAlarmTimeImageViewEvening)
        glide.load(R.drawable.baseline_night_shelter_24).into(binding.setAlarmTimeImageViewNight)

        binding.setAlarmTimeBtnNext.setOnClickListener(this)
        binding.setAlarmTimeCardViewMorning.setOnClickListener(this)
        binding.setAlarmTimeCardViewAfternoon.setOnClickListener(this)
        binding.setAlarmTimeCardViewEvening.setOnClickListener(this)
        binding.setAlarmTimeCardViewNight.setOnClickListener(this)

        binding.setAlarmTimeSwitchMorning.setOnCheckedChangeListener(this)
        binding.setAlarmTimeSwitchAfternoon.setOnCheckedChangeListener(this)
        binding.setAlarmTimeSwitchEvening.setOnCheckedChangeListener(this)
        binding.setAlarmTimeSwitchNight.setOnCheckedChangeListener(this)

        //알람 수정 시 기존 값 넣어놓기
        if(tempAlarmData2 != null) {
            var tmpAmpm = "오전"
            if (tempAlarmData2!!.mampm == 1)
                tmpAmpm = "오후"
            var cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, tempAlarmData2!!.mhour)
            cal.set(Calendar.MINUTE, tempAlarmData2!!.mminute)
            binding.setAlarmTimeTextViewMTime.text =
                "$tmpAmpm ${SimpleDateFormat("H:mm").format(cal.time)}"
            saveTimeData(binding.setAlarmTimeTextViewMTime, tmpAmpm, tempAlarmData2!!.mhour, tempAlarmData2!!.mminute)

            tmpAmpm = "오전"
            if (tempAlarmData2!!.aampm == 1)
                tmpAmpm = "오후"
            cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, tempAlarmData2!!.ahour)
            cal.set(Calendar.MINUTE, tempAlarmData2!!.aminute)
            binding.setAlarmTimeTextViewATime.text =
                "$tmpAmpm ${SimpleDateFormat("H:mm").format(cal.time)}"
            saveTimeData(binding.setAlarmTimeTextViewATime, tmpAmpm, tempAlarmData2!!.ahour, tempAlarmData2!!.aminute)

            tmpAmpm = "오전"
            if (tempAlarmData2!!.eampm == 1)
                tmpAmpm = "오후"
            cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, tempAlarmData2!!.ehour)
            cal.set(Calendar.MINUTE, tempAlarmData2!!.eminute)
            binding.setAlarmTimeTextViewETime.text =
                "$tmpAmpm ${SimpleDateFormat("H:mm").format(cal.time)}"
            saveTimeData(binding.setAlarmTimeTextViewETime, tmpAmpm, tempAlarmData2!!.ehour, tempAlarmData2!!.eminute)

            tmpAmpm = "오전"
            if (tempAlarmData2!!.nampm == 1)
                tmpAmpm = "오후"
            cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, tempAlarmData2!!.nhour)
            cal.set(Calendar.MINUTE, tempAlarmData2!!.nminute)
            binding.setAlarmTimeTextViewNTime.text =
                "$tmpAmpm ${SimpleDateFormat("H:mm").format(cal.time)}"
            saveTimeData(binding.setAlarmTimeTextViewNTime, tmpAmpm, tempAlarmData2!!.nhour, tempAlarmData2!!.nminute)
//            binding.setAlarmExpiredTextViewExplain.text = "${tempAlarmData2!!.expiredInt}회분으로 알림을 설정할게요!"

            if(tempAlarmData2!!.mswitch == 1) binding.setAlarmTimeSwitchMorning.isChecked = true
            if(tempAlarmData2!!.aswitch == 1) binding.setAlarmTimeSwitchAfternoon.isChecked = true
            if(tempAlarmData2!!.eswitch == 1) binding.setAlarmTimeSwitchEvening.isChecked = true
            if(tempAlarmData2!!.nswitch == 1) binding.setAlarmTimeSwitchNight.isChecked = true
        }
    }
}