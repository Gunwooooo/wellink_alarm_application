package com.hanait.wellinkalarmapplication.home


import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.SplashActivity.Companion._windowHeight
import com.hanait.wellinkalarmapplication.home.HomeCalendarFragment.Companion.takenArray
import java.util.*


class CalendarAdapter(
    var context: Context,
    var data: Array<Pair<String, Int>?>,
    cal: GregorianCalendar
) :
    RecyclerView.Adapter<CalendarAdapter.VH>() {

    private val glide by lazy { Glide.with(context) }

    private val DAY_TYPE = 0
    private val DAY_BLUE_TYPE = 1
    private val DAY_RED_TYPE = 2
    private val DAY_EMPTY_NEXT_TYPE = 3
    private val DAY_EMPTY_BLUE_NEXT_TYPE = 4
    private val DAY_EMPTY_RED_NEXT_TYPE = 5
    private val DAY_EMPTY_PREV_TYPE = 6
    private val DAY_EMPTY_BLUE_PREV_TYPE = 7
    private val DAY_EMPTY_RED_PREV_TYPE = 8

    //adapter 클릭 리스너 외부 처리를 위한 인터페이스
    private var mListener: OnItemClickListener? = null
    public interface OnItemClickListener {
        fun onDayItemClick(v:View, pos:Int)
        fun onEmptyDayNextItemClick(v:View, pos:Int)
        fun onEmptyDayPrevItemClick(v:View, pos:Int)
    }
    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }
    /////////


    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var dayTextView: TextView
        private lateinit var dayView: LinearLayout
        private lateinit var dayImageView: ImageView
        private lateinit var dayImageCircle: ImageView
        fun setDayTextView(day: String?, color:Int) {
            dayTextView = itemView.findViewById(R.id.homeCalendarItem_textView)
            dayTextView.setTextColor(ContextCompat.getColor(context, color))
            dayTextView.text = day
            dayImageCircle = itemView.findViewById(R.id.homeCalendarItem_imageView_circle)
            dayView = itemView.findViewById(R.id.homeCalendarItem_view)

            dayView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) mListener?.onDayItemClick(it, pos)
            }

            //복용 데이터 가져와서 화면에 이미지 뿌리기
            //DB에서 캘린더 데이터 가져오기
            //오늘 날짜는 안띄우기
            dayImageView = itemView.findViewById(R.id.homeCalendarItem_imageView)

            val index = day?.replace(" ", "")?.toInt()
            if (index != null) {
                //복용 예정 약이 있는 view
                if(takenArray[index][4] != 0) {
                    dayImageCircle.visibility = View.VISIBLE
                }
                if(takenArray[index][3] == 1) {
                    dayTextView.setTextColor(ContextCompat.getColor(context, R.color.teal_200))
                    return
                }
                if(takenArray[index][0] == 0 ) return
                else if(takenArray[index][1] > 0 && takenArray[index][2] == 0) glide.load(R.drawable.check).into(dayImageView)
                else if(takenArray[index][1] > 0 && takenArray[index][2] > 0) glide.load(R.drawable.warn).into(dayImageView)
                else glide.load(R.drawable.cancel).into(dayImageView)
            }
        }
        fun setEmptyDayNextTextView(day: String?, color:Int) {
            dayTextView = itemView.findViewById(R.id.homeCalendarItemEmptyNext_textView)
            dayTextView.setTextColor(ContextCompat.getColor(context, color))
            dayTextView.text = day
            dayTextView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) mListener?.onEmptyDayNextItemClick(it, pos)
            }
        }
        fun setEmptyDayPrevTextView(day: String?, color:Int) {
            dayTextView = itemView.findViewById(R.id.homeCalendarItemEmptyPrev_textView)
            dayTextView.setTextColor(ContextCompat.getColor(context, color))
            dayTextView.text = day
            dayTextView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) mListener?.onEmptyDayPrevItemClick(it, pos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        // 전체 리사이클러뷰 크기 / 6해서 화면 꽉채우기
        val itemHeight = (_windowHeight- 48 - 46 - 48 - 60 - 32) / 6

        val itemHeightToPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeight, context.resources.displayMetrics).toInt()
        return when(viewType) {

            0, 1, 2 -> {
                val itemView = LayoutInflater.from(context).inflate(R.layout.home_calendar_item, parent, false)
                itemView.layoutParams.height = itemHeightToPx
                VH(itemView)
            }
            3, 4, 5 -> {
                val itemView = LayoutInflater.from(context).inflate(R.layout.home_calendar_item_empty_next, parent, false)
                itemView.layoutParams.height = itemHeightToPx
                VH(itemView)
            }
            6, 7, 8 -> {
                val itemView = LayoutInflater.from(context).inflate(R.layout.home_calendar_item_empty_prev, parent, false)
                itemView.layoutParams.height = itemHeightToPx
                VH(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(context).inflate(R.layout.home_calendar_item, parent, false)
                itemView.layoutParams.height = itemHeightToPx
                VH(itemView)
            }
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