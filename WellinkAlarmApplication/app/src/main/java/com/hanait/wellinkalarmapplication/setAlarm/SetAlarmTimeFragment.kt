package com.hanait.wellinkalarmapplication.setAlarm

import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.hanait.wellinkalarmapplication.home.HomeActivity
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmTimeBinding
import com.hanait.wellinkalarmapplication.utils.AlarmData
import java.text.SimpleDateFormat
import java.util.*

class SetAlarmTimeFragment : BaseFragment<FragmentSetAlarmTimeBinding>(FragmentSetAlarmTimeBinding::inflate), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prevFragment = SetAlarmExpiredFragment()
        progressBar.progress = 80

        init()

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

    private fun init() {
        AlarmData.mswitch = 0
        AlarmData.aswitch = 0
        AlarmData.eswitch = 0
        AlarmData.nswitch = 0
    }


    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmTimeBtnNext -> {
                Log.d("로그", "SetAlarmTimeFragment - onClick : name : ${AlarmData.name}")
                Log.d("로그", "SetAlarmTimeFragment - onClick : period : ${AlarmData.period}")
                Log.d("로그", "SetAlarmTimeFragment - onClick : expired : ${AlarmData.expired}")

                Log.d("로그", "SetAlarmTimeFragment - onClick : mampm : ${AlarmData.mampm}  ${AlarmData.mhour} : ${AlarmData.mminute}  switch : ${AlarmData.mswitch}")
                Log.d("로그", "SetAlarmTimeFragment - onClick : aampm : ${AlarmData.aampm}  ${AlarmData.ahour} : ${AlarmData.aminute}  switch : ${AlarmData.aswitch}")
                Log.d("로그", "SetAlarmTimeFragment - onClick : eampm : ${AlarmData.eampm}  ${AlarmData.ehour} : ${AlarmData.eminute}  switch : ${AlarmData.eswitch}")
                Log.d("로그", "SetAlarmTimeFragment - onClick : nampm : ${AlarmData.nampm}  ${AlarmData.nhour} : ${AlarmData.nminute}  switch : ${AlarmData.nswitch}")

                AlarmData.resetAlarmData()

                Toast.makeText(context, "약이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }
            binding.setAlarmTimeCardViewMorning -> {
                makeDialog(binding.setAlarmTimeTextViewMTime, 7, 0)
            }
            binding.setAlarmTimeCardViewAfternoon -> {
                makeDialog(binding.setAlarmTimeTextViewATime, 12, 30)
            }
            binding.setAlarmTimeCardViewEvening -> {
                makeDialog(binding.setAlarmTimeTextViewETime, 18, 30)
            }
            binding.setAlarmTimeCardViewNight -> {
                makeDialog(binding.setAlarmTimeTextViewNTime, 20, 0)
            }
        }
    }

    override fun onCheckedChanged(v: CompoundButton?, isChecked: Boolean) {
        var tmpChecked = 0
        var tmpText = ""
        if(isChecked)
            tmpChecked = 1
        Log.d("로그", "SetAlarmTimeFragment - onCheckedChanged : $tmpChecked")
        when(v) {
            binding.setAlarmTimeSwitchMorning -> {
                AlarmData.mswitch = tmpChecked
                if(tmpChecked == 1) tmpText = "아침 "
            }
            binding.setAlarmTimeSwitchAfternoon -> {
                AlarmData.aswitch = tmpChecked
            }
            binding.setAlarmTimeSwitchEvening -> {
                AlarmData.eswitch = tmpChecked
            }
            binding.setAlarmTimeSwitchNight -> {
                AlarmData.nswitch = tmpChecked
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun makeDialog(v: TextView, cHour:Int, cMinute: Int){
        Log.d("로그", "SetAlarmTimeFragment - getTime : 다이어로그 호출됨")
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            Log.d("로그", "$hour : $minute")
            var tmpHour = hour
            var ampm = "오전"
            if(hour >= 12) {
                if(hour >= 13) tmpHour -= 12
                ampm = "오후"
            }
            cal.set(Calendar.HOUR_OF_DAY, tmpHour)
            cal.set(Calendar.MINUTE, minute)
            v.text = "$ampm ${SimpleDateFormat("H:mm").format(cal.time)}"

        }
        val timePickerDialog = TimePickerDialog(context, android.R.style.Theme_Holo_Dialog_NoActionBar, timeSetListener, cHour, cMinute, false)
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }
}