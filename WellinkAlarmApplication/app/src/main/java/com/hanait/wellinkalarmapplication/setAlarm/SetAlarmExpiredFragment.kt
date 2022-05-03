package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.view.View
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmExpiredBinding


class SetAlarmExpiredFragment : BaseFragment<FragmentSetAlarmExpiredBinding>(FragmentSetAlarmExpiredBinding::inflate), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prevFragment = SetAlarmPeriodFragment()
        progressBar.progress = 60

        binding.setAlarmExpiredBtnNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmExpiredBtnNext -> {
                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmTimeFragment")
            }
        }
    }
}