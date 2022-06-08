package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmNameBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData2

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
                //약 이름 중복 체크
                val mAlarmList = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectAlarmAll()
                if(tempAlarmData2 == null) {
                    for (i in 0 until mAlarmList.size) {
                        if (mAlarmList[i].name == binding.setAlarmNameEditTextAlarmName.text.toString()) {
                            binding.setAlarmNameEditTextAlarmName.setText("")
                            Toast.makeText(context, "이미 등록된 약입니다.", Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
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

        //알람 수정 시 기존 값 넣어놓기
        if(tempAlarmData2 != null) {
            binding.setAlarmNameEditTextAlarmName.setText(tempAlarmData2!!.name)
        }
    }
}