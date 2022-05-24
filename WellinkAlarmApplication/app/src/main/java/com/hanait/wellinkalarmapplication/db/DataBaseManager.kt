package com.hanait.wellinkalarmapplication.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.hanait.wellinkalarmapplication.beans.AlarmData

class DatabaseManager(context: Context, fileName: String) :
    SQLiteOpenHelper(context, fileName, null, version) {
    companion object {
        const val version = 1
        var INSTANCE: DatabaseManager? = null
        fun getInstance(context: Context, fileName: String): DatabaseManager {
            if (INSTANCE == null) {
                INSTANCE = DatabaseManager(context, fileName)
            }
            return INSTANCE!! }
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE ALARMS(name TEXT PRIMARY KEY, period INTEGER, expired INTEGER, " +
                "mampm INTEGER, mhour INTEGER, mminute INTEGER, mswitch INTEGER," +
                "aampm INTEGER, ahour INTEGER, aminute INTEGER, aswitch INTEGER," +
                "eampm INTEGER, ehour INTEGER, eminute INTEGER, eswitch INTEGER," +
                "nampm INTEGER, nhour INTEGER, nminute INTEGER, nswitch INTEGER);")
        Log.d("로그", "DatabaseManager - insert : DB 생성")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insert(alarmData: AlarmData) {
        val db = writableDatabase
//        db.execSQL("insert into ALARMS values('" + alarmData.name + "','" + alarmData.period.toString() + "', '" + alarmData.expired.toString() + "','"
//                + alarmData.mampm.toString() + "', '" + alarmData.mhour.toString() + "','" + alarmData.mminute + "','" + alarmData.mswitch + "','"
//                + alarmData.aampm.toString() + "', '" + alarmData.ahour.toString() + "','" + alarmData.aminute + "','" + alarmData.aswitch + "','"
//                + alarmData.eampm.toString() + "', '" + alarmData.ehour.toString() + "','" + alarmData.eminute + "','" + alarmData.eswitch + "','"
//                + alarmData.nampm.toString() + "', '" + alarmData.nhour.toString() + "','" + alarmData.nminute + "','" + alarmData.nswitch + "');")

        db.execSQL("insert into ALARMS values('" + alarmData.name + "'," + alarmData.period.toString() + "," + alarmData.expired.toString() + ","
                + alarmData.mampm.toString() + "," + alarmData.mhour.toString() + "," + alarmData.mminute + "," + alarmData.mswitch + ","
                + alarmData.aampm.toString() + "," + alarmData.ahour.toString() + "," + alarmData.aminute + "," + alarmData.aswitch + ","
                + alarmData.eampm.toString() + "," + alarmData.ehour.toString() + "," + alarmData.eminute + "," + alarmData.eswitch + ","
                + alarmData.nampm.toString() + "," + alarmData.nhour.toString() + "," + alarmData.nminute + "," + alarmData.nswitch + ");")
        db.close()
        Log.d("로그", "DatabaseManager - insert : DB에 알람 저장됨")
    }
//
//    fun delete(schedules: Schedules) {
//        val db = writableDatabase
//        db.execSQL("delete from SCHEDULE where code = '" + schedules.code.toString() + "';")
//    }
//

    //모든 알람 데이터 가져오기
    @SuppressLint("Recycle")
    fun selectAll(): ArrayList<AlarmData> {
        val db = readableDatabase
        val list: ArrayList<AlarmData> = ArrayList()
        val cursor = db.rawQuery(
            "select * from ALARMS",
            null
        )
        while (cursor.moveToNext()) {
            val mAlarmData = AlarmData()
            mAlarmData.name = cursor.getString(0)
            mAlarmData.period = cursor.getInt(1)
            mAlarmData.expired = cursor.getInt(2)
            mAlarmData.mampm = cursor.getInt(3)
            mAlarmData.mhour = cursor.getInt(4)
            mAlarmData.mminute = cursor.getInt(5)
            mAlarmData.mswitch = cursor.getInt(6)
            mAlarmData.aampm = cursor.getInt(7)
            mAlarmData.ahour = cursor.getInt(8)
            mAlarmData.aminute = cursor.getInt(9)
            mAlarmData.aswitch = cursor.getInt(10)
            mAlarmData.eampm = cursor.getInt(11)
            mAlarmData.ehour = cursor.getInt(12)
            mAlarmData.eminute = cursor.getInt(13)
            mAlarmData.eswitch = cursor.getInt(14)
            mAlarmData.nampm = cursor.getInt(15)
            mAlarmData.nhour = cursor.getInt(16)
            mAlarmData.nminute = cursor.getInt(17)
            mAlarmData.nswitch = cursor.getInt(18)
            list.add(mAlarmData)
        }
        return list
    }

    //특정 알람 데이터 가져오기
    @SuppressLint("Recycle")
    fun select(alarmDataName: String): AlarmData {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "select * from ALARMS where name = '$alarmDataName'",
            null
        )
        val mAlarmData = AlarmData()
        while (cursor.moveToNext()) {
            mAlarmData.name = cursor.getString(0)
            mAlarmData.period = cursor.getInt(1)
            mAlarmData.expired = cursor.getInt(2)
            mAlarmData.mampm = cursor.getInt(3)
            mAlarmData.mhour = cursor.getInt(4)
            mAlarmData.mminute = cursor.getInt(5)
            mAlarmData.mswitch = cursor.getInt(6)
            mAlarmData.aampm = cursor.getInt(7)
            mAlarmData.ahour = cursor.getInt(8)
            mAlarmData.aminute = cursor.getInt(9)
            mAlarmData.aswitch = cursor.getInt(10)
            mAlarmData.eampm = cursor.getInt(11)
            mAlarmData.ehour = cursor.getInt(12)
            mAlarmData.eminute = cursor.getInt(13)
            mAlarmData.eswitch = cursor.getInt(14)
            mAlarmData.nampm = cursor.getInt(15)
            mAlarmData.nhour = cursor.getInt(16)
            mAlarmData.nminute = cursor.getInt(17)
            mAlarmData.nswitch = cursor.getInt(18)
        }
        return mAlarmData
    }

    //알람 삭제하기
    fun delete(alarmData: AlarmData) {
        val db = writableDatabase
        db.execSQL("delete from ALARMS where name = '" + alarmData.name + "';")
    }

}
