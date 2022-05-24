package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.beans.AlarmData
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeAlarmBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.utils.BaseFragment


class HomeAlarmFragment : BaseFragment<FragmentHomeAlarmBinding>(FragmentHomeAlarmBinding::inflate), View.OnClickListener {
    private var mAlarmList: ArrayList<AlarmData> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeAlarmAddAlarm.setOnClickListener(this)

        getAlarmList()

        recyclerViewCreate()
    }

    @SuppressLint("SetTextI18n")
    private fun getAlarmList() {
        mAlarmList = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectAll()
        Log.d("로그", "HomeAlarmFragment - getAlarmList : 알람 갯수 : ${mAlarmList.size}")
        binding.homeAlarmTextViewAlarmCount.text = "${mAlarmList.size}개"
        if(mAlarmList.size != 0) {
            binding.homeAlarmTextViewExplain.text = "아래 약들의 알람을 울려드려요"
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.homeAlarmAddAlarm -> {
                val intent = Intent(context, SetAlarmActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun recyclerViewCreate() {
        val alarmView = binding.homeAlarmRecyclerView
        val alarmAdapter =
            context?.let { AlarmAdapter(it, mAlarmList, HomeAlarmFragment()) }

        alarmAdapter?.setOnItemClickListener(
            object : AlarmAdapter.OnItemClickListener {
                override fun onItemClick(v: View, pos: Int) {
                    //아이템 클릭 리스너
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onDeleteItem(v: View, pos: Int) {
                    Log.d("로그", "HomeAlarmFragment - onDeleteItem :  포지션 : $pos")
                    DatabaseManager.getInstance(requireContext(), "Alarms.db").delete(mAlarmList[pos - 1])
                    mAlarmList.removeAt(pos - 1)
                    alarmAdapter.notifyDataSetChanged()
                }
            })
        val layoutManager = LinearLayoutManager(context)
        alarmView.layoutManager = layoutManager
        alarmView.adapter = alarmAdapter
    }
}