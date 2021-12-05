package com.example.finalprojectassignment_20012022003

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SigninActivity : AppCompatActivity() {

    private lateinit var signinEmail : TextInputLayout
    private lateinit var signinPassword : TextInputLayout
    private lateinit var signinText : TextView
    private lateinit var signinEmailTextFieldAnimation : Animation
    private lateinit var signinPasswordTextFieldAnimation : Animation
    private lateinit var signinButtonAnimation : Animation
    private lateinit var signinNotAccountTextView : TextView
    private lateinit var signinBtn : AppCompatButton

    private lateinit var signinEmailField : TextInputEditText
    private lateinit var signinPasswordField : TextInputEditText

    private lateinit var loaderImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        setStatusBarColor(Color.TRANSPARENT)

        signinEmail = findViewById(R.id.signin_email_box)
        signinPassword = findViewById(R.id.signin_password_box)
        signinBtn = findViewById(R.id.signin_btn)
        signinText = findViewById(R.id.signin_textView)
        signinEmailField = findViewById(R.id.signin_email)
        signinPassword = findViewById(R.id.signin_password_box)
        signinPasswordField = findViewById(R.id.signin_password)

        signinNotAccountTextView = findViewById(R.id.signin_not_account_textView)

        loaderImage = findViewById(R.id.loader)

        signinEmailTextFieldAnimation = AnimationUtils.loadAnimation(this,R.anim.welcome_btn_left_translate)
        signinPasswordTextFieldAnimation = AnimationUtils.loadAnimation(this,R.anim.welcome_btn_right_translate)
        signinButtonAnimation = AnimationUtils.loadAnimation(this,R.anim.signin_btn_scale)

        signinEmail.startAnimation(signinEmailTextFieldAnimation)
        signinPassword.startAnimation(signinPasswordTextFieldAnimation)
        signinBtn.startAnimation(signinButtonAnimation)
        signinNotAccountTextView.startAnimation(signinButtonAnimation)

        val dbUserController = DBUserController(this)
        val sharedPrefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        signinNotAccountTextView.setOnClickListener {
            Intent(this,SignupActivity::class.java).apply{
                startActivity(this)
            }
        }

        signinBtn.setOnClickListener {
            when {
                signinEmailField.text!!.toString() == "" -> {
                    Toast.makeText(this, "Email Is Required!", Toast.LENGTH_SHORT).show()
                }
                signinPasswordField.text!!.toString() == "" -> {
                    Toast.makeText(this, "Password is Required!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    loaderImage.visibility = View.VISIBLE
                    Glide.with(this).load(R.drawable.loading).into(loaderImage)
                    val user = dbUserController.verifyUser(
                                    signinEmailField.text.toString(),
                                    signinPasswordField.text.toString())
                    if(user!=null){
                        sharedPrefs.edit().putInt("user_id",user.getId()).apply()
                        Handler().postDelayed({
                            val intent = Intent(this, DashBoardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 3000)
                    }
                    else{
                        loaderImage.visibility = View.INVISIBLE
                        signinPasswordField.setText("", TextView.BufferType.EDITABLE)
                        Toast.makeText(this,"Email and Password doesn't matched!", Toast.LENGTH_SHORT).show()
                    }
                }
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