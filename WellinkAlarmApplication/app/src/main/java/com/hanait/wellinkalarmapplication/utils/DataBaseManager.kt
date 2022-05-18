package com.hanait.wellinkalarmapplication.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList

class DatabaseManager(context: Context?, name: String?, factory: CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE SCHEDULE(code INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, title TEXT, contents TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
//    fun insert(schedules: Schedules) {
//        val db = writableDatabase
//        db.execSQL("insert into SCHEDULE values(null, '" + schedules.date.toString() + "', '" + schedules.title.toString() + "', '" + schedules.contents.toString() + "');")
//        db.close()
//    }
//
//    fun delete(schedules: Schedules) {
//        val db = writableDatabase
//        db.execSQL("delete from SCHEDULE where code = '" + schedules.code.toString() + "';")
//    }
//
//    fun select(schedules: Schedules): ArrayList<Schedules> {
//        val db = readableDatabase
//        val list: ArrayList<Schedules> = ArrayList<Schedules>()
//        val str = ""
//        val cursor = db.rawQuery(
//            "select * from SCHEDULE where date = '" + schedules.date.toString() + "'",
//            null
//        )
//        while (cursor.moveToNext()) {
//            val schedules1 = Schedules()
//            schedules1.code = cursor.getString(0)
//            schedules1.date = cursor.getString(1)
//            schedules1.title = cursor.getString(2)
//            schedules1.contents = cursor.getString(3)
//            list.add(schedules1)
//        }
//        return list
//    }
}
