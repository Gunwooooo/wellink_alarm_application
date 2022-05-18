package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmPeriodBinding


class SetAlarmPeriodFragment : BaseFragment<FragmentSetAlarmPeriodBinding>(FragmentSetAlarmPeriodBinding::inflate), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prevFragment = SetAlarmNameFragment()
        progressBar.progress = 40

        val mActivity = activity as SetAlarmActivity
        mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.setAlarmPeriodBtnNext.setOnClickListener(this)
        binding.setAlarmPeriodBtnFirst.setOnClickListener(this)
        binding.setAlarmPeriodBtnSecond.setOnClickListener(this)

        binding.setAlarmPeriodNumberPicker.maxValue = 30
        binding.setAlarmPeriodNumberPicker.minValue = 2
        binding.setAlarmPeriodNumberPicker.setOnValueChangedListener { p0, oldVal, newVal ->
            binding.setAlarmPeriodTextViewExplain.text = "${newVal}일마다 알림을 울려드릴게요!"
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmPeriodBtnNext -> {
                //다음 프래그먼트 이동
                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmExpiredFragment")
            }
            binding.setAlarmPeriodBtnSecond -> {
                binding.setAlarmPeriodTextViewExplain.text = "2일마다 알림을 울려드릴게요!"
                
                binding.setAlarmPeriodSpaceLayout.visibility = View.GONE
                binding.setAlarmPeriodNumberPickerLayout.visibility = View.VISIBLE
                binding.setAlarmPeriodBtnSecond.setBackgroundResource(R.drawable.btn_border_blue)
                binding.setAlarmPeriodBtnSecond.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.setAlarmPeriodBtnFirst.setBackgroundResource(R.drawable.btn_border_gray)
                binding.setAlarmPeriodBtnFirst.setTextColor(ContextCompat.getColor(requireContext(), R.color.toss_black_200))
            }
            binding.setAlarmPeriodBtnFirst -> {
                binding.setAlarmPeriodNumberPicker.value = 2

                binding.setAlarmPeriodSpaceLayout.visibility = View.VISIBLE
                binding.setAlarmPeriodNumberPickerLayout.visibility = View.GONE
                binding.setAlarmPeriodBtnFirst.setBackgroundResource(R.drawable.btn_border_blue)
                binding.setAlarmPeriodBtnFirst.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.setAlarmPeriodBtnSecond.setBackgroundResource(R.drawable.btn_border_gray)
                binding.setAlarmPeriodBtnSecond.setTextColor(ContextCompat.getColor(requireContext(), R.color.toss_black_200))

                binding.setAlarmPeriodTextViewExplain.text = "매일마다 알림을 울려드릴게요!"

            }
        }
    }



}