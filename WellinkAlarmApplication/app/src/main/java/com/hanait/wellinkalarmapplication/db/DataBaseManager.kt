package com.hanait.wellinkalarmapplication.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.hanait.wellinkalarmapplication.model.AlarmData

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
        db.execSQL("CREATE TABLE ALARMS(id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, name TEXT, period INTEGER, expired TEXT, expiredInt INTEGER," +
                "mampm INTEGER, mhour INTEGER, mminute INTEGER, mswitch INTEGER, mtaken INTEGER," +
                "aampm INTEGER, ahour INTEGER, aminute INTEGER, aswitch INTEGER, ataken INTEGER," +
                "eampm INTEGER, ehour INTEGER, eminute INTEGER, eswitch INTEGER, etaken INTEGER," +
                "nampm INTEGER, nhour INTEGER, nminute INTEGER, nswitch INTEGER, ntaken INTEGER);")
        Log.d("로그", "DatabaseManager - insert : DB 생성")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun updateAlarm(alarmData: AlarmData, prevName: String) {
        val db = writableDatabase
        db.execSQL("update ALARMS set date=strftime('%Y-%m-%d', 'now', 'localtime'), name='${alarmData.name}', period=${alarmData.period}, " +
                "expired='${alarmData.expired}', expiredInt=${alarmData.expiredInt}, " +
                "mampm=${alarmData.mampm}, mhour=${alarmData.mhour}, mminute=${alarmData.mminute}, mswitch=${alarmData.mswitch}, mtaken=${alarmData.mtaken}," +
                "aampm=${alarmData.aampm}, ahour=${alarmData.ahour}, aminute=${alarmData.aminute}, aswitch=${alarmData.aswitch}, ataken=${alarmData.ataken}," +
                "eampm=${alarmData.eampm}, ehour=${alarmData.ehour}, eminute=${alarmData.eminute}, eswitch=${alarmData.eswitch}, etaken=${alarmData.etaken}," +
                "nampm=${alarmData.nampm}, nhour=${alarmData.nhour}, nminute=${alarmData.nminute}, nswitch=${alarmData.nswitch}, ntaken=${alarmData.ntaken} " +
                "where name='${prevName}';")
        db.close()
        Log.d("로그", "DatabaseManager - insert : DB에 알람 저장됨")
    }

    fun insertAlarm(alarmData: AlarmData) {
        val db = writableDatabase
        db.execSQL("insert into ALARMS ( date, name, period, expired, expiredInt, mampm, mhour, mminute, mswitch, mtaken, aampm, ahour, aminute, aswitch, ataken, eampm, ehour, eminute, eswitch, etaken, nampm, nhour, nminute, nswitch, ntaken) values(strftime('%Y-%m-%d', 'now', 'localtime'),'" + alarmData.name + "'," + alarmData.period + ",'" + alarmData.expired + "'," + alarmData.expiredInt + ","
                + alarmData.mampm + "," + alarmData.mhour + "," + alarmData.mminute + "," + alarmData.mswitch + "," + alarmData.mtaken + ","
                + alarmData.aampm + "," + alarmData.ahour + "," + alarmData.aminute + "," + alarmData.aswitch + "," + alarmData.ataken + ","
                + alarmData.eampm + "," + alarmData.ehour + "," + alarmData.eminute + "," + alarmData.eswitch + "," + alarmData.etaken + ","
                + alarmData.nampm + "," + alarmData.nhour + "," + alarmData.nminute + "," + alarmData.nswitch + "," + alarmData.ntaken + ");")
        db.close()
        Log.d("로그", "DatabaseManager - insert : DB에 알람 저장됨")
    }

    //이름에 해당하는 알람 아이디 가져오기
    @SuppressLint("Recycle", "Range")
    fun selectAlarmIdAsName(name: String) : Int{
        val db = readableDatabase
        val list: ArrayList<Int> = ArrayList()
        val cursor = db.rawQuery("select id from ALARMS where name = '$name'", null)
        while (cursor.moveToNext()) {
            list.add(cursor.getInt((cursor.getColumnIndex("id"))))
        }
        db.close()
        return list[0]
    }

    //Id에 해당하는 알람 정보 가져오기
    @SuppressLint("Recycle", "Range")
    fun selectAlarmAsId(id: Int) : AlarmData{
        val db = readableDatabase
        val list: ArrayList<AlarmData> = ArrayList()
        val cursor = db.rawQuery("select * from ALARMS where id = $id", null)
        while (cursor.moveToNext()) {
            val mAlarmData = AlarmData()
            mAlarmData.id = cursor.getInt((cursor.getColumnIndex("id")))
            mAlarmData.date = cursor.getString(cursor.getColumnIndex("date"))
            mAlarmData.name = cursor.getString(cursor.getColumnIndex("name"))
            mAlarmData.period = cursor.getInt((cursor.getColumnIndex("period")))
            mAlarmData.expired = cursor.getString((cursor.getColumnIndex("expired")))
            mAlarmData.expiredInt = cursor.getInt((cursor.getColumnIndex("expiredInt")))
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
        db.close()
        return list[0]
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
            mAlarmData.id = cursor.getInt((cursor.getColumnIndex("id")))
            mAlarmData.date = cursor.getString(cursor.getColumnIndex("date"))
            mAlarmData.name = cursor.getString(cursor.getColumnIndex("name"))
            mAlarmData.period = cursor.getInt((cursor.getColumnIndex("period")))
            mAlarmData.expired = cursor.getString((cursor.getColumnIndex("expired")))
            mAlarmData.expiredInt = cursor.getInt((cursor.getColumnIndex("expiredInt")))
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
        db.close()
        return list
    }

    //알람 삭제하기
    fun deleteAlarm(alarmData: AlarmData) {
        val db = writableDatabase
        db.execSQL("delete from ALARMS where name = '" + alarmData.name + "';")
        db.close()
    }

    //달력 클릭 시 데이터 가져오기
    @SuppressLint("Range", "Recycle")
    fun selectCalendarItemAlarm(clickedDate: String): ArrayList<AlarmData> {
        Log.d("로그", "DatabaseManager - selectCalendarItemAlarm : $clickedDate")
        val db = readableDatabase
        val list: ArrayList<AlarmData> = ArrayList()
        val cursor = db.rawQuery(
            "select * from ALARMS " +
                    "where (strftime('%s', '$clickedDate') - strftime('%s', date) >= 0)" +
                    "and (expired != '' and (strftime('%s', '$clickedDate') - strftime('%s', expired) < 0))" +
                    "and (((strftime('%d', '$clickedDate') - strftime('%d', date)) % period) == 0)" +
                    "or ((strftime('%s', '$clickedDate') - strftime('%s', date) >= 0) and expired == '')",
            null
        )
        while (cursor.moveToNext()) {
            val mAlarmData = AlarmData()
            mAlarmData.date = cursor.getString(cursor.getColumnIndex("date"))
            mAlarmData.name = cursor.getString(cursor.getColumnIndex("name"))
            mAlarmData.period = cursor.getInt((cursor.getColumnIndex("period")))
            mAlarmData.expired = cursor.getString((cursor.getColumnIndex("expired")))
            mAlarmData.expiredInt = cursor.getInt((cursor.getColumnIndex("expiredInt")))
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
        db.close()
        return list
    }
}
