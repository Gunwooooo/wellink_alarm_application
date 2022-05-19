package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmExpiredBinding
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData


class SetAlarmExpiredFragment : BaseFragment<FragmentSetAlarmExpiredBinding>(FragmentSetAlarmExpiredBinding::inflate), View.OnClickListener {

    var alarmDataNumber = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmExpiredBtnNext -> {
                tempAlarmData.expired = alarmDataNumber

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
    }
}