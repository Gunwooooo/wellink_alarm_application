package com.hanait.wellinkalarmapplication.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.CalendarData
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

                ////////////////////////////////////////////////////////////////////////////////////////////////////////
                //오늘 날짜 이후로는 selectCalendarItemAlarm으로 데이터 가져와서 출력하기
                //이전 날짜라면 mCalendarList에서 해당 날짜 데이터만 출력하기
                //DB에서 해당하는 약 데이터 가져오기
                if(cal)
                val strDate = cal?.time?.let { sdf.format(it) }
                val mAlarmList: ArrayList<AlarmData> =
                    context?.let { DatabaseManager.getInstance(it, "Alarms.db").selectCalendarItemAlarm(strDate!!) }!!

                val morningArray = Array(3) { "" }
                val afternoonArray = Array(3) { "" }
                val eveningArray = Array(3) { "" }
                val nightArray = Array(3) { "" }

                //0 : 복용예정   1:복용   2:미복용
                for(i in 0 until mAlarmList.size ) {
                    //DB에서 캘린더 데이터 가져오기
                    var calendarData: CalendarData? = CalendarData(0, strDate!!, "", 0, 0, 0, 0)
                    if(DatabaseManager.getInstance(requireContext(), "Alarms.db").selectCalendarAsDateAndName(strDate, mAlarmList[i].name) != null) {
                        calendarData = DatabaseManager.getInstance(requireContext(), "Alarms.db").selectCalendarAsDateAndName(strDate, mAlarmList[i].name)
                    }
                    if(mAlarmList[i].mswitch == 1) {
                        when(calendarData?.mtaken) {
                            0 -> morningArray[0] += "${mAlarmList[i].name}  "
                            1 -> morningArray[1] += "${mAlarmList[i].name}  "
                            2 -> morningArray[2] += "${mAlarmList[i].name}  "
                        }
                    }
                    if(mAlarmList[i].aswitch == 1) {
                        when(calendarData?.ataken) {
                            0 -> afternoonArray[0] += "${mAlarmList[i].name}  "
                            1 -> afternoonArray[1] += "${mAlarmList[i].name}  "
                            2 -> afternoonArray[2] += "${mAlarmList[i].name}  "
                        }
                    }
                    if(mAlarmList[i].eswitch == 1) {
                        when(calendarData?.etaken) {
                            0 -> eveningArray[0] += "${mAlarmList[i].name}  "
                            1 -> eveningArray[1] += "${mAlarmList[i].name}  "
                            2 -> eveningArray[2] += "${mAlarmList[i].name}  "
                        }
                    }
                    if(mAlarmList[i].nswitch == 1) {
                        when(calendarData?.ntaken) {
                            0 -> nightArray[0] += "${mAlarmList[i].name}  "
                            1 -> nightArray[1] += "${mAlarmList[i].name}  "
                            2 -> nightArray[2] += "${mAlarmList[i].name}  "
                        }
                    }
                }

                val morningTextView = view.findViewById(R.id.homeCalendarDialog_textView_morning) as TextView
                val afternoonTextView = view.findViewById(R.id.homeCalendarDialog_textView_afternoon) as TextView
                val eveningTextView = view.findViewById(R.id.homeCalendarDialog_textView_evening) as TextView
                val nightTextView = view.findViewById(R.id.homeCalendarDialog_textView_night) as TextView

                var spannableString = SpannableString("${morningArray[1]}${morningArray[2]}${morningArray[0]}")

                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#487fee")), 0, morningArray[1].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if(morningArray[1].isNotEmpty() && morningArray[2].isNotEmpty()) spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#d0252e")), morningArray[1].length, morningArray[1].length + morningArray[2].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                else if(morningArray[1].isEmpty() && morningArray[2].isNotEmpty()) spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#d0252e")), 0, morningArray[1].length + morningArray[2].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                morningTextView.text = spannableString

                spannableString = SpannableString("${afternoonArray[1]}${afternoonArray[2]}${afternoonArray[0]}")

                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#487fee")), 0, afternoonArray[1].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if(afternoonArray[1].isNotEmpty() && afternoonArray[2].isNotEmpty()) spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#d0252e")), afternoonArray[1].length, afternoonArray[1].length + afternoonArray[2].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                else if(afternoonArray[1].isEmpty() && afternoonArray[2].isNotEmpty()) spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#d0252e")), 0, afternoonArray[1].length + afternoonArray[2].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                afternoonTextView.text = spannableString

                spannableString = SpannableString("${eveningArray[1]}${eveningArray[2]}${eveningArray[0]}")

                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#487fee")), 0, eveningArray[1].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if(eveningArray[1].isNotEmpty() && eveningArray[2].isNotEmpty()) spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#d0252e")), eveningArray[1].length, eveningArray[1].length + eveningArray[2].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                else if(eveningArray[1].isEmpty() && eveningArray[2].isNotEmpty()) spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#d0252e")), 0, eveningArray[1].length + eveningArray[2].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                eveningTextView.text = spannableString

                spannableString = SpannableString("${nightArray[1]}${nightArray[2]}${nightArray[0]}")

                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#487fee")), 0, nightArray[1].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if(nightArray[1].isNotEmpty() && nightArray[2].isNotEmpty()) spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#d0252e")), nightArray[1].length, nightArray[1].length + nightArray[2].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                else if(nightArray[1].isEmpty() && nightArray[2].isNotEmpty()) spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#d0252e")), 0, nightArray[1].length + nightArray[2].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                nightTextView.text = spannableString
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
