package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmNameBinding
import com.hanait.wellinkalarmapplication.utils.AlarmData

class SetAlarmNameFragment : BaseFragment<FragmentSetAlarmNameBinding>(FragmentSetAlarmNameBinding::inflate), View.OnClickListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prevFragment = SetAlarmNameFragment()
        progressBar.progress = 20
        AlarmData.name = ""
        binding.setAlarmNameEditTextAlarmName.setText(AlarmData.name)


        val mActivity = activity as SetAlarmActivity
        mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.setAlarmNameBtnNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmNameBtnNext -> {
                //이름 저장하기
                if(binding.setAlarmNameEditTextAlarmName.text.toString() == "") {
                    Toast.makeText(context, "약 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                    return
                }
                AlarmData.name = binding.setAlarmNameEditTextAlarmName.text.toString()
                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmPeriodFragment")
            }
        }
    }
}