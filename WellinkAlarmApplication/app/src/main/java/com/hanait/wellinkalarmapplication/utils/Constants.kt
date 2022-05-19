package com.hanait.wellinkalarmapplication.utils

import android.annotation.SuppressLint
import android.widget.ProgressBar
import androidx.fragment.app.Fragment

@SuppressLint("StaticFieldLeak")
object Constants {
    lateinit var progressBar: ProgressBar
    lateinit var prevFragment: Fragment
}

object AlarmData {
    var name = ""
    var period = 1
    var expired = 0

    var mampm = 0
    var mhour = 0
    var mminute = 0
    var mswitch = 0

    var aampm = 0
    var ahour = 0
    var aminute = 0
    var aswitch = 0

    var eampm = 0
    var ehour = 0
    var eminute = 0
    var eswitch = 0

    var nampm = 0
    var nhour = 0
    var nminute = 0
    var nswitch = 0
    fun resetAlarmData() {
        this.name = ""
        this.period = 1
        this.expired = 0
        this.mampm = 0
        this.mhour = 0
        this.mminute = 0
        this.mswitch = 0
        this.aampm = 0
        this.ahour = 0
        this.aminute = 0
        this.aswitch = 0
        this.eampm = 0
        this.ehour = 0
        this.eminute = 0
        this.eswitch = 0
        this.nampm = 0
        this.nhour = 0
        this.nminute = 0
        this.nswitch = 0
    }
}
