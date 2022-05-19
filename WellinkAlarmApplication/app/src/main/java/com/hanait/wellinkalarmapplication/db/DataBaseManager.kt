package com.hanait.wellinkalarmapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.hanait.wellinkalarmapplication.beans.AlarmData

class DatabaseManager(context: Context?, name: String?, factory: CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {
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
        db.execSQL("insert into ALARMS values(" + alarmData.name + "','" + alarmData.period.toString() + "', '" + alarmData.expired.toString() + "','"
                + alarmData.mampm.toString() + "', '" + alarmData.mhour.toString() + "','" + alarmData.mminute + "','" + alarmData.mswitch + "','"
                + alarmData.aampm.toString() + "', '" + alarmData.ahour.toString() + "','" + alarmData.aminute + "','" + alarmData.aswitch + "','"
                + alarmData.eampm.toString() + "', '" + alarmData.ehour.toString() + "','" + alarmData.eminute + "','" + alarmData.eswitch + "','"
                + alarmData.nampm.toString() + "', '" + alarmData.nhour.toString() + "','" + alarmData.nminute + "','" + alarmData.nswitch + "','"
                +"');")
        db.close()
        Log.d("로그", "DatabaseManager - insert : DB에 알람 저장됨")
    }
//
//    fun delete(schedules: Schedules) {
//        val db = writableDatabase
//        db.execSQL("delete from SCHEDULE where code = '" + schedules.code.toString() + "';")
//    }
//
//    fun select(alarmData: AlarmData): ArrayList<AlarmData> {
//        val db = readableDatabase
//        val list: ArrayList<AlarmData> = ArrayList()
//
//        val cursor = db.rawQuery(
//            "select * from ALARMS where date = '" + schedules.date.toString() + "'",
//            null
//        )
//        while (cursor.moveToNext()) {
//            val schedules1 = AlarmData()
//            schedules1.code = cursor.getString(0)
//            schedules1.date = cursor.getString(1)
//            schedules1.title = cursor.getString(2)
//            schedules1.contents = cursor.getString(3)
//            list.add(schedules1)
//        }
//        return list
//    }
}
