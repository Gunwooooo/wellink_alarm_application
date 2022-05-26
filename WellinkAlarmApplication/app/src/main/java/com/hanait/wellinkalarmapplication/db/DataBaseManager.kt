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
        db.execSQL("CREATE TABLE ALARMS(date TEXT, name TEXT PRIMARY KEY, period INTEGER, expired INTEGER, " +
                "mampm INTEGER, mhour INTEGER, mminute INTEGER, mswitch INTEGER, mtaken INTEGER," +
                "aampm INTEGER, ahour INTEGER, aminute INTEGER, aswitch INTEGER, ataken INTEGER," +
                "eampm INTEGER, ehour INTEGER, eminute INTEGER, eswitch INTEGER, etaken INTEGER," +
                "nampm INTEGER, nhour INTEGER, nminute INTEGER, nswitch INTEGER, ntaken INTEGER);")
        Log.d("로그", "DatabaseManager - insert : DB 생성")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insertAlarm(alarmData: AlarmData) {
        val db = writableDatabase
        db.execSQL("insert into ALARMS values(datetime('now', 'localtime'),'" + alarmData.name + "'," + alarmData.period + "," + alarmData.expired + ","
                + alarmData.mampm + "," + alarmData.mhour + "," + alarmData.mminute + "," + alarmData.mswitch + "," + alarmData.mtaken + ","
                + alarmData.aampm + "," + alarmData.ahour + "," + alarmData.aminute + "," + alarmData.aswitch + "," + alarmData.ataken + ","
                + alarmData.eampm + "," + alarmData.ehour + "," + alarmData.eminute + "," + alarmData.eswitch + "," + alarmData.etaken + ","
                + alarmData.nampm + "," + alarmData.nhour + "," + alarmData.nminute + "," + alarmData.nswitch + "," + alarmData.ntaken + ");")
        db.close()
        Log.d("로그", "DatabaseManager - insert : DB에 알람 저장됨")
    }

    //모든 알람 데이터 가져오기
    @SuppressLint("Recycle", "Range")
    fun selectAlarmAll(): ArrayList<AlarmData> {
        val db = readableDatabase
        val list: ArrayList<AlarmData> = ArrayList()
        val cursor = db.rawQuery(
            "select * from ALARMS",
            null
        )
        while (cursor.moveToNext()) {
            val mAlarmData = AlarmData()
            mAlarmData.date = cursor.getString(cursor.getColumnIndex("date"))
            mAlarmData.name = cursor.getString(cursor.getColumnIndex("name"))
            mAlarmData.period = cursor.getInt((cursor.getColumnIndex("period")))
            mAlarmData.expired = cursor.getInt((cursor.getColumnIndex("expired")))
            mAlarmData.mampm = cursor.getInt((cursor.getColumnIndex("mampm")))
            mAlarmData.mhour = cursor.getInt((cursor.getColumnIndex("mhour")))
            mAlarmData.mminute = cursor.getInt((cursor.getColumnIndex("mminute")))
            mAlarmData.mswitch = cursor.getInt((cursor.getColumnIndex("mswitch")))
            mAlarmData.mtaken = cursor.getInt((cursor.getColumnIndex("mtaken")))
            mAlarmData.aampm = cursor.getInt((cursor.getColumnIndex("aampm")))
            mAlarmData.ahour = cursor.getInt((cursor.getColumnIndex("ahour")))
            mAlarmData.aminute = cursor.getInt((cursor.getColumnIndex("aminute")))
            mAlarmData.aswitch = cursor.getInt((cursor.getColumnIndex("aswitch")))
            mAlarmData.ataken = cursor.getInt((cursor.getColumnIndex("ataken")))
            mAlarmData.eampm = cursor.getInt((cursor.getColumnIndex("eampm")))
            mAlarmData.ehour = cursor.getInt((cursor.getColumnIndex("ehour")))
            mAlarmData.eminute = cursor.getInt((cursor.getColumnIndex("eminute")))
            mAlarmData.eswitch = cursor.getInt((cursor.getColumnIndex("eswitch")))
            mAlarmData.etaken = cursor.getInt((cursor.getColumnIndex("etaken")))
            mAlarmData.nampm = cursor.getInt((cursor.getColumnIndex("nampm")))
            mAlarmData.nhour = cursor.getInt((cursor.getColumnIndex("nhour")))
            mAlarmData.nminute = cursor.getInt((cursor.getColumnIndex("nminute")))
            mAlarmData.nswitch = cursor.getInt((cursor.getColumnIndex("nswitch")))
            mAlarmData.ntaken = cursor.getInt((cursor.getColumnIndex("ntaken")))
            list.add(mAlarmData)
        }
        return list
    }

    //특정 알람 데이터 가져오기
    @SuppressLint("Range")

    fun selectAlarm(alarmDataName: String): AlarmData {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "select * from ALARMS where name = '$alarmDataName'",
            null
        )
        val mAlarmData = AlarmData()
        while (cursor.moveToNext()) {
            mAlarmData.date = cursor.getString(cursor.getColumnIndex("date"))
            mAlarmData.name = cursor.getString(cursor.getColumnIndex("name"))
            mAlarmData.period = cursor.getInt((cursor.getColumnIndex("period")))
            mAlarmData.expired = cursor.getInt((cursor.getColumnIndex("expired")))
            mAlarmData.mampm = cursor.getInt((cursor.getColumnIndex("mampm")))
            mAlarmData.mhour = cursor.getInt((cursor.getColumnIndex("mhour")))
            mAlarmData.mminute = cursor.getInt((cursor.getColumnIndex("mminute")))
            mAlarmData.mswitch = cursor.getInt((cursor.getColumnIndex("mswitch")))
            mAlarmData.mtaken = cursor.getInt((cursor.getColumnIndex("mtaken")))
            mAlarmData.aampm = cursor.getInt((cursor.getColumnIndex("aampm")))
            mAlarmData.ahour = cursor.getInt((cursor.getColumnIndex("ahour")))
            mAlarmData.aminute = cursor.getInt((cursor.getColumnIndex("aminute")))
            mAlarmData.aswitch = cursor.getInt((cursor.getColumnIndex("aswitch")))
            mAlarmData.ataken = cursor.getInt((cursor.getColumnIndex("ataken")))
            mAlarmData.eampm = cursor.getInt((cursor.getColumnIndex("eampm")))
            mAlarmData.ehour = cursor.getInt((cursor.getColumnIndex("ehour")))
            mAlarmData.eminute = cursor.getInt((cursor.getColumnIndex("eminute")))
            mAlarmData.eswitch = cursor.getInt((cursor.getColumnIndex("eswitch")))
            mAlarmData.etaken = cursor.getInt((cursor.getColumnIndex("etaken")))
            mAlarmData.nampm = cursor.getInt((cursor.getColumnIndex("nampm")))
            mAlarmData.nhour = cursor.getInt((cursor.getColumnIndex("nhour")))
            mAlarmData.nminute = cursor.getInt((cursor.getColumnIndex("nminute")))
            mAlarmData.nswitch = cursor.getInt((cursor.getColumnIndex("nswitch")))
            mAlarmData.ntaken = cursor.getInt((cursor.getColumnIndex("ntaken")))
        }
        return mAlarmData
    }

    //알람 삭제하기
    fun deleteAlarm(alarmData: AlarmData) {
        val db = writableDatabase
        db.execSQL("delete from ALARMS where name = '" + alarmData.name + "';")
    }

    @SuppressLint("Range", "Recycle")
    fun selectCalendarItemAlarm(clickedDate: String): ArrayList<AlarmData> {
        val db = readableDatabase
        val list: ArrayList<AlarmData> = ArrayList()
        val cursor = db.rawQuery(
            "select * from ALARMS " +
                    "where (strftime('%s', clickedDate) - strftime('%s', date) > 0)" +
                    "and (expired != 0 and (strftime('%s', clickedDate) - (strftime('%s', date) + expired) < 0))",
            null
        )
        while (cursor.moveToNext()) {
            val mAlarmData = AlarmData()
            mAlarmData.date = cursor.getString(cursor.getColumnIndex("date"))
            mAlarmData.name = cursor.getString(cursor.getColumnIndex("name"))
            mAlarmData.period = cursor.getInt((cursor.getColumnIndex("period")))
            mAlarmData.expired = cursor.getInt((cursor.getColumnIndex("expired")))
            mAlarmData.mampm = cursor.getInt((cursor.getColumnIndex("mampm")))
            mAlarmData.mhour = cursor.getInt((cursor.getColumnIndex("mhour")))
            mAlarmData.mminute = cursor.getInt((cursor.getColumnIndex("mminute")))
            mAlarmData.mswitch = cursor.getInt((cursor.getColumnIndex("mswitch")))
            mAlarmData.mtaken = cursor.getInt((cursor.getColumnIndex("mtaken")))
            mAlarmData.aampm = cursor.getInt((cursor.getColumnIndex("aampm")))
            mAlarmData.ahour = cursor.getInt((cursor.getColumnIndex("ahour")))
            mAlarmData.aminute = cursor.getInt((cursor.getColumnIndex("aminute")))
            mAlarmData.aswitch = cursor.getInt((cursor.getColumnIndex("aswitch")))
            mAlarmData.ataken = cursor.getInt((cursor.getColumnIndex("ataken")))
            mAlarmData.eampm = cursor.getInt((cursor.getColumnIndex("eampm")))
            mAlarmData.ehour = cursor.getInt((cursor.getColumnIndex("ehour")))
            mAlarmData.eminute = cursor.getInt((cursor.getColumnIndex("eminute")))
            mAlarmData.eswitch = cursor.getInt((cursor.getColumnIndex("eswitch")))
            mAlarmData.etaken = cursor.getInt((cursor.getColumnIndex("etaken")))
            mAlarmData.nampm = cursor.getInt((cursor.getColumnIndex("nampm")))
            mAlarmData.nhour = cursor.getInt((cursor.getColumnIndex("nhour")))
            mAlarmData.nminute = cursor.getInt((cursor.getColumnIndex("nminute")))
            mAlarmData.nswitch = cursor.getInt((cursor.getColumnIndex("nswitch")))
            mAlarmData.ntaken = cursor.getInt((cursor.getColumnIndex("ntaken")))
            list.add(mAlarmData)
        }
        return list
    }
}
