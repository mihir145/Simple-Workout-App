package com.example.finalprojectassignment_20012022003

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.LinearLayoutManager




class WorkoutActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationView
    private lateinit var listView : ListView
    @RequiresApi(Build.VERSION_CODES.O)

    companion object{
        var arrayList = ArrayList<Workout>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val sharedPrefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        if(sharedPrefs.getInt("user_id",-1)==-1){
            Handler().postDelayed({
                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }, 0)
        }

        bottomNavigation = findViewById(R.id.bottom_navigation)
        listView = findViewById(R.id.listView)

        bottomNavigation.selectedItemId = R.id.workouts

        setStatusBarColor(Color.TRANSPARENT)

        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.dashboard -> {
                    Intent(this,DashBoardActivity::class.java).apply {
                        startActivity(this)
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.workouts->{
                    Intent(this,WorkoutActivity::class.java).apply {
                        startActivity(this)
                    }
                    return@setOnItemSelectedListener  true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }

        val dbWorkoutController = DBWorkoutController(this)
        arrayList = dbWorkoutController.getWorkouts()

        val adapter = WorkoutListViewAdapter(this,arrayList)

        listView.adapter = adapter

    }

    private fun setWindowFlag(flagTranslucentStatus: Int, b: Boolean) {
        val winParameters = window.attributes
        if(b){
            winParameters.flags =  winParameters.flags or flagTranslucentStatus
        }
        else{
            winParameters.flags =  winParameters.flags and flagTranslucentStatus.inv()
        }
        window.attributes = winParameters
    }

    private fun setStatusBarColor(color:Int){
        if (Build.VERSION.SDK_INT in 19..20) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if(Build.VERSION.SDK_INT >= 21){
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = color
        }
    }
}