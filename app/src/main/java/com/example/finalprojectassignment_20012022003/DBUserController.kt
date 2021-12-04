package com.example.finalprojectassignment_20012022003

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast

class DBUserController(context: Context) {
    private var dbHelper: DBHelper
    private lateinit var database: SQLiteDatabase
    private val pContext = context

    init{
        dbHelper = DBHelper(context)
    }

    fun close(){
        dbHelper.close()
    }

    fun addUser(user : User){
        database = dbHelper.writableDatabase

        val values = ContentValues()

        values.put(DBHelper.COL_USER_EMAIL,user.getEmail())
        values.put(DBHelper.COL_USER_NAME,user.getUsername())
        values.put(DBHelper.COL_USER_PASSWORD,user.getPassword())

        database.insert(DBHelper.USER_TABLE, null, values)
        database.close()
    }

    fun getUser(id:Int):User{
        database = dbHelper.readableDatabase

        val cursor: Cursor = database.query(
            DBHelper.USER_TABLE,
            DBHelper.user_columns,
            DBHelper.COL_USER_ID + " =?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val user = User(
            cursor.getString(3),
            cursor.getString(1),
            cursor.getString(2)
        )
        user.setId(cursor.getInt(0))
        return user
    }

    fun verifyUser(email : String,password : String) : User? {
        database = dbHelper.readableDatabase

        val cursor: Cursor = database.rawQuery(
            "select * from ${DBHelper.USER_TABLE} where ${DBHelper.COL_USER_EMAIL}=? and ${DBHelper.COL_USER_PASSWORD}=?",
            arrayOf(email, password)
        )

        cursor.moveToFirst()
        if(cursor.count > 0) {
            val user = User(
                cursor.getString(3),
                cursor.getString(1),
                cursor.getString(2)
            )
            user.setId(cursor.getInt(0))
            return user
        }
        database.close()
        return null
    }

}