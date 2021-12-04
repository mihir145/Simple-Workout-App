package com.example.finalprojectassignment_20012022003

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import com.bumptech.glide.Glide


class WorkoutListViewAdapter(private val context:Context,private val arrayList: ArrayList<Workout>) : BaseAdapter(){
    private lateinit var imageView : ImageView
    private lateinit var startBtn : AppCompatButton
    private lateinit var calorieBurnedWorkout : TextView
    private lateinit var timeTextView: TextView

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val convertView: View? = LayoutInflater.from(context).inflate(R.layout.workout_box,parent,false)
        
        this.imageView = convertView!!.findViewById(R.id.workout_item_imageView)
        this.calorieBurnedWorkout = convertView.findViewById(R.id.calorie_burned_textView)
        this.startBtn = convertView.findViewById(R.id.start_btn)
        this.timeTextView = convertView.findViewById(R.id.time_textView)

        val workout = arrayList[position]

        timeTextView.text = "${workout.minute} Minute"
        calorieBurnedWorkout.text = "${workout.burnedCalorie} Calorie Workout"

        val urlGif = workout.imageString
        val uri: Uri = Uri.parse(urlGif)

        Glide.with(context).load(uri).placeholder(R.drawable.loading).into(imageView)

        startBtn.setOnClickListener {
            context.startActivity(
                Intent(context,SingleWorkoutActivity::class.java)
                .putExtra("position",position)
            )
        }
        return convertView
    }
}
