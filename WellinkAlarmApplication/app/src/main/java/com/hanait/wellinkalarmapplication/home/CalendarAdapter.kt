package com.hanait.wellinkalarmapplication.home


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.databinding.HomeCalendarItemBinding


class CalendarAdapter(var context: Context, var data: Array<Pair<String, Int>?>) :
    RecyclerView.Adapter<CalendarAdapter.VH>() {

    private val DAY_TYPE = 0
    private val DAY_BLUE_TYPE = 1
    private val DAY_RED_TYPE = 2
    private val DAY_EMPTY_NEXT_TYPE = 3
    private val DAY_EMPTY_BLUE_NEXT_TYPE = 4
    private val DAY_EMPTY_RED_NEXT_TYPE = 5
    private val DAY_EMPTY_PREV_TYPE = 6
    private val DAY_EMPTY_BLUE_PREV_TYPE = 7
    private val DAY_EMPTY_RED_PREV_TYPE = 8


    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var dayTextView: TextView
        fun setDayTextView(day: String?, color:Int) {
            dayTextView = itemView.findViewById(R.id.homeCalendarItem_textView)
            dayTextView.setTextColor(ContextCompat.getColor(context, color))
            dayTextView.text = day

        }
        fun setEmptyDayNextTextView(day: String?, color:Int) {
            dayTextView = itemView.findViewById(R.id.homeCalendarItemEmptyNext_textView)
            dayTextView.setTextColor(ContextCompat.getColor(context, color))
            dayTextView.text = day
            dayTextView.setOnClickListener {

            }
        }
        fun setEmptyDayPrevTextView(day: String?, color:Int) {
            dayTextView = itemView.findViewById(R.id.homeCalendarItemEmptyPrev_textView)
            dayTextView.setTextColor(ContextCompat.getColor(context, color))
            dayTextView.text = day
            dayTextView.setOnClickListener {
                HomeCalendarFragment().binding.homeCalendarPrevBtn.performClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when(viewType) {
            0, 1, 2 ->
                VH(LayoutInflater.from(context).inflate(R.layout.home_calendar_item, parent, false))
            3, 4, 5 ->
                VH(LayoutInflater.from(context).inflate(R.layout.home_calendar_item_empty_next, parent, false))
            6, 7, 8 ->
                VH(LayoutInflater.from(context).inflate(R.layout.home_calendar_item_empty_prev, parent, false))
            else ->
                VH(LayoutInflater.from(context).inflate(R.layout.home_calendar_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val tmpData = data[position]?.first
        when(getItemViewType(position)) {
            DAY_TYPE -> holder.setDayTextView(tmpData, R.color.toss_black_700)
            DAY_BLUE_TYPE -> holder.setDayTextView(tmpData, R.color.text_blue_500)
            DAY_RED_TYPE -> holder.setDayTextView(tmpData, R.color.text_red_500)
            DAY_EMPTY_NEXT_TYPE -> holder.setEmptyDayNextTextView(tmpData, R.color.toss_black_150)
            DAY_EMPTY_BLUE_NEXT_TYPE  -> holder.setEmptyDayNextTextView(tmpData, R.color.text_blue_100)
            DAY_EMPTY_RED_NEXT_TYPE -> holder.setEmptyDayNextTextView(tmpData, R.color.text_red_100)
            DAY_EMPTY_PREV_TYPE ->  holder.setEmptyDayPrevTextView(tmpData, R.color.toss_black_150)
            DAY_EMPTY_BLUE_PREV_TYPE -> holder.setEmptyDayPrevTextView(tmpData, R.color.text_blue_100)
            DAY_EMPTY_RED_PREV_TYPE ->holder.setEmptyDayPrevTextView(tmpData, R.color.text_red_100)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position]?.second) {
            0 -> DAY_TYPE
            1 -> DAY_BLUE_TYPE
            2 -> DAY_RED_TYPE
            3 -> DAY_EMPTY_NEXT_TYPE
            4 -> DAY_EMPTY_BLUE_NEXT_TYPE
            5 -> DAY_EMPTY_RED_NEXT_TYPE
            6 -> DAY_EMPTY_PREV_TYPE
            7 -> DAY_EMPTY_BLUE_PREV_TYPE
            8 -> DAY_EMPTY_RED_PREV_TYPE
            else -> 0
        }
    }
}