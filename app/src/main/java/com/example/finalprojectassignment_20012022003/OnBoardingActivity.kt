package com.example.finalprojectassignment_20012022003

import android.R.attr
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import java.util.ArrayList
import android.R.attr.button
import android.content.Context
import android.content.SharedPreferences


class OnBoardingActivity : AppCompatActivity() {
    private var pager_indicator: LinearLayout? = null
    private var dotsCount = 0
    private lateinit var dots: Array<ImageView?>
    private var onboard_pager: ViewPager? = null
    private var mAdapter: OnBoardAdapter? = null
    private lateinit var btn_get_started: Button
    var previous_pos = 0
    var onBoardItems = ArrayList<OnBoardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        setStatusBarColor(Color.TRANSPARENT)

        btn_get_started = findViewById<View>(R.id.btn_get_started) as Button
        onboard_pager = findViewById<View>(R.id.pager_introduction) as ViewPager
        pager_indicator = findViewById<View>(R.id.viewPagerCountDots) as LinearLayout

        btn_get_started.setOnClickListener {

            val sharedPrefs : SharedPreferences = getSharedPreferences("FirstTimeCheck",Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = sharedPrefs.edit()
            editor.putBoolean("isFirstTime",false)
            editor.apply()

            val dbWorkoutController = DBWorkoutController(this)
            dbWorkoutController.addWorkouts()

            Handler().postDelayed({
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 0)
        }

        loadData()

        mAdapter = OnBoardAdapter(this, onBoardItems)
        onboard_pager!!.adapter = mAdapter
        onboard_pager!!.currentItem = 0
        onboard_pager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {


                // Change the current position intimation
                for (i in 0 until dotsCount) {
                    dots[i]!!
                        .setImageDrawable(
                            ContextCompat.getDrawable(
                                this@OnBoardingActivity,
                                R.drawable.non_selected_item_dot
                            )
                        )
                }
                dots[position]!!
                    .setImageDrawable(
                        ContextCompat.getDrawable(
                            this@OnBoardingActivity,
                            R.drawable.selected_item_dot
                        )
                    )
                val pos = position + 1
                if (pos == dotsCount && previous_pos == dotsCount - 1)
                    show_animation()
                else if (pos == dotsCount - 1 && previous_pos == dotsCount)
                    hide_animation()

                previous_pos = pos
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        setUiPageViewController()
    }

    // Load data into the viewpager
    fun loadData() {
        val imageId =
            intArrayOf(R.drawable.onboard_screen1, R.drawable.onboard_screen2, R.drawable.onboard_screen3,R.drawable.onboard_screen4)

        for (i in imageId.indices) {
            val item = OnBoardItem()
            item.imageID = imageId[i]
            onBoardItems.add(item)
        }
    }

    // Button bottomUp animation
    fun show_animation() {
        val show = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim)
        btn_get_started.startAnimation(show)
        show.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                btn_get_started.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                btn_get_started.clearAnimation()
            }
        })
    }

    // Button Topdown animation
    fun hide_animation() {
        val hide = AnimationUtils.loadAnimation(this, R.anim.slide_down_anim)
        btn_get_started!!.startAnimation(hide)
        hide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                btn_get_started!!.clearAnimation()
                btn_get_started!!.visibility = View.GONE
            }
        })
    }

    // setup the
    private fun setUiPageViewController() {
        dotsCount = mAdapter!!.count
        dots = arrayOfNulls(dotsCount)
        for (i in 0 until dotsCount) {
            dots[i] = ImageView(this)
            dots[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this@OnBoardingActivity,
                    R.drawable.non_selected_item_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(6, 0, 6, 0)
            pager_indicator!!.addView(dots[i], params)
        }
        dots[0]!!.setImageDrawable(
            ContextCompat.getDrawable(
                this@OnBoardingActivity,
                R.drawable.selected_item_dot
            )
        )
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