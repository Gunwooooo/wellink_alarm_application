package com.hanait.wellinkalarmapplication.setAlarm

import android.R
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmTimeBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.home.HomeActivity
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.mAlarmList
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData2
import java.text.SimpleDateFormat
import java.util.*

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

                insertOrUpdateAlarm()

                setAllAlarm()

                //임시 데이터 삭제
                tempAlarmData2 = null
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }
            //카드뷰 클릭 시 다이어로그 생성
            binding.setAlarmTimeCardViewMorning -> makeDialog(binding.setAlarmTimeTextViewMTime, 7, 0, binding.setAlarmTimeSwitchMorning)
            binding.setAlarmTimeCardViewAfternoon -> makeDialog(binding.setAlarmTimeTextViewATime, 12, 30, binding.setAlarmTimeSwitchAfternoon)
            binding.setAlarmTimeCardViewEvening -> makeDialog(binding.setAlarmTimeTextViewETime, 18, 30, binding.setAlarmTimeSwitchEvening)
            binding.setAlarmTimeCardViewNight -> makeDialog(binding.setAlarmTimeTextViewNTime, 22, 0, binding.setAlarmTimeSwitchNight)
        }
    }

    //스위치에 따른 알람 울리게 설정 및 취소 설정
    private fun setAllAlarm() {
        val pendingId = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectAlarmIdAsName(tempAlarmData.name)?.times(4) ?: return
        if(binding.setAlarmTimeSwitchMorning.isChecked)
            setAlarm(pendingId, tempAlarmData.mampm, tempAlarmData.mhour, tempAlarmData.mminute)
        else deleteAlarm(pendingId)
        if(binding.setAlarmTimeSwitchAfternoon.isChecked)
            setAlarm(pendingId + 1, tempAlarmData.aampm, tempAlarmData.ahour, tempAlarmData.aminute)
        else deleteAlarm(pendingId + 1)
        if(binding.setAlarmTimeSwitchEvening.isChecked)
            setAlarm(pendingId + 2, tempAlarmData.eampm, tempAlarmData.ehour, tempAlarmData.eminute)
        else deleteAlarm(pendingId + 2)
        if(binding.setAlarmTimeSwitchNight.isChecked)
            setAlarm(pendingId + 3, tempAlarmData.nampm, tempAlarmData.nhour, tempAlarmData.nminute)
        else deleteAlarm(pendingId + 3)
    }

    //약 이름 중복 체크
    private fun insertOrUpdateAlarm() {
        if(tempAlarmData2 == null) {
            DatabaseManager.getInstance(requireContext(), "Alarms.db")
                .insertAlarm(tempAlarmData)
            Toast.makeText(context, "약이 추가되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            DatabaseManager.getInstance(requireContext(), "Alarms.db")
                .updateAlarm(tempAlarmData, tempAlarmData2!!.name)
            Toast.makeText(context, "약이 수정되었습니다.", Toast.LENGTH_SHORT).show()
        }
        mAlarmList = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectAlarmAll()
        Log.d("로그", "HomeAlarmFragment - getAlarmList : 알람 갯수 : ${mAlarmList.size}")
    }

    //스위치 변화 이벤트 감지
    override fun onCheckedChanged(v: CompoundButton?, isChecked: Boolean) {
        makeExplainText()
    }

    //시간 설정 다이어로그 생성
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun makeDialog(v: TextView, cHour:Int, cMinute: Int, switch: Switch){
        Log.d("로그", "SetAlarmTimeFragment - getTime : 다이어로그 호출됨")
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
        val timePickerDialog = TimePickerDialog(context, R.style .Theme_Holo_Dialog_NoActionBar, timeSetListener, cHour, cMinute, false)
        timePickerDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        timePickerDialog.show()
    }

    //실제 알람 설정
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm(pendingId: Int, ampm:Int, hour: Int, minute: Int) {
        Log.d("로그", "SetAlarmTimeFragment - setAlarm : 설정 된 알람 아이디 : $pendingId")
        val myCalendar = Calendar.getInstance()
        val calendar = myCalendar.clone() as Calendar
        if(ampm == 1 && calendar.get(Calendar.HOUR_OF_DAY) != 12)
            calendar.set(Calendar.HOUR_OF_DAY, hour + 12)
        else calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        if(calendar <= myCalendar) {
            calendar.add(Calendar.DATE, 1)
        }
        Log.d(
            "로그",
            "setAlarm: 캘린더 시간 : " + calendar[Calendar.YEAR] + "-" + calendar[Calendar.MONTH] + "-" + calendar[Calendar.DAY_OF_MONTH] + "   " + calendar[Calendar.HOUR_OF_DAY] + ":" + calendar[Calendar.MINUTE] + ":" + calendar[Calendar.SECOND]
        )

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("intentType", ADD_INTENT)
        intent.putExtra("PendingId", pendingId)
        val alarmIntent = PendingIntent.getBroadcast(context, pendingId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context?.let { getSystemService(it, AlarmManager::class.java) }
        alarmManager?.cancel(alarmIntent)
        alarmManager?.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, alarmIntent)
    }
    //알람 취소
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun deleteAlarm(pendingId: Int) {
        val alarmManager = context?.let { getSystemService(it, AlarmManager::class.java) }
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, pendingId, intent, 0)
        alarmManager?.cancel(alarmIntent)
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
        if(binding.setAlarmTimeSwitchNight.isChecked) explainText += " 취침전"
        binding.setAlarmTimeTextViewExplain.text = "${explainText}에 알려드릴게요!"
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
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