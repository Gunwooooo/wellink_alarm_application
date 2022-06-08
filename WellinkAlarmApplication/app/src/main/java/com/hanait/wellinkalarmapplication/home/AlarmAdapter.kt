package com.hanait.wellinkalarmapplication.home
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.model.AlarmData


class AlarmAdapter(var context: Context, var data: ArrayList<AlarmData>, var link: HomeAlarmFragment) :
    RecyclerView.Adapter<AlarmAdapter.VH>() {

    //adapter 클릭 리스너 외부 처리를 위한 인터페이스
    private var mListener: OnItemClickListener? = null

    public interface OnItemClickListener {
        fun onItemClick(v:View, pos:Int)
        fun onDeleteItem(v:View, pos:Int)
    }
    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var alarmNumTextView: TextView
        private lateinit var alarmNameTextView: TextView
        private lateinit var alarmMorningTextView: TextView
        private lateinit var alarmAfternoonTextView: TextView
        private lateinit var alarmEveningTextView: TextView
        private lateinit var alarmNightTextView: TextView
        private lateinit var alarmDeleteImageView: ImageView
        private lateinit var alarmItemLayout: LinearLayout
        fun setAlarmTextView(alarmData: AlarmData, position: Int) {
            alarmNumTextView = itemView.findViewById(R.id.homeAlarmItem_textView_num)
            alarmNameTextView = itemView.findViewById(R.id.homeAlarmItem_textView_name)

            alarmMorningTextView = itemView.findViewById(R.id.homeAlarmItem_textView_morning)
            alarmAfternoonTextView = itemView.findViewById(R.id.homeAlarmItem_textView_afternoon)
            alarmEveningTextView = itemView.findViewById(R.id.homeAlarmItem_textView_evening)
            alarmNightTextView = itemView.findViewById(R.id.homeAlarmItem_textView_night)

            alarmNumTextView.text = position.toString()
            alarmNameTextView.text = alarmData.name


            if(alarmData.mswitch == 1) alarmMorningTextView.setTextColor(ContextCompat.getColor(context, R.color.toss_black_700))
            if(alarmData.aswitch == 1) alarmAfternoonTextView.setTextColor(ContextCompat.getColor(context, R.color.toss_black_700))
            if(alarmData.eswitch == 1) alarmEveningTextView.setTextColor(ContextCompat.getColor(context, R.color.toss_black_700))
            if(alarmData.nswitch == 1) alarmNightTextView.setTextColor(ContextCompat.getColor(context, R.color.toss_black_700))


            alarmDeleteImageView = itemView.findViewById(R.id.homeAlarmItem_imageView_delete)
            alarmItemLayout = itemView.findViewById(R.id.homeAlarmItem_layout)
            alarmDeleteImageView.setOnClickListener {
                mListener?.onDeleteItem(it, position)
            }
            alarmItemLayout.setOnClickListener {
                mListener?.onItemClick(it, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.home_alarm_item, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val tmpData = data[position]
        holder.setAlarmTextView(tmpData, position+1)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}