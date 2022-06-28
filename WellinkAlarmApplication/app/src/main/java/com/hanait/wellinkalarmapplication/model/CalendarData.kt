package com.hanait.wellinkalarmapplication.model

data class CalendarData(
    var id : Int = 0,
    var date: String = "",
    var name: String = "",
    var mtaken: Int = 0,
    var ataken: Int = 0,
    var etaken: Int = 0,
    var ntaken: Int = 0) {
}
