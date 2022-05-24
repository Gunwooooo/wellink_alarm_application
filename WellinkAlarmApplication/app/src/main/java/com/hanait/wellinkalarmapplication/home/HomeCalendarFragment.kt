package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.GridLayoutManager
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.FragmentHomeCalendarBinding
import com.hanait.wellinkalarmapplication.utils.BaseFragment
import com.hanait.wellinkalarmapplication.utils.CustomDialogFragment
import com.hanait.wellinkalarmapplication.utils.OnSwipeTouchListener
import java.util.*

class HomeCalendarFragment : BaseFragment<FragmentHomeCalendarBinding>(FragmentHomeCalendarBinding::inflate), View.OnClickListener {
    private val mCalendarList = arrayOfNulls<Pair<String, Int>>(42)
    lateinit var cal: GregorianCalendar
    var prevChoiceDay = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initCalendarList()
    }

    @SuppressLint("ClickableViewAccessibility")
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
        //오늘 날짜 테두리 표시
        binding.homeCalendarRecyclerView.doOnPreDraw {
            val todayPos =
                GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    1, 0, 0, 0).get(Calendar.DAY_OF_WEEK) - 1 + cal.get(Calendar.DAY_OF_MONTH) - 1
            binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(todayPos)?.setBackgroundResource(R.drawable.calendar_choice_item_border)
            prevChoiceDay = todayPos
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCalendarList() {
        cal = GregorianCalendar()
        binding.homeCalendarTextViewMonth.text = "${cal.get(Calendar.YEAR)}년 ${cal.get(Calendar.MONTH) + 1}월"
        setCalendarList(cal)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v) {
            binding.homeCalendarPrevBtn -> {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) -1 , 1)
                setCalendarList(cal)
                binding.homeCalendarTextViewMonth.text = "${cal.get(Calendar.YEAR)}년 ${cal.get(Calendar.MONTH) + 1}월"
            }
            binding.homeCalendarNextBtn -> {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1)
                setCalendarList(cal)
                binding.homeCalendarTextViewMonth.text = "${cal.get(Calendar.YEAR)}년 ${cal.get(Calendar.MONTH) + 1}월"
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

    private fun recyclerViewCreate() {
        val calendarView = binding.homeCalendarRecyclerView
        val calendarAdapter =
            context?.let { CalendarAdapter(it, mCalendarList, HomeCalendarFragment()) }

        calendarAdapter?.setOnItemClickListener(
            object : CalendarAdapter.OnItemClickListener {
                override fun onDayItemClick(v: View, pos: Int) {
                    //테두리 구현 부분
                    Log.d("로그", "HomeCalendarFragment - onDayItemClick : pos  =  $pos")
                    binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(prevChoiceDay)?.setBackgroundResource(R.drawable.calendar_item_border)
                    binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(pos)?.setBackgroundResource(R.drawable.calendar_choice_item_border)
                    prevChoiceDay = pos
                    showDialog()
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

    fun showDialog() {
        val customDialog = CustomDialogFragment(R.layout.home_calendar_dialog)
        fragmentManager?.let { customDialog.show(it, "home_calendar_dialog") }
    }
}