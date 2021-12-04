package com.example.finalprojectassignment_20012022003

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.CalendarContract
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.marginBottom
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.text.SimpleDateFormat
import java.util.*

class DashBoardActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var scrollView : ScrollView
    private lateinit var usernameTextView : TextView
    private lateinit var emailTextView : TextView
    private lateinit var workoutTextview1 : TextView
    private lateinit var workoutTextview2 : TextView
    private lateinit var workoutTextview3 : TextView
    private lateinit var seeMoreBtn : AppCompatButton
    private lateinit var calorieBurnedTextView: TextView
    private lateinit var logoutTextView: TextView

    val dbActivityController = DBActivityController(this)
    val dbWorkoutController = DBWorkoutController(this)

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        WorkoutActivity.arrayList = dbWorkoutController.getWorkouts()

        val sharedPrefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val userId = sharedPrefs.getInt("user_id",-1)
        if(userId==-1){
            Handler().postDelayed({
                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }, 0)
        }

        val dbUserController = DBUserController(this)
        val user = dbUserController.getUser(userId)

        val dbWorkoutActivity = DBWorkoutController(this)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        scrollView = findViewById(R.id.scrollView)
        usernameTextView = findViewById(R.id.dashboard_username)
        emailTextView = findViewById(R.id.dashboard_email)
        workoutTextview1 = findViewById(R.id.workout_textview_1)
        workoutTextview2 = findViewById(R.id.workout_textview_2)
        workoutTextview3 = findViewById(R.id.workout_textview_3)
        seeMoreBtn = findViewById(R.id.see_more_btn)
        calorieBurnedTextView = findViewById(R.id.calorie_burned_textView1)
        logoutTextView = findViewById(R.id.dashboard_logout)

        logoutTextView.setOnClickListener {
            val editor = sharedPrefs.edit()
            editor.remove("user_id")
            editor.apply()
            Handler().postDelayed({
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 0)
        }


        val  sdf = SimpleDateFormat("yyyy-MM-dd")

        val circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)

        val currentCalorie = getCurrentCalorieBurned(sdf.format(Date()))
        val totalCalorie = dbWorkoutActivity.getTotalCalorie()
        calorieBurnedTextView.text = "$currentCalorie / $totalCalorie Cal. Burned"
        // Set Init progress with animation
        circularProgressBar.setProgressWithAnimation(currentCalorie.toFloat(), 1000)
        circularProgressBar.progressMax = totalCalorie.toFloat()

        usernameTextView.text = user.getUsername()
        emailTextView.text = user.getEmail()

        setStatusBarColor(Color.TRANSPARENT)

        setRecentWorkout()

        bottomNavigationView.setOnItemSelectedListener {
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
    }

    @SuppressLint("SetTextI18n")
    private fun setRecentWorkout() {
        val sharedPreferences = getSharedPreferences("recent_workout",Context.MODE_PRIVATE)


        if(sharedPreferences.getInt("first",-1)==1){
            workoutTextview1.text = "Not Done Any Workout Yet"
            seeMoreBtn.visibility = View.INVISIBLE
            return
        }

        if(sharedPreferences.getInt("first",-1)!=-1){
            val workout = WorkoutActivity.arrayList[sharedPreferences.getInt("first",0)]
            workoutTextview1.text = "${workout.name}, Calorie Burned-${workout.burnedCalorie}"
            seeMoreBtn.visibility = View.INVISIBLE
        }

        if(sharedPreferences.getInt("second",-1)!=-1){
            val workout = WorkoutActivity.arrayList[sharedPreferences.getInt("second",0)]
            workoutTextview2.text = "${workout.name}, Calorie Burned-${workout.burnedCalorie}"
            seeMoreBtn.visibility = View.INVISIBLE
        }

        if(sharedPreferences.getInt("third",-1)!=-1){
            val workout = WorkoutActivity.arrayList[sharedPreferences.getInt("third",0)]
            workoutTextview3.text = "${workout.name}, Calorie Burned-${workout.burnedCalorie}"
            seeMoreBtn.visibility = View.VISIBLE
        }
    }

    private fun getCurrentCalorieBurned(date:String):Int{
        val sharedPreferences = getSharedPreferences("calorie_burned",Context.MODE_PRIVATE)
        return sharedPreferences.getInt(date,0)
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