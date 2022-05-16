package com.hanait.wellinkalarmapplication.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeAlarmBinding
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.utils.BaseFragment


class HomeAlarmFragment : BaseFragment<FragmentHomeAlarmBinding>(FragmentHomeAlarmBinding::inflate), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeMedicineAddMedicine.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent(context, SetAlarmActivity::class.java)
        startActivity(intent)
    }
}