package com.hanait.wellinkalarmapplication.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.db.DatabaseManager
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


    @SuppressLint("SetTextI18n", "RtlHardcoded")
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
                val monthTextView = view.findViewById(R.id.homeCalendarDialog_textView_dayOfMonth) as TextView
                monthTextView.text = "${cal?.get(Calendar.DAY_OF_MONTH).toString()}일"

                //DB에서 해당하는 약 데이터 가져오기
                val strDate = cal?.time?.let { sdf.format(it) }
                var mAlarmList: ArrayList<AlarmData> = ArrayList()
                mAlarmList = context?.let { DatabaseManager.getInstance(it, "Alarms.db").selectCalendarItemAlarm(strDate!!) }!!

                //스위치 온인 부분에 표시
                val morningNotTakenTextView = view.findViewById(R.id.homeCalendarDialog_textView_notTaken_morning) as TextView
                val morningTakenTextView = view.findViewById(R.id.homeCalendarDialog_textView_taken_morning) as TextView
                val afternoonNotTakenTextView = view.findViewById(R.id.homeCalendarDialog_textView_notTaken_afternoon) as TextView
                val afternoonTakenTextView = view.findViewById(R.id.homeCalendarDialog_textView_taken_afternoon) as TextView
                val eveningNotTakenTextView = view.findViewById(R.id.homeCalendarDialog_textView_notTaken_evening) as TextView
                val eveningTakenTextView = view.findViewById(R.id.homeCalendarDialog_textView_taken_evening) as TextView
                val nightNotTakenTextView = view.findViewById(R.id.homeCalendarDialog_textView_notTaken_night) as TextView
                val nightTakenTextView = view.findViewById(R.id.homeCalendarDialog_textView_taken_night) as TextView

                for(i in 0 until mAlarmList.size ) {
                    Log.d("로그", "mAlarmList : ${i+1}     $mAlarmList")
                    if(mAlarmList[i].mswitch == 1) {
                        if(mAlarmList[i].mtaken == 1) {
                            morningTakenTextView.text = "${morningTakenTextView.text}  ${mAlarmList[i].name}"
                        } else {
                            morningNotTakenTextView.text = "${morningNotTakenTextView.text}  ${mAlarmList[i].name}"
                        }
                    }
                    if(mAlarmList[i].aswitch == 1) {
                        if(mAlarmList[i].mtaken == 1) {
                            afternoonTakenTextView.text = "${afternoonTakenTextView.text}  ${mAlarmList[i].name}"
                        } else {
                            afternoonNotTakenTextView.text = "${afternoonNotTakenTextView.text}  ${mAlarmList[i].name}"
                        }
                    }
                    if(mAlarmList[i].eswitch == 1) {
                        if(mAlarmList[i].mtaken == 1) {
                            eveningTakenTextView.text = "${eveningTakenTextView.text}  ${mAlarmList[i].name}"
                        } else {
                            eveningNotTakenTextView.text = "${eveningNotTakenTextView.text}  ${mAlarmList[i].name}"
                        }
                    }
                    if(mAlarmList[i].nswitch == 1) {
                        if(mAlarmList[i].mtaken == 1) {
                            nightTakenTextView.text = "${nightTakenTextView.text}  ${mAlarmList[i].name}"
                        } else {
                            nightNotTakenTextView.text = "${nightNotTakenTextView.text}  ${mAlarmList[i].name}"
                        }
                    }
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
