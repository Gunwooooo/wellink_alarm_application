package com.hanait.wellinkalarmapplication.setAlarm

import android.os.Bundle
import android.util.Log
import android.view.View
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetUserNameBinding

class SetUserNameFragment : BaseFragment<FragmentSetUserNameBinding>(FragmentSetUserNameBinding::inflate), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prevFragment = SetUserNameFragment()
        progressBar.progress = 0

        binding.setUserNameBtnNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setUserNameBtnNext -> {
                val mActivity = activity as SetAlarmActivity
                mActivity.changeFragment("SetAlarmNameFragment")
            }
        }
    }
}