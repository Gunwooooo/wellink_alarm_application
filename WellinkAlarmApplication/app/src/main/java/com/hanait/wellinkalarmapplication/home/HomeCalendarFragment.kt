package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
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
        
        //오늘 날짜 테두리 표시
        setTodayBorder()
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
        //달력 전부 그리기 전에 오늘 날짜 테두리 표시
        binding.homeCalendarRecyclerView.doOnPreDraw {
            val todayPos =
                GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    1, 0, 0, 0).get(Calendar.DAY_OF_WEEK) - 1 + cal.get(Calendar.DAY_OF_MONTH) - 1
            binding.homeCalendarRecyclerView.layoutManager?.findViewByPosition(todayPos)?.setBackgroundResource(R.drawable.calendar_choice_item_border)
            prevChoiceDay = todayPos
        }
    }

    //월별 달력 정보 가져오기
    @SuppressLint("SimpleDateFormat")
    private fun getCalendarAsMonth(cal: Calendar) {

        val year = SimpleDateFormat("yyyy").format(cal.time)
        val month = SimpleDateFormat("MM").format(cal.time)
        //아이콘 표시를 위한 arrayList
        //index -> 0:데이터 유무  1:복용갯수  2:미복용갯수  3:오늘날자 체크 4:복용 예정 약 개수
        mCalendarList = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectCalendarAsMonth(year, month)
        takenArray = Array(32) { IntArray(5) { 0 } }

        //오늘 날짜 체크하기
        var j = 1

        val todayCal = GregorianCalendar()
        if( todayCal.get(Calendar.YEAR) ==  cal.get(Calendar.YEAR) && todayCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
            j = todayCal.get(Calendar.DAY_OF_MONTH)
            takenArray[todayCal.get(Calendar.DAY_OF_MONTH)][3] = 1
        }

        //복용 예정일 알람 개수 저장
        if(todayCal.get(Calendar.YEAR) < cal.get(Calendar.YEAR) || todayCal.get(Calendar.MONTH) <= cal.get(Calendar.MONTH)) {
            for (i in j until 32) {
                val tempCal = GregorianCalendar()
                tempCal.set(Calendar.YEAR, cal.get(Calendar.YEAR))
                tempCal.set(Calendar.MONTH, cal.get(Calendar.MONTH))
                tempCal.set(Calendar.DAY_OF_MONTH, i)
                val strDate = tempCal.time.let { Constants.sdf.format(it) }
                val dayOfAlarmSize = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectCalendarItemAsDate(strDate).size
                takenArray[i][4] = dayOfAlarmSize
            }
        }


        //복용 정보 따로 저장하기
        for(i in 0 until mCalendarList.size) {
            val index = mCalendarList[i].date.split('-')[2].toInt()
            takenArray[index][0] = 1
            val arr = arrayOf(mCalendarList[i].mtaken, mCalendarList[i].ataken, mCalendarList[i].etaken, mCalendarList[i].ntaken)
            for(k in 0 until 4) {
                if(arr[k] != 0) {
                    takenArray[index][arr[k]]++
                }
            }
        }
    }

    //초기 달력 만들기
    @SuppressLint("SetTextI18n")
    private fun initCalendarList() {
        cal = GregorianCalendar()
        //월 캘린더 복용 데이터 가져오기
        getCalendarAsMonth(cal)
        binding.homeCalendarTextViewMonth.text = "${cal.get(Calendar.YEAR)}년  ${cal.get(Calendar.MONTH) + 1}월"
        setCalendarList(cal)
    }

    
    //클릭 리스너
    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v) {
            //다음 달로 변경
            binding.homeCalendarPrevBtn -> {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) -1 , 1)
                setCalendarList(cal)
                getCalendarAsMonth(cal)
                binding.homeCalendarTextViewMonth.text = "${cal.get(Calendar.YEAR)}년  ${cal.get(Calendar.MONTH) + 1}월"
                setTodayBorder()
            }
            //이전 달로 변경
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

    //리사이클러뷰로 달력 그리기
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

    //해당 날짜 복용 정보 다이얼로그 표시
    fun showDialog(dayOfMonth: Int) {
        val calendar = GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), dayOfMonth, 0, 0, 0)
        val customDialog = CustomDialogFragment(R.layout.home_calendar_dialog, calendar)
        customDialog.setDialogListener(object:CustomDialogFragment.CalendarDialogListener {
            //알람 설정하러 가기 버튼 클릭 이벤트 처리
            override fun onGoAlarmIntentClicked() {
                Constants.tempAlarmData2 = null
                val intent = Intent(context, SetAlarmActivity::class.java)
                startActivity(intent)
                customDialog.dismiss()
            }
        })
        fragmentManager?.let { customDialog.show(it, "home_calendar_dialog") }
    }
}