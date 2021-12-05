package com.example.finalprojectassignment_20012022003

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


class SingleWorkoutActivity : AppCompatActivity() {

    private lateinit var timerTextView : TextView
    private lateinit var imageView : ImageView
    private lateinit var startBtn  : AppCompatButton
    private lateinit var nextBtn  : ImageView
    private lateinit var prevBtn  : ImageView
    private lateinit var downTimer: CountDownTimer
    private lateinit var workoutTitle : TextView
    private lateinit var fadeInAnimation : Animation

    private var start = false
    private var position  = 0
    private var userId = -1

    companion object{
        const val channelId = "notesChannel"
        const val channelName = "Notes Channel"
    }


    val dbActivityController = DBActivityController(this)


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_workout)

        setStatusBarColor(Color.TRANSPARENT)

        val sharedPrefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        userId = sharedPrefs.getInt("user_id",-1)


        fadeInAnimation = AnimationUtils.loadAnimation(this,R.anim.fade_in_image)

        timerTextView = findViewById(R.id.timerTextView)
        imageView = findViewById(R.id.imageView)
        startBtn = findViewById(R.id.startBtn)
        nextBtn = findViewById(R.id.nextBtn)
        prevBtn = findViewById(R.id.prevBtn)
        workoutTitle = findViewById(R.id.workout_title)

        position = intent!!.getIntExtra("position",0)

        refreshComponents()
        setButtons()




        start = false
        startBtn.setOnClickListener {
            WorkoutActivity.arrayList[position].isDone = false
            if(!start) {
                downTimer.start()
                startBtn.text = "Stop"
                startStopService(position,"start")
            }
            else{
                downTimer.cancel()
                timerTextView.text = WorkoutActivity.arrayList[position].minute.toString() +":00"
                startBtn.text = "Start"
                startStopService(position,"stop")
            }
            start = !start
        }

        nextBtn.setOnClickListener {
            downTimer.cancel()
            timerTextView.text = WorkoutActivity.arrayList[position].minute.toString()+":00"
            startBtn.text = "Start"
            startStopService(position,"stop")
            position++

            refreshComponents()
            setButtons()
        }

        prevBtn.setOnClickListener {
            downTimer.cancel()
            timerTextView.text = WorkoutActivity.arrayList[position].minute.toString()+":00"
            startBtn.text="Start"
            startStopService(position,"stop")
            position--

            refreshComponents()
            setButtons()
        }
    }

    private fun startStopService(position : Int, service : String){
        if(service=="start") {
            Intent(applicationContext, BackgroundMusicService::class.java).apply {
                putExtra("service", "play/pause")
                putExtra("position", position)
                startService(this)
            }
        }
        else if(service=="stop"){
            Intent(applicationContext, BackgroundMusicService::class.java).apply {
                putExtra("service", "play/pause")
                putExtra("position", position)
                stopService(this)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshComponents(){
        if(WorkoutActivity.arrayList[position].isDone){
            timerTextView.text = "Done!"
            startBtn.text = "Start Again"
        }
        timerTextView.text = WorkoutActivity.arrayList[position].minute.toString() +":00"
        start = false

        val uri: Uri = Uri.parse(WorkoutActivity.arrayList[position].imageString)
        Glide.with(this).load(uri).into(imageView)
        imageView.startAnimation(fadeInAnimation)

        workoutTitle.text = WorkoutActivity.arrayList[position].name
        startTimer(timerTextView,WorkoutActivity.arrayList[position].minute*60)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setButtons(){
        if(position < WorkoutActivity.arrayList.size-1){
            nextBtn.isEnabled = true
            nextBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_right_24))
        }else{
            nextBtn.isEnabled = false
            nextBtn.setImageDrawable(null)
        }

        if(position>0){
            prevBtn.isEnabled = true
            prevBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_arrow_left_24))
        }else{
            prevBtn.isEnabled = false
            prevBtn.setImageDrawable(null)
        }
    }

    private fun startTimer(textView: TextView, sec: Int) {
        downTimer = object : CountDownTimer((1000 * sec).toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val v = String.format("%02d", millisUntilFinished / 60000)
                val va = (millisUntilFinished % 60000 / 1000).toInt()
                textView.text = "$v:${String.format("%02d", va)}"
            }
            @SuppressLint("SetTextI18n", "UnspecifiedImmutableFlag", "SimpleDateFormat")
            override fun onFinish() {
                downTimer.cancel()
                textView.text = WorkoutActivity.arrayList[position].minute.toString()+":00"
                startBtn.text = "Start"
                startStopService(position,"stop")
                WorkoutActivity.arrayList[position].isDone = true

                val sdf = SimpleDateFormat("yyyy-MM-dd")

                dbActivityController.addActivity(userId,WorkoutActivity.arrayList[position].id,sdf.format(Date()))

                putCalorieBurned(sdf.format(Date()),WorkoutActivity.arrayList[position].burnedCalorie)
                putRecentWorkout()

                if(position!=WorkoutActivity.arrayList.size-1){
                    position++
                }
                refreshComponents()
                setButtons()
            }
        }
    }

    private fun putRecentWorkout(){
        val sharedPreferences = getSharedPreferences("recent_workout",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        when {
            sharedPreferences.getInt("first",-1)==-1 -> {
                editor.putInt("first",position)
            }
            sharedPreferences.getInt("second",-1)==-1 -> {
                editor.putInt("second",position)
            }
            sharedPreferences.getInt("third",-1)==-1 -> {
                editor.putInt("third",position)
            }
            else -> {
                editor.putInt("first",sharedPreferences.getInt("second",0))
                editor.putInt("second",sharedPreferences.getInt("third",0))
                editor.putInt("third",position)
            }
        }
        editor.apply()
    }

    private fun putCalorieBurned(date: String,calorie :Int) {
        val sharedPreferences = getSharedPreferences("calorie_burned",Context.MODE_PRIVATE)
        val editor  = sharedPreferences.edit()
        val sharedPreferenceIds = sharedPreferences.all.map { it.key }
        if(sharedPreferences.getInt(date,0)==0) {
            for (i in sharedPreferenceIds) {
                editor.remove(i)
                editor.apply()
            }
        }
        editor.apply()
        editor.putInt(date,sharedPreferences.getInt(date,0)+calorie)
        editor.apply()
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

    override fun onDestroy() {
        super.onDestroy()
        startStopService(position,"stop")
    }
}