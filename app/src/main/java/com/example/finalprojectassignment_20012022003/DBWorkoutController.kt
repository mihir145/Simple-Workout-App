package com.example.finalprojectassignment_20012022003

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast

class DBWorkoutController(context : Context) {

    private var dbHelper: DBHelper
    private lateinit var database: SQLiteDatabase
    private val pContext = context

    init{
        dbHelper = DBHelper(context)
    }

    fun close(){
        dbHelper.close()
    }

    fun getMusicRawId(id:Int):Int{
        when(id){
            1 ->{
                return R.raw.first
            }
            2 ->{
                return R.raw.second
            }
            3 ->{
                return R.raw.third
            }
            4 ->{
                return R.raw.fourth
            }
            5 ->{
                return R.raw.fifth
            }
        }
        return 0
    }

    fun getMusicId(id:Int):Int{
        when(id){
            R.raw.first ->{
                return 1
            }
            R.raw.second ->{
                return 2
            }
            R.raw.third ->{
                return 3
            }
            R.raw.fourth ->{
                return 4
            }
            R.raw.fifth ->{
                return 5
            }
        }
        return 0
    }

    fun addWorkouts() {
        val arrayList = ArrayList<Workout>()
        database = dbHelper.writableDatabase

        arrayList.add(Workout("Workout-1","https://d205bpvrqc9yn1.cloudfront.net/0001.gif",R.raw.first,false,2,30))
        arrayList.add(Workout("Workout-2","https://d205bpvrqc9yn1.cloudfront.net/0002.gif",R.raw.second,false,2,40))
        arrayList.add(Workout("Workout-3","https://d205bpvrqc9yn1.cloudfront.net/0003.gif",R.raw.third,false,3,60))
        arrayList.add(Workout("Workout-4","https://d205bpvrqc9yn1.cloudfront.net/0030.gif",R.raw.fourth,false,3,70))
        arrayList.add(Workout("Workout-5","https://d205bpvrqc9yn1.cloudfront.net/0090.gif",R.raw.fifth,false,3,50))
        arrayList.add(Workout("Workout-6","https://d205bpvrqc9yn1.cloudfront.net/0006.gif",R.raw.first,false,2,40))
        arrayList.add(Workout("Workout-7","https://d205bpvrqc9yn1.cloudfront.net/0007.gif",R.raw.second,false,2,40))
        arrayList.add(Workout("Workout-8","https://d205bpvrqc9yn1.cloudfront.net/0101.gif",R.raw.fifth,false,2,40))
        arrayList.add(Workout("Workout-9","https://d205bpvrqc9yn1.cloudfront.net/0106.gif",R.raw.fourth,false,1,30))
        arrayList.add(Workout("Workout-10","https://d205bpvrqc9yn1.cloudfront.net/0108.gif",R.raw.third,false,1,30))

        database = dbHelper.writableDatabase
        for(i in 0 until arrayList.size) {

            val values = ContentValues()

            values.put(DBHelper.COLUMN_WORKOUT_ID,arrayList[i].id)
            values.put(DBHelper.COL_WORKOUT_NAME,arrayList[i].name)
            values.put(DBHelper.COL_BURNED_CALORIE,arrayList[i].burnedCalorie)
            values.put(DBHelper.COL_WORKOUT_MINUTE,arrayList[i].minute)
            values.put(DBHelper.COL_WORKOUT_IMAGE,arrayList[i].imageString)
            values.put(DBHelper.COL_WORKOUT_MUSIC,getMusicId(arrayList[i].music))

            database.insert(DBHelper.WORKOUT_TABLE, null, values)
        }
        database.close()
    }

    fun getWorkouts():ArrayList<Workout>{
            val db = dbHelper.writableDatabase
            val arrayList = ArrayList<Workout>()
            // Select All Query
            val selectQuery = "SELECT * FROM " + DBHelper.WORKOUT_TABLE

            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    val workout = Workout(
                        cursor.getString(1),
                        cursor.getString(2),
                        getMusicRawId(cursor.getInt(3)),
                        false,
                        cursor.getInt(4),
                        cursor.getInt(5)
                    )
                    workout.id = cursor.getString(0)
                    Log.e("Fetched ",workout.id)
                    arrayList.add(workout)

                } while (cursor.moveToNext())
            }
            return arrayList
    }

    fun getTotalCalorie():Int{
        database = dbHelper.readableDatabase

        val query = "SELECT SUM(${DBHelper.COL_BURNED_CALORIE}) FROM ${DBHelper.WORKOUT_TABLE};"
        val cursor = database.rawQuery(query,null)
        cursor.moveToFirst()
        val total = cursor.getInt(0)
        database.close()
        return total
    }

    fun getWorkout(workoutId: String):Workout{
        database = dbHelper.readableDatabase

        val query = "SELECT * FROM ${DBHelper.WORKOUT_TABLE} WHERE ${DBHelper.COLUMN_WORKOUT_ID}=${workoutId};"
        val cursor = database.rawQuery(query,null)
        cursor.moveToFirst()

        val workout = Workout(
            cursor.getString(1),
            cursor.getString(2),
            getMusicRawId(cursor.getInt(3)),
            false,
            cursor.getInt(4),
            cursor.getInt(5)
        )
        workout.id = cursor.getString(0)
        database.close()
        return workout
    }

}