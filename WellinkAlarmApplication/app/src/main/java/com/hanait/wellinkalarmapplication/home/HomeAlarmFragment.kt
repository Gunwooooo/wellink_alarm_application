package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeAlarmBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.mAlarmList
import com.hanait.wellinkalarmapplication.utils.Constants.tempAlarmData2
import com.hanait.wellinkalarmapplication.utils.CustomDialogFragment
import java.util.*


class HomeAlarmFragment : BaseFragment<FragmentHomeAlarmBinding>(FragmentHomeAlarmBinding::inflate), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeAlarmAddAlarm.setOnClickListener(this)
        binding.homeAlarmFloatingButton.setOnClickListener(this)
        setTextAlarmCountAndExplain()
        recyclerViewCreate()
        for(i in 0 until mAlarmList.size) {
            Log.d("로그", "HomeAlarmFragment - onViewCreated : ${mAlarmList[i]}")
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.homeAlarmAddAlarm, binding.homeAlarmFloatingButton -> {
                tempAlarmData2 = null
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
                    //알람 데이터 수정하기
                    Log.d("로그", "HomeAlarmFragment - onItemClick : ${mAlarmList[pos - 1]}")

                    val intent = Intent(context, SetAlarmActivity::class.java)
                    tempAlarmData2 = AlarmData()
                    tempAlarmData2 = mAlarmList[pos - 1]
                    startActivity(intent)
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
        val customDialog = CustomDialogFragment(R.layout.home_alarm_delete_dialog, null)
        customDialog.setDialogListener(object:CustomDialogFragment.DeleteDialogListener {
            @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
            //삭제 예 버튼 클릭
            override fun onPositiveClicked() {
                //오늘 복용 데이터 삭제하기
                val todayCal = GregorianCalendar()
                val todayStrDate = todayCal.time.let { Constants.sdf.format(it) }
                DatabaseManager.getInstance(requireContext(), "Alarms.db").deleteCalendarAsDateAndName(mAlarmList[pos - 1].name, todayStrDate)

                //알람 DB에서 삭제 
                DatabaseManager.getInstance(requireContext(), "Alarms.db").deleteAlarm(mAlarmList[pos - 1].name)
                
                //알람 리스트에서 삭제
                val alarmId = mAlarmList[pos-1].id
                mAlarmList.removeAt(pos - 1)
                setTextAlarmCountAndExplain()
                alarmAdapter.notifyDataSetChanged()

                //alarmmanager 아점저취 모두 삭제 -> 서비스 호출 종료
                val intent = Intent(context, AlarmReceiver::class.java)
                for(i in 0 until 4) {
                    val pendingId = alarmId * 4 + i
                    val alarmIntent = PendingIntent.getBroadcast(context, pendingId, intent, 0)
                    val alarmManager = context?.let { ContextCompat.getSystemService(it, AlarmManager::class.java) }
                    alarmManager?.cancel(alarmIntent)
                    Log.d("로그", "HomeAlarmFragment - onPositiveClicked : $pendingId 알람 삭제 됩니다")
                }
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