package com.hanait.wellinkalarmapplication.utils

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.beans.AlarmData
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.home.AlarmAdapter
import com.hanait.wellinkalarmapplication.utils.Constants.sdf
import java.util.*

//달력 상세 화면 다이어로그
class CustomDialogFragment(private val layout: Int, private val cal: GregorianCalendar?) : DialogFragment(), View.OnClickListener {

    private var mListener: CustomDialogListener? = null
    interface CustomDialogListener {
        fun onPositiveClicked()
        fun onNegativeClicked()
    }
    fun setDialogListener(customDialogListener: CustomDialogListener) {
        this.mListener = customDialogListener
    }


    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(layout, null)
        builder.setView(view)

        when(layout){
            R.layout.home_alarm_delete_dialog -> {
                val positiveButton =
                    view.findViewById(R.id.homeAlarmDeleteDialog_btn_positive) as Button
                val negativeButton =
                    view.findViewById(R.id.homeAlarmDeleteDialog_btn_negative) as Button
                positiveButton.setOnClickListener(this)
                negativeButton.setOnClickListener(this)
            }
            R.layout.home_calendar_dialog -> {
                //상단 일 표시
                Log.d("로그", "${cal?.get(Calendar.YEAR)}  ${cal?.get(Calendar.MONTH)}   ${cal?.get(Calendar.DAY_OF_MONTH)} : ")
                val monthTextView = view.findViewById(R.id.homeCalendarDialog_textView_dayOfMonth) as TextView
                monthTextView.text = "${cal?.get(Calendar.DAY_OF_MONTH).toString()}일"
                //DB에서 해당하는 약 데이터 가져오기
                val strDate = cal?.time?.let { sdf.format(it) }
                Log.d("로그", " - onCreateDialog : strdate :  $strDate")
                var mAlarmList: ArrayList<AlarmData> = ArrayList()
                mAlarmList = context?.let { DatabaseManager.getInstance(it, "Alarms.db").selectCalendarItemAlarm(strDate!!) }!!
                for(i in 0 until mAlarmList.size ) {
                    Log.d("로그", "mAlarmList : $i     ${mAlarmList[i].name}")
                }
            }
        }

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout, container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return view
    }

    //버튼 클릭 리스너
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.homeAlarmDeleteDialog_btn_positive -> mListener?.onPositiveClicked()
            R.id.homeAlarmDeleteDialog_btn_negative -> mListener?.onNegativeClicked()
        }
    }
}
