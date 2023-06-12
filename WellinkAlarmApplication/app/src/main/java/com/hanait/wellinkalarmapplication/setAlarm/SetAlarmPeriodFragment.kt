package com.hanait.wellinkalarmapplication.setAlarm

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmPeriodBinding
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData2


class SetAlarmPeriodFragment : BaseFragment<FragmentSetAlarmPeriodBinding>(FragmentSetAlarmPeriodBinding::inflate), View.OnClickListener {

    var numberPickerValue = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmPeriodBtnNext -> {
                tempAlarmData.period = numberPickerValue

                //다음 프래그먼트 이동
                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmTimeFragment")

            }
            binding.setAlarmPeriodBtnSecond -> {
                numberPickerValue = 2
                binding.setAlarmPeriodNumberPicker.value = 2

                binding.setAlarmPeriodTextViewExplain.text = "2일마다 알림을 울릴게요!"

                binding.setAlarmPeriodNumberPickerLayout.visibility = View.VISIBLE

                //버튼 활성화 변경
                changeButtonColor(binding.setAlarmPeriodBtnSecond, binding.setAlarmPeriodBtnFirst)
            }
            binding.setAlarmPeriodBtnFirst -> {
                numberPickerValue = 1

                binding.setAlarmPeriodTextViewExplain.text = "매일 알림을 울릴게요!"

                binding.setAlarmPeriodNumberPickerLayout.visibility = View.GONE

                //버튼 활성화 변경
                changeButtonColor(binding.setAlarmPeriodBtnFirst, binding.setAlarmPeriodBtnSecond)

            }
        }
    }

    //버튼 토글 기능
    private fun changeButtonColor(onButton: Button, offButton: Button) {
        onButton.isEnabled = false
        offButton.isEnabled = true
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        prevFragment = SetAlarmNameFragment()
        progressBar.progress = 40

        numberPickerValue = 1

        //액션바 다시 보이게하기(뒤로가기)
        val mActivity = activity as SetAlarmActivity
        mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.setAlarmPeriodTextViewQuestion.text = "${tempAlarmData.name}을(를) 주기적으로 드세요?"

        binding.setAlarmPeriodBtnNext.setOnClickListener(this)
        binding.setAlarmPeriodBtnFirst.setOnClickListener(this)
        binding.setAlarmPeriodBtnSecond.setOnClickListener(this)

        //넘버 픽커 설정
        binding.setAlarmPeriodNumberPicker.maxValue = 30
        binding.setAlarmPeriodNumberPicker.minValue = 2
        binding.setAlarmPeriodNumberPicker.setOnValueChangedListener { _, _, newVal ->
            binding.setAlarmPeriodTextViewExplain.text = "${newVal}일마다 알림을 울릴게요!"
            numberPickerValue = newVal
        }

        //알람 수정 시 기존 값 넣어놓기
        if(tempAlarmData2 != null && tempAlarmData2!!.period != 1) {
            binding.setAlarmPeriodBtnSecond.performClick()
            numberPickerValue = tempAlarmData2!!.period
            binding.setAlarmPeriodNumberPicker.value = tempAlarmData2!!.period
            binding.setAlarmPeriodTextViewExplain.text = "${tempAlarmData2!!.period}일마다 알림을 울릴게요!"
        }
    }

}