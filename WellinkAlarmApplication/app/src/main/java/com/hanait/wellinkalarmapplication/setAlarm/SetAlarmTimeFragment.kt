package com.hanait.wellinkalarmapplication.setAlarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hanait.wellinkalarmapplication.home.HomeActivity
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants.prevFragment
import com.hanait.wellinkalarmapplication.utils.Constants.progressBar
import com.hanait.wellinkalarmapplication.databinding.FragmentSetAlarmTimeBinding

class SetAlarmTimeFragment : BaseFragment<FragmentSetAlarmTimeBinding>(FragmentSetAlarmTimeBinding::inflate), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prevFragment = SetAlarmExpiredFragment()
        progressBar.progress = 80

        binding.setAlarmTimeBtnNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.setAlarmTimeBtnNext -> {
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}