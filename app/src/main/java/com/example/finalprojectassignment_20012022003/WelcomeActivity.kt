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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatButton

class WelcomeActivity : AppCompatActivity() {
    private lateinit var signupBtn : AppCompatButton
    private lateinit var signinBtn : AppCompatButton

    private lateinit var signupBtnAnimation : Animation
    private lateinit var signinBtnAnimation : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val sharedPrefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val userId = sharedPrefs.getInt("user_id",-1)
        if(userId!=-1){
            Handler().postDelayed({
                val intent = Intent(this, DashBoardActivity::class.java)
                startActivity(intent)
                finish()
            }, 0)
        }

        signupBtn = findViewById(R.id.welcome_signup_btn)
        signinBtn = findViewById(R.id.welcome_signin_btn)

        signupBtnAnimation = AnimationUtils.loadAnimation(this,R.anim.welcome_btn_left_translate)
        signinBtnAnimation = AnimationUtils.loadAnimation(this,R.anim.welcome_btn_right_translate)

        signupBtn.startAnimation(signupBtnAnimation)
        signinBtn.startAnimation(signinBtnAnimation)

        setStatusBarColor(Color.TRANSPARENT)

        signupBtn.setOnClickListener {
            Intent(this,SignupActivity::class.java).apply {
                startActivity(this)
            }
        }

        signinBtn.setOnClickListener {
            Intent(this,SigninActivity::class.java).apply {
                startActivity(this)
            }
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