package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.hanait.wellinkalarmapplication.beans.AlarmData
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmNameBinding
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData

class SetAlarmNameFragment : BaseFragment<FragmentSetAlarmNameBinding>(FragmentSetAlarmNameBinding::inflate), View.OnClickListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        //임시 전역 변수 선언
        tempAlarmData = AlarmData()
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmNameBtnNext -> {
                //이름 저장하기
                if(binding.setAlarmNameEditTextAlarmName.text.toString() == "") {
                    Toast.makeText(context, "약 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                    return
                }
                tempAlarmData.name = binding.setAlarmNameEditTextAlarmName.text.toString()
                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmPeriodFragment")
            }
        }
    }

    private fun init() {
        prevFragment = SetAlarmNameFragment()
        progressBar.progress = 20

        //액션바 안보이게 하기(뒤로가기)
        val mActivity = activity as SetAlarmActivity
        mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.setAlarmNameBtnNext.setOnClickListener(this)
    }
}