package com.example.finalprojectassignment_20012022003

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var textViewSplash : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setStatusBarColor(Color.TRANSPARENT)

        lottieAnimationView = findViewById(R.id.lottie_animation_view)
        textViewSplash = findViewById(R.id.app_name_splash)

        lottieAnimationView.animate().scaleX(1.5f).scaleY(1.5f).setStartDelay(1500).duration = 1500

        val sharedPrefs : SharedPreferences = getSharedPreferences("FirstTimeCheck", Context.MODE_PRIVATE)

        if(sharedPrefs.getBoolean("isFirstTime",true)) {

            Handler().postDelayed({
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
        else{
            Handler().postDelayed({
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 4000)
        }
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