package com.hanait.wellinkalarmapplication.setAlarm

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmExpiredBinding
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData2
import java.util.*


class SetAlarmExpiredFragment : BaseFragment<FragmentSetAlarmExpiredBinding>(FragmentSetAlarmExpiredBinding::inflate), View.OnClickListener {

    var alarmDataNumber = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmExpiredBtnNext -> {

                tempAlarmData.expired = getExpiredDate()
                tempAlarmData.expiredInt = alarmDataNumber

                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmTimeFragment")
            }

            binding.setAlarmExpiredBtnSecond -> {
                alarmDataNumber = 2
                binding.setAlarmExpiredNumberPicker.value = 2

                binding.setAlarmExpiredTextViewExplain.text = "2회분으로 알림을 설정할게요!"

                binding.setAlarmExpiredSpaceLayout.visibility = View.GONE
                binding.setAlarmExpiredNumberPickerLayout.visibility = View.VISIBLE
                binding.setAlarmExpiredBtnSecond.setBackgroundResource(R.drawable.btn_border_blue)
                binding.setAlarmExpiredBtnSecond.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.setAlarmExpiredBtnFirst.setBackgroundResource(R.drawable.btn_border_gray)
                binding.setAlarmExpiredBtnFirst.setTextColor(ContextCompat.getColor(requireContext(), R.color.toss_black_200))
            }
            binding.setAlarmExpiredBtnFirst -> {
                alarmDataNumber  = 0

                binding.setAlarmExpiredSpaceLayout.visibility = View.VISIBLE
                binding.setAlarmExpiredNumberPickerLayout.visibility = View.GONE
                binding.setAlarmExpiredBtnFirst.setBackgroundResource(R.drawable.btn_border_blue)
                binding.setAlarmExpiredBtnFirst.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.setAlarmExpiredBtnSecond.setBackgroundResource(R.drawable.btn_border_gray)
                binding.setAlarmExpiredBtnSecond.setTextColor(ContextCompat.getColor(requireContext(), R.color.toss_black_200))

                binding.setAlarmExpiredTextViewExplain.text = "기간에 제한없이 알림을 울려드릴게요!"
            }
        }
    }

    //만기날짜 가져오기
    private fun getExpiredDate(): String {
        if(alarmDataNumber == 0) {
            return ""
        }
        val cal = Calendar.getInstance()
        var expiredDate = ""
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DAY_OF_MONTH, alarmDataNumber)
        expiredDate = cal.time.let { Constants.sdf.format(it) }
        Log.d("로그", "오늘 날짜 : $expiredDate")
        return expiredDate
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        prevFragment = SetAlarmPeriodFragment()
        progressBar.progress = 60
        binding.setAlarmExpiredTextViewQuestion.text = "${tempAlarmData.name}의\n회분이 정해져 있나요?"
        alarmDataNumber = 0

        binding.setAlarmExpiredBtnNext.setOnClickListener(this)
        binding.setAlarmExpiredBtnFirst.setOnClickListener(this)
        binding.setAlarmExpiredBtnSecond.setOnClickListener(this)

        //넘버 픽커 설정
        binding.setAlarmExpiredNumberPicker.maxValue = 100
        binding.setAlarmExpiredNumberPicker.minValue = 2
        binding.setAlarmExpiredNumberPicker.setOnValueChangedListener { _, _, newVal ->
            binding.setAlarmExpiredTextViewExplain.text = "${newVal}회분으로 알림을 설정할게요!"
            alarmDataNumber = newVal
        }

        //알람 수정 시 기존 값 넣어놓기
        if(tempAlarmData2 != null && tempAlarmData2!!.expiredInt != 0) {
            binding.setAlarmExpiredBtnSecond.performClick()
            alarmDataNumber = tempAlarmData2!!.expiredInt
            binding.setAlarmExpiredNumberPicker.value = tempAlarmData2!!.expiredInt
            binding.setAlarmExpiredTextViewExplain.text = "${tempAlarmData2!!.expiredInt}회분으로 알림을 설정할게요!"
        }
    }
}