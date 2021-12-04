package com.example.finalprojectassignment_20012022003

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignupActivity : AppCompatActivity() {


    private lateinit var signupEmailBox : TextInputLayout
    private lateinit var signupPasswordBox : TextInputLayout
    private lateinit var signupConfirmPasswordBox : TextInputLayout
    private lateinit var signupUsernameBox : TextInputLayout

    private lateinit var signupText : TextView

    private lateinit var signupEmailTextFieldAnimation : Animation
    private lateinit var signupPasswordTextFieldAnimation : Animation
    private lateinit var signupButtonAnimation : Animation
    private lateinit var signupViewAnimation : Animation

    private lateinit var signupAlreadyAccountTextView :TextView
    private lateinit var signupBtn : AppCompatButton


    private lateinit var signupEmailField : TextInputEditText
    private lateinit var signupUsernameField : TextInputEditText
    private lateinit var signupPasswordField : TextInputEditText
    private lateinit var signupConfirmPasswordField : TextInputEditText

    private lateinit var loaderImage : ImageView


    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupEmailField = findViewById(R.id.signup_email)
        signupPasswordField = findViewById(R.id.signup_password)
        signupConfirmPasswordField = findViewById(R.id.signup_confirm_password)
        signupUsernameField = findViewById(R.id.signup_username)

        signupEmailBox = findViewById(R.id.signup_email_box)
        signupPasswordBox = findViewById(R.id.signup_password_box)
        signupConfirmPasswordBox = findViewById(R.id.signup_confirm_password_box)
        signupUsernameBox = findViewById(R.id.signup_username_box)

        signupText = findViewById(R.id.signup_textView)
        signupBtn = findViewById(R.id.signup_btn)
        loaderImage = findViewById(R.id.loader)

        signupAlreadyAccountTextView = findViewById(R.id.signup_already_account_textView)

        setStatusBarColor(Color.TRANSPARENT)

        signupEmailTextFieldAnimation = AnimationUtils.loadAnimation(this,R.anim.welcome_btn_left_translate)
        signupPasswordTextFieldAnimation = AnimationUtils.loadAnimation(this,R.anim.welcome_btn_right_translate)
        signupButtonAnimation = AnimationUtils.loadAnimation(this,R.anim.signin_btn_scale)
        signupViewAnimation = AnimationUtils.loadAnimation(this,R.anim.signin_view_animation)


        signupEmailBox.startAnimation(signupEmailTextFieldAnimation)
        signupPasswordBox.startAnimation(signupPasswordTextFieldAnimation)
        signupConfirmPasswordBox.startAnimation(signupEmailTextFieldAnimation)
        signupUsernameBox.startAnimation(signupPasswordTextFieldAnimation)

        signupBtn.startAnimation(signupButtonAnimation)
        signupAlreadyAccountTextView.startAnimation(signupButtonAnimation)

        val dbUserController = DBUserController(this)


        signupAlreadyAccountTextView.setOnClickListener {
            Intent(this,SigninActivity::class.java).apply {
                startActivity(this)
            }
        }

        signupBtn.setOnClickListener {
            when {
                signupUsernameField.text.toString() == "" -> {
                    Toast.makeText(this, "Username is required!", Toast.LENGTH_SHORT).show()
                }
                signupEmailField.text.toString() == "" -> {
                    Toast.makeText(this, "Email is required!", Toast.LENGTH_SHORT).show()
                }
                signupPasswordField.text.toString() == "" -> {
                    Toast.makeText(this, "Password is required!", Toast.LENGTH_SHORT).show()
                }
                signupConfirmPasswordField.text.toString() == "" -> {
                    Toast.makeText(this, "Confirm Password field is Required!", Toast.LENGTH_SHORT).show()
                }
                signupConfirmPasswordField.text.toString() != signupPasswordField.text.toString() -> {
                    Toast.makeText(this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Glide.with(this).load(R.drawable.loading).into(loaderImage)
                    val userData = User(
                        signupUsernameField.text.toString(),
                        signupEmailField.text.toString(),
                        signupPasswordField.text.toString()
                    )

                    dbUserController.addUser(userData)
                    Handler().postDelayed({
                        val intent = Intent(this, SigninActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 3000)
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