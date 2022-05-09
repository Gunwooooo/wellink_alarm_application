package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeCalendarBinding
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import java.util.*
import com.hanait.wellinkalarmapplication.R
import android.view.MotionEvent

import android.view.GestureDetector
import android.view.View.OnTouchListener
import kotlin.math.abs


class HomeCalendarFragment : BaseFragment<FragmentHomeCalendarBinding>(FragmentHomeCalendarBinding::inflate), View.OnClickListener {
    val mCalendarList = arrayOfNulls<Pair<String, Int>>(42)
    lateinit var cal: GregorianCalendar
    var prevChoiceDay = 0
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initCalendarList()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        binding.homeCalendarNextBtn.setOnClickListener(this)
        binding.homeCalendarPrevBtn.setOnClickListener(this)
    }

    private fun initCalendarList() {
        cal = GregorianCalendar()
        binding.homeCalendarTextView.text = "${cal.get(Calendar.YEAR)}년 ${cal.get(Calendar.MONTH) + 1}월"

        //오늘 날짜 테두리 표시
        val week =
            GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0).get(Calendar.DAY_OF_WEEK) - 1 + cal.get(Calendar.DAY_OF_MONTH)
        binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(week)?.setBackgroundResource(R.drawable.calendar_choice_item_border)
        prevChoiceDay = week

        setCalendarList(cal)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v) {
            binding.homeCalendarPrevBtn -> {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) -1 , 1)
                Log.d("로그", "HomeCalendarFragment - onClick : 이전 달")
                setCalendarList(cal)
                binding.homeCalendarTextView.text = "${cal.get(Calendar.YEAR)}년 ${cal.get(Calendar.MONTH) + 1}월"
            }
            binding.homeCalendarNextBtn -> {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1)
                Log.d("로그", "HomeCalendarFragment - onClick : 다음 달")
                setCalendarList(cal)
                binding.homeCalendarTextView.text = "${cal.get(Calendar.YEAR)}년 ${cal.get(Calendar.MONTH) + 1}월"
            }
        }
    }

    //캘리넏 날짜 데이터 셋팅
    fun setCalendarList(cal: GregorianCalendar) {
        //현재 날짜의 1일
        val calendar =
            GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0)
        //저번달의 첫번째 1일
        val prevCalendar =
            GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, 1, 0, 0, 0)
        //앞에 빈 공간
        val week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        prevChoiceDay = week + 1
        //뒤에 빈 공간
        val max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1
        for (i: Int in mCalendarList.indices) {
            if (i < week) { // 저번달의 끝의 일수을 설정
                val tmpString =
                    (prevCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - week + i + 1).toString()
                if (i % 7 == 0) mCalendarList[i] = Pair(tmpString, 8)
                else if (i % 7 == 6) mCalendarList[i] = Pair(tmpString, 7)
                else mCalendarList[i] = Pair(tmpString, 6)
            } else if (i > max + week) { // 이번달 끝이후의 일수를 설정
                val tmpString = (i - (max + week)).toString()
                if (i % 7 == 0) mCalendarList[i] = Pair(tmpString, 5)
                else if (i % 7 == 6) mCalendarList[i] = Pair(tmpString, 4)
                else mCalendarList[i] = Pair(tmpString, 3)
            } else { // 이번달 일수
                val tmpString = " " + (i - week + 1) + " "
                if (i % 7 == 0) mCalendarList[i] = Pair(tmpString, 2)
                else if (i % 7 == 6) mCalendarList[i] = Pair(tmpString, 1)
                else mCalendarList[i] = Pair(tmpString, 0)
            }
        }
        recyclerViewCreate()
    }

    fun recyclerViewCreate() {
        val calendarView = binding.homeCalendarRecyclerView
        val calendarAdapter =
            context?.let { CalendarAdapter(it, mCalendarList, HomeCalendarFragment()) }

        calendarAdapter?.setOnItemClickListener(
            object : CalendarAdapter.OnItemClickListener {
                override fun onDayItemClick(v: View, pos: Int) {
                    //테두리 구현 부분
                    binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(prevChoiceDay)?.setBackgroundResource(R.drawable.calendar_item_border)
                    binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(pos)?.setBackgroundResource(R.drawable.calendar_choice_item_border)
                    prevChoiceDay = pos
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
}