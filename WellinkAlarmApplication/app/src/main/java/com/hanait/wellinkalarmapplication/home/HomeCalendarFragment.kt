package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.GridLayoutManager
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeCalendarBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.CalendarData
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.CustomDialogFragment
import com.hanait.wellinkalarmapplication.utils.OnSwipeTouchListener
import java.text.SimpleDateFormat
import java.util.*

class HomeCalendarFragment : BaseFragment<FragmentHomeCalendarBinding>(FragmentHomeCalendarBinding::inflate), View.OnClickListener {
    private val calendarRecyclerList = arrayOfNulls<Pair<String, Int>>(42)
    lateinit var cal: GregorianCalendar
    var prevChoiceDay = 0

    companion object {
        lateinit var mCalendarList : ArrayList<CalendarData>
        var takenArray = Array(32) { IntArray(4) { 0 } }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initCalendarList()
    }

    @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
    private fun init() {
        binding.homeCalendarNextBtn.setOnClickListener(this)
        binding.homeCalendarPrevBtn.setOnClickListener(this)
        binding.homeCalendarRecyclerView.setOnTouchListener(object: OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                binding.homeCalendarNextBtn.performClick()
            }
            override fun onSwipeRight() {
                binding.homeCalendarPrevBtn.performClick()
            }
        })
        setTodayBorder()
    }

    private fun setTodayBorder() {
        //오늘 날짜 테두리 표시
        binding.homeCalendarRecyclerView.doOnPreDraw {
            val todayPos =
                GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    1, 0, 0, 0).get(Calendar.DAY_OF_WEEK) - 1 + cal.get(Calendar.DAY_OF_MONTH) - 1
            binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(todayPos)?.setBackgroundResource(R.drawable.calendar_choice_item_border)
            prevChoiceDay = todayPos
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCalendarAsMonth(cal: Calendar) {
        val month = SimpleDateFormat("MM").format(cal.time)
        //아이콘 표시를 위한 arrayList
        mCalendarList = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectCalendarAsMonth(month)
        takenArray = Array(32) { IntArray(4) { 0 } }

        Log.d("로그", "HomeCalendarFragment - getCalendarAsMonth : 복용 데이터 크기 : ${mCalendarList.size}")
        for(i in 0 until mCalendarList.size) {
            Log.d("로그", "HomeCalendarFragment - getCalendarAsMonth : mCalendarList[$i] : ${mCalendarList[i]}")
        }
        
        //오늘 날짜 체크하기
        val todayCal = GregorianCalendar()
        if( todayCal.get(Calendar.YEAR) ==  cal.get(Calendar.YEAR) && todayCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
            takenArray[todayCal.get(Calendar.DAY_OF_MONTH)][3] = 1
        }

        //복용 정보 따로 저장하기
        for(i in 0 until mCalendarList.size) {
            val index = mCalendarList[i].date.split('-')[2].toInt()
            takenArray[index][0] = 1
            val arr = arrayOf(mCalendarList[i].mtaken, mCalendarList[i].ataken, mCalendarList[i].etaken, mCalendarList[i].ntaken)
            for(j in 0 until 4) {
                if(arr[j] != 0) {
                    takenArray[index][arr[j]]++
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCalendarList() {
        cal = GregorianCalendar()
        //월 캘린더 복용 데이터 가져오기
        getCalendarAsMonth(cal)
        binding.homeCalendarTextViewMonth.text = "${cal.get(Calendar.YEAR)}년  ${cal.get(Calendar.MONTH) + 1}월"
        setCalendarList(cal)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v) {
            binding.homeCalendarPrevBtn -> {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) -1 , 1)
                setCalendarList(cal)
                getCalendarAsMonth(cal)
                binding.homeCalendarTextViewMonth.text = "${cal.get(Calendar.YEAR)}년  ${cal.get(Calendar.MONTH) + 1}월"
                setTodayBorder()
            }
            binding.homeCalendarNextBtn -> {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1)
                setCalendarList(cal)
                getCalendarAsMonth(cal)
                binding.homeCalendarTextViewMonth.text = "${cal.get(Calendar.YEAR)}년  ${cal.get(Calendar.MONTH) + 1}월"
                setTodayBorder()
            }
        }
    }

    //캘린더 날짜 데이터 셋팅
    private fun setCalendarList(cal: GregorianCalendar) {
        //현재 날짜의 1일
        val calendar = GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0)
        //저번달의 첫번째 1일
        val prevCalendar = GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, 1, 0, 0, 0)
        //앞에 빈 공간
        val week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        //뒤에 빈 공간
        val max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1
        for (i: Int in calendarRecyclerList.indices) {
            if (i < week) { // 저번달의 끝의 일수을 설정
                val tmpString =
                    (prevCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - week + i + 1).toString()
                if (i % 7 == 0) calendarRecyclerList[i] = Pair(tmpString, 8)
                else if (i % 7 == 6) calendarRecyclerList[i] = Pair(tmpString, 7)
                else calendarRecyclerList[i] = Pair(tmpString, 6)
            } else if (i > max + week) { // 이번달 끝이후의 일수를 설정
                val tmpString = (i - (max + week)).toString()
                if (i % 7 == 0) calendarRecyclerList[i] = Pair(tmpString, 5)
                else if (i % 7 == 6) calendarRecyclerList[i] = Pair(tmpString, 4)
                else calendarRecyclerList[i] = Pair(tmpString, 3)
            } else { // 이번달 일수
                val tmpString = " " + (i - week + 1) + " "
                if (i % 7 == 0) calendarRecyclerList[i] = Pair(tmpString, 2)
                else if (i % 7 == 6) calendarRecyclerList[i] = Pair(tmpString, 1)
                else calendarRecyclerList[i] = Pair(tmpString, 0)
            }
        }
        recyclerViewCreate()
    }

    private fun recyclerViewCreate() {
        val calendarView = binding.homeCalendarRecyclerView
        val calendarAdapter =
            context?.let { CalendarAdapter(it, calendarRecyclerList, cal) }

        calendarAdapter?.setOnItemClickListener(
            object : CalendarAdapter.OnItemClickListener {
                override fun onDayItemClick(v: View, pos: Int) {
                    //테두리 구현 부분
                    binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(prevChoiceDay)?.setBackgroundResource(R.drawable.calendar_item_border)
                    binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(pos)?.setBackgroundResource(R.drawable.calendar_choice_item_border)
                    prevChoiceDay = pos

                    val dayOfMonth = binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(pos)?.findViewById(R.id.homeCalendarItem_textView) as TextView
                    showDialog(dayOfMonth.text.toString().replace(" ", "").toInt())
                }

                override fun onEmptyDayNextItemClick(v: View, pos: Int) {
                    binding.homeCalendarNextBtn.performClick()
                }

                override fun onEmptyDayPrevItemClick(v: View, pos: Int) {
                    binding.homeCalendarPrevBtn.performClick()
                }
            })

        val layoutManager = GridLayoutManager(context, 7)
        calendarView.layoutManager = layoutManager
        calendarView.adapter = calendarAdapter
    }

    fun showDialog(dayOfMonth: Int) {
        val calendar = GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), dayOfMonth, 0, 0, 0)
        val customDialog = CustomDialogFragment(R.layout.home_calendar_dialog, calendar)
        customDialog.setDialogListener(object:CustomDialogFragment.CalendarDialogListener {
            //알람 설정하러 가기 버튼 클릭 이벤트 처리
            override fun onGoAlarmIntentClicked() {
                Log.d("로그", "HomeCalendarFragment - onGoAlarmIntentChecked : 알람 등록하러 가기 클릭 됨")
                Constants.tempAlarmData2 = null
                val intent = Intent(context, SetAlarmActivity::class.java)
                startActivity(intent)
                customDialog.dismiss()
            }
        })
        fragmentManager?.let { customDialog.show(it, "home_calendar_dialog") }
    }
}