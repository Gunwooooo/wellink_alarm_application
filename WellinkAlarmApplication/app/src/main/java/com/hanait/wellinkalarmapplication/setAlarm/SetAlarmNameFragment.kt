package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.util.Log
import android.view.View
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmNameBinding

class SetAlarmNameFragment : BaseFragment<FragmentSetAlarmNameBinding>(FragmentSetAlarmNameBinding::inflate), View.OnClickListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prevFragment = SetAlarmNameFragment()
        progressBar.progress = 20

        val mActivity = activity as SetAlarmActivity
        mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.setAlarmNameBtnNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmNameBtnNext -> {
                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmPeriodFragment")
            }
        }
    }
}