package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.util.Log
import android.view.View
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmPeriodBinding


class SetAlarmPeriodFragment : BaseFragment<FragmentSetAlarmPeriodBinding>(FragmentSetAlarmPeriodBinding::inflate), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prevFragment = SetAlarmNameFragment()
        progressBar.progress = 40

        binding.setAlarmPeriodBtnNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmPeriodBtnNext -> {
                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmExpiredFragment")
            }
        }
    }

}