package com.hanait.wellinkalarmapplication.utils

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.home.AlarmAdapter

//달력 상세 화면 다이어로그
class CustomDialogFragment(private val layout: Int) : DialogFragment(), View.OnClickListener {

    private var mListener: CustomDialogListener? = null
    interface CustomDialogListener {
        fun onPositiveClicked()
        fun onNegativeClicked()
    }
    fun setDialogListener(customDialogListener: CustomDialogListener) {
        this.mListener = customDialogListener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(layout, null)
        builder.setView(view)

        //알람 삭제 다이어 로그 클릭 리스너 초기화
        if(layout == R.layout.home_alarm_delete_dialog) {
            val positiveButton = view.findViewById(R.id.homeAlarmDeleteDialog_btn_positive) as Button
            val negativeButton = view.findViewById(R.id.homeAlarmDeleteDialog_btn_negative) as Button
            positiveButton.setOnClickListener(this)
            negativeButton.setOnClickListener(this)
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
