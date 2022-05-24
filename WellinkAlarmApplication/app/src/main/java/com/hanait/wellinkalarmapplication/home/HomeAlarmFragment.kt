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
import com.hanait.wellinkalarmapplication.utils.CustomDialogFragment


class HomeAlarmFragment : BaseFragment<FragmentHomeAlarmBinding>(FragmentHomeAlarmBinding::inflate), View.OnClickListener {
    private var mAlarmList: ArrayList<AlarmData> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeAlarmAddAlarm.setOnClickListener(this)

        getAlarmList()

        recyclerViewCreate()
    }
    
    private fun getAlarmList() {
        mAlarmList = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectAll()
        Log.d("로그", "HomeAlarmFragment - getAlarmList : 알람 갯수 : ${mAlarmList.size}")
        setTextAlarmCountAndExplain()
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
                    Log.d("로그", "HomeAlarmFragment - onItemClick : 아이템 클릭됨!  $pos")
                }
                override fun onDeleteItem(v: View, pos: Int) {
                    showDialog(alarmAdapter, pos)
                }
            })
        val layoutManager = LinearLayoutManager(context)
        alarmView.layoutManager = layoutManager
        alarmView.adapter = alarmAdapter
    }

    //알람 갯수 및 상단 설명 글 수정
    @SuppressLint("SetTextI18n")
    fun  setTextAlarmCountAndExplain() {
        binding.homeAlarmTextViewAlarmCount.text = "${mAlarmList.size}개"
        if(mAlarmList.size != 0) {
            binding.homeAlarmTextViewExplain.text = "아래 약들의 알람을 울려드려요"
            return
        }
        binding.homeAlarmTextViewExplain.text = "현재 복용중인 약이 없어요"
    }

    //알람 삭제 다이어로그
    fun showDialog(alarmAdapter:AlarmAdapter, pos:Int) {
        val customDialog = CustomDialogFragment(R.layout.home_alarm_delete_dialog)
        customDialog.setDialogListener(object:CustomDialogFragment.CustomDialogListener {
            @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
            //삭제 예 버튼 클릭
            override fun onPositiveClicked() {
                DatabaseManager.getInstance(requireContext(), "Alarms.db").delete(mAlarmList[pos - 1])
                mAlarmList.removeAt(pos - 1)
                setTextAlarmCountAndExplain()
                alarmAdapter.notifyDataSetChanged()
                customDialog.dismiss()
            }
            //삭제 아니요 버튼 클릭
            override fun onNegativeClicked() {
                customDialog.dismiss()
            }
        })
        fragmentManager?.let { customDialog.show(it, "home_alarm_delete_dialog") }
    }
}