package com.hanait.wellinkalarmapplication.setAlarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmExpiredBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.home.HomeActivity
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.isMediaOn
import com.hanait.wellinkalarmapplication.utils.Constants.isVibrationOn
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData2
import com.hanait.wellinkalarmapplication.utils.CustomAlarmManager
import java.util.*


class SetAlarmExpiredFragment : BaseFragment<FragmentSetAlarmExpiredBinding>(FragmentSetAlarmExpiredBinding::inflate), View.OnClickListener {
    var numberPickerValue = 0
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v) {
            //다음 버튼 클릭
            binding.setAlarmExpiredBtnNext -> {
                //전역 변수에 값 넣기
                tempAlarmData.expired = getExpiredDate()
                tempAlarmData.expiredInt = numberPickerValue

                //DB에 알람 정보 최종 저장하기
                insertOrUpdateAlarm()

                //저장된 알람 ID 가져와서 pendingId로 사용
                setAllAlarmManager()

                //임시 데이터 삭제
                tempAlarmData2 = null

                //홈 액티비티로 전환
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
            }

            //만기일 설정 버튼
            binding.setAlarmExpiredBtnSecond -> {
                numberPickerValue = 2
                binding.setAlarmExpiredNumberPicker.value = 2

                binding.setAlarmExpiredTextViewExplain.text = "${tempAlarmData.period}일마다 2회 알림을 울릴게요!" +
                        "\n만기일은 ${getExpiredDate()} 이에요."

                binding.setAlarmExpiredNumberPickerLayout.visibility = View.VISIBLE

                //버튼 활성화 변경
                changeButtonColor(binding.setAlarmExpiredBtnSecond, binding.setAlarmExpiredBtnFirst)
            }
            //만기일 없이 설정 버튼
            binding.setAlarmExpiredBtnFirst -> {
                numberPickerValue  = 0

                binding.setAlarmExpiredTextViewExplain.text = "만기일 없이 알림을 울릴게요!"

                binding.setAlarmExpiredNumberPickerLayout.visibility = View.GONE

                //버튼 활성화 변경
                changeButtonColor(binding.setAlarmExpiredBtnFirst, binding.setAlarmExpiredBtnSecond)

            }
        }
    }

    //버튼 토글 기능
    private fun changeButtonColor(onButton: Button, offButton: Button) {
        onButton.isEnabled = false
        offButton.isEnabled = true
    }

    //만기날짜 가져오기
    private fun getExpiredDate(): String {
        if(numberPickerValue == 0) {
            return ""
        }
        val cal = Calendar.getInstance()
        var expiredDate = ""
        cal.add(Calendar.DAY_OF_MONTH, (numberPickerValue - 1) * tempAlarmData.period)
        expiredDate = cal.time.let { Constants.sdf.format(it) }
        return expiredDate
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        prevFragment = SetAlarmTimeFragment()
        progressBar.progress = 80

        binding.setAlarmExpiredTextViewQuestion.text = "${tempAlarmData.name}을(를) 며칠 동안 드시나요?"
        numberPickerValue = 0

        binding.setAlarmExpiredBtnNext.setOnClickListener(this)
        binding.setAlarmExpiredBtnFirst.setOnClickListener(this)
        binding.setAlarmExpiredBtnSecond.setOnClickListener(this)

        //넘버 픽커 설정
        binding.setAlarmExpiredNumberPicker.maxValue = 100
        binding.setAlarmExpiredNumberPicker.minValue = 2
        binding.setAlarmExpiredNumberPicker.setOnValueChangedListener { _, _, newVal ->
            numberPickerValue = newVal
            binding.setAlarmExpiredTextViewExplain.text = "${tempAlarmData.period}일마다 ${newVal}회 알림을 울릴게요!" +
                    "\n만기일은 ${getExpiredDate()} 입니다."
        }

        //알람 수정 시 기존 값 넣어놓기
        if(tempAlarmData2 != null && tempAlarmData2!!.expiredInt != 0) {
            binding.setAlarmExpiredBtnSecond.performClick()
            numberPickerValue = tempAlarmData2!!.expiredInt
            binding.setAlarmExpiredNumberPicker.value = tempAlarmData2!!.expiredInt
            binding.setAlarmExpiredTextViewExplain.text = "${tempAlarmData.period}일마다 ${tempAlarmData2!!.expiredInt}회 알림을 울릴게요!" +
                    "\n만기일은 ${getExpiredDate()} 입니다."
        }
    }

    //DB에서 약 이름 중복 체크
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
        Constants.mAlarmList = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectAlarmAll()
    }



    // 스위치에 따른 알람 울리게 설정 및 취소 설정
    @RequiresApi(Build.VERSION_CODES.S)
    private fun setAllAlarmManager() {
        val pendingId = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectAlarmIdAsName(tempAlarmData.name)?.times(4) ?: return
        if(tempAlarmData.mswitch == 1)
            CustomAlarmManager.getInstance(context).setAlarmManager(pendingId, tempAlarmData.mampm, tempAlarmData.mhour, tempAlarmData.mminute, isMediaOn, isVibrationOn
            )
        else deleteAlarmManager(pendingId)
        if(tempAlarmData.aswitch == 1)
            CustomAlarmManager.getInstance(context).setAlarmManager(pendingId + 1, tempAlarmData.aampm, tempAlarmData.ahour, tempAlarmData.aminute, isMediaOn, isVibrationOn)
        else deleteAlarmManager(pendingId + 1)
        if(tempAlarmData.eswitch == 1)
            CustomAlarmManager.getInstance(context).setAlarmManager(pendingId + 2, tempAlarmData.eampm, tempAlarmData.ehour, tempAlarmData.eminute, isMediaOn, isVibrationOn)
        else deleteAlarmManager(pendingId + 2)
        if(tempAlarmData.nswitch == 1)
            CustomAlarmManager.getInstance(context).setAlarmManager(pendingId + 3, tempAlarmData.nampm, tempAlarmData.nhour, tempAlarmData.nminute, isMediaOn, isVibrationOn)
        else deleteAlarmManager(pendingId + 3)
    }

    //알람 취소
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun deleteAlarmManager(pendingId: Int) {
        val alarmManager = context?.let {
            ContextCompat.getSystemService(
                it,
                AlarmManager::class.java
            )
        }
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, pendingId, intent, 0 or PendingIntent.FLAG_MUTABLE)
        alarmManager?.cancel(alarmIntent)
    }
}