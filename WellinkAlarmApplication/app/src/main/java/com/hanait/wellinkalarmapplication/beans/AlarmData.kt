package com.hanait.wellinkalarmapplication.beans

data class AlarmData(var date: String = "",
                 var name: String = "",
                 var period: Int = 1,
                 var expired: Int = 0,

                 var mampm: Int = 0,
                 var mhour: Int = 7,
                 var mminute: Int = 0,
                 var mswitch: Int = 0,
                 var mtaken: Int = 0,

                 var aampm: Int = 1,
                 var ahour: Int = 12,
                 var aminute: Int = 30,
                 var aswitch: Int = 0,
                 var ataken: Int = 0,

                 var eampm: Int = 1,
                 var ehour: Int = 6,
                 var eminute: Int = 30,
                 var eswitch: Int = 0,
                 var etaken: Int = 0,

                 var nampm: Int = 1,
                 var nhour: Int = 10,
                 var nminute: Int = 30,
                 var nswitch: Int = 0,
                 var ntaken: Int = 0,) {
}
