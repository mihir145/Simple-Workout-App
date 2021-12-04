package com.example.finalprojectassignment_20012022003

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast

class DBActivityController(context: Context) {
    private var dbHelper: DBHelper
    private lateinit var database: SQLiteDatabase
    private val pContext = context

    init{
        dbHelper = DBHelper(context)
    }

    fun close(){
        dbHelper.close()
    }

    fun addActivity(userId : Int, workoutId: String,date:String){
        database = dbHelper.writableDatabase

        val values = ContentValues()

        values.put(DBHelper.COL_USER_ACTIVITY_ID,userId)
        values.put(DBHelper.COL_WORKOUT_ID,workoutId)
        values.put(DBHelper.COL_ACTIVITY_DATE,date)

        database.insert(DBHelper.ACTIVITY_TABLE, null, values)
        database.close()
    }

    fun getActivities(id:Int) : HashMap<String,ArrayList<String>>{
        val db = dbHelper.writableDatabase
        val hashMap = HashMap<String,ArrayList<String>>()

        // Select All Query
        val selectQuery = "SELECT ${DBHelper.COL_WORKOUT_ID},${DBHelper.COL_ACTIVITY_DATE} FROM " + DBHelper.ACTIVITY_TABLE + " ?=?"

        val cursor = db.rawQuery(selectQuery, arrayOf(DBHelper.COL_USER_ACTIVITY_ID,id.toString()))

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(!hashMap.containsKey(cursor.getString(1))){
                    hashMap[cursor.getString(1)] = ArrayList()
                }

                hashMap[cursor.getString(1)]?.add(cursor.getString(0))

            } while (cursor.moveToNext())
        }
        return hashMap
    }
}