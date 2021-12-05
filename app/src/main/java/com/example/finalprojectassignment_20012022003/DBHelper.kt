package com.example.finalprojectassignment_20012022003

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION) {
    val pContext = context
    companion object{
        val DB_NAME = "final_assignment_project.db"
        val DB_VERSION = 1

        /* User Table */

        //Column Names
        val COL_USER_ID = "_id"
        val COL_USER_NAME = "_name"
        val COL_USER_EMAIL = "_email"
        val COL_USER_PASSWORD = "_password"

        val user_columns = arrayOf(
            COL_USER_ID,
            COL_USER_EMAIL,
            COL_USER_PASSWORD,
            COL_USER_NAME
        )

        val USER_TABLE = "user"

        private val USER_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + USER_TABLE +
                        "(" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_USER_EMAIL + " TEXT NOT NULL," +
                        COL_USER_PASSWORD + " TEXT NOT NULL," +
                        COL_USER_NAME + " TEXT NOT NULL" +
                        ");"


        private val USER_TABLE_DROP = "DROP TABLE $USER_TABLE"

        /* User Table */


        /* Workout */

        //Column Names
        val COLUMN_WORKOUT_ID = "_id"
        val COL_WORKOUT_NAME = "_name"
        val COL_WORKOUT_IMAGE = "_image"
        val COL_WORKOUT_MUSIC = "_music"
        val COL_WORKOUT_MINUTE = "_minute"
        val COL_BURNED_CALORIE = "_calorie"

        val workout_columns = arrayOf(
            COLUMN_WORKOUT_ID,
            COL_WORKOUT_NAME,
            COL_WORKOUT_IMAGE,
            COL_WORKOUT_MUSIC,
            COL_WORKOUT_MINUTE,
            COL_BURNED_CALORIE
        )

        val WORKOUT_TABLE = "workout"

        private val WORKOUT_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + WORKOUT_TABLE +
                        "(" +
                        COLUMN_WORKOUT_ID + " TEXT PRIMARY KEY, " +
                        COL_WORKOUT_NAME + " TEXT NOT NULL, " +
                        COL_WORKOUT_IMAGE + " TEXT NOT NULL, " +
                        COL_WORKOUT_MUSIC + " INTEGER, " +
                        COL_WORKOUT_MINUTE + " INTEGER, " +
                        COL_BURNED_CALORIE + " INTEGER " +
                        ");"

        private val WORKOUT_TABLE_DROP = "DROP TABLE $WORKOUT_TABLE"

        /* Workout */

        /* Activity */

        //Column Names
        val COL_ACTIVITY_ID = "_id"
        val COL_WORKOUT_ID = "_workout_id"
        val COL_USER_ACTIVITY_ID = "_user_id"
        val COL_ACTIVITY_DATE = "_date"

        val activity_columns = arrayOf(
            COL_ACTIVITY_ID,
            COL_WORKOUT_ID,
            COL_USER_ACTIVITY_ID,
            COL_ACTIVITY_DATE
        )

        val ACTIVITY_TABLE = "activity"

        private val ACTIVITY_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ACTIVITY_TABLE +
                    "(" +
                    COL_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_WORKOUT_ID + " TEXT NOT NULL," +
                    COL_USER_ACTIVITY_ID + " TEXT NOT NULL," +
                    COL_ACTIVITY_DATE + " TEXT NOT NULL " +
                    ");"


        private val ACTIVITY_TABLE_DROP = "DROP TABLE $ACTIVITY_TABLE"

        /* Activity */
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(USER_TABLE_CREATE)
        db?.execSQL(ACTIVITY_TABLE_CREATE)
        db?.execSQL(WORKOUT_TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(USER_TABLE_DROP)
        db?.execSQL(ACTIVITY_TABLE_DROP)
        db?.execSQL(WORKOUT_TABLE_DROP)
        onCreate(db)
    }

}