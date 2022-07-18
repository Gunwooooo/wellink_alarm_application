package com.hanait.wellinkalarmapplication.setAlarm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmExpiredBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.home.HomeActivity
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData2
import java.util.*


class SetAlarmExpiredFragment : BaseFragment<FragmentSetAlarmExpiredBinding>(FragmentSetAlarmExpiredBinding::inflate), View.OnClickListener {

    var numberPickerValue = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmExpiredBtnNext -> {

                tempAlarmData.expired = getExpiredDate()
                tempAlarmData.expiredInt = numberPickerValue

                //DB에 알람 정보 최종 저장하기
                insertOrUpdateAlarm()

                //임시 데이터 삭제
                tempAlarmData2 = null
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }

            binding.setAlarmExpiredBtnSecond -> {
                numberPickerValue = 2
                binding.setAlarmExpiredNumberPicker.value = 2

                binding.setAlarmExpiredTextViewExplain.text = "${tempAlarmData.period}일마다 2회 알림을 설정할게요!" +
                        "\n만기일은 ${getExpiredDate()} 이에요."

                binding.setAlarmExpiredSpaceLayout.visibility = View.GONE
                binding.setAlarmExpiredNumberPickerLayout.visibility = View.VISIBLE
                binding.setAlarmExpiredBtnSecond.setBackgroundResource(R.drawable.btn_border_blue)
                binding.setAlarmExpiredBtnSecond.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.setAlarmExpiredBtnFirst.setBackgroundResource(R.drawable.btn_border_gray)
                binding.setAlarmExpiredBtnFirst.setTextColor(ContextCompat.getColor(requireContext(), R.color.toss_black_200))
            }
            binding.setAlarmExpiredBtnFirst -> {
                numberPickerValue  = 0

                binding.setAlarmExpiredSpaceLayout.visibility = View.VISIBLE
                binding.setAlarmExpiredNumberPickerLayout.visibility = View.GONE
                binding.setAlarmExpiredBtnFirst.setBackgroundResource(R.drawable.btn_border_blue)
                binding.setAlarmExpiredBtnFirst.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.setAlarmExpiredBtnSecond.setBackgroundResource(R.drawable.btn_border_gray)
                binding.setAlarmExpiredBtnSecond.setTextColor(ContextCompat.getColor(requireContext(), R.color.toss_black_200))

                binding.setAlarmExpiredTextViewExplain.text = "만기일 없이 알림을 울려드릴게요!"
            }
        }
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
        Log.d("로그", "만기 날짜 : $expiredDate")
        return expiredDate
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        prevFragment = SetAlarmTimeFragment()
        progressBar.progress = 80

        binding.setAlarmExpiredTextViewQuestion.text = "${tempAlarmData.name}을(를)\n며칠 동안 드시나요?"
        numberPickerValue = 0

        binding.setAlarmExpiredBtnNext.setOnClickListener(this)
        binding.setAlarmExpiredBtnFirst.setOnClickListener(this)
        binding.setAlarmExpiredBtnSecond.setOnClickListener(this)

        //넘버 픽커 설정
        binding.setAlarmExpiredNumberPicker.maxValue = 100
        binding.setAlarmExpiredNumberPicker.minValue = 2
        binding.setAlarmExpiredNumberPicker.setOnValueChangedListener { _, _, newVal ->
            numberPickerValue = newVal
            binding.setAlarmExpiredTextViewExplain.text = "${tempAlarmData.period}일마다 ${newVal}회 알림을 울려드릴게요!" +
                    "\n만기일은 ${getExpiredDate()} 입니다."

        }

        //알람 수정 시 기존 값 넣어놓기
        if(tempAlarmData2 != null && tempAlarmData2!!.expiredInt != 0) {
            binding.setAlarmExpiredBtnSecond.performClick()
            numberPickerValue = tempAlarmData2!!.expiredInt
            binding.setAlarmExpiredNumberPicker.value = tempAlarmData2!!.expiredInt
            binding.setAlarmExpiredTextViewExplain.text = "${tempAlarmData.period}일마다 ${tempAlarmData2!!.expiredInt}회 알림을 울려드릴게요!" +
                    "\n만기일은 ${getExpiredDate()} 입니다."
        }
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
        Constants.mAlarmList = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectAlarmAll()
        Log.d("로그", "HomeAlarmFragment - getAlarmList : 알람 갯수 : ${Constants.mAlarmList.size}")
    }
}