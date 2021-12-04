package com.example.finalprojectassignment_20012022003
import android.content.Context;
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import android.net.LinkAddress
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter

import java.util.ArrayList;

internal class OnBoardAdapter(mContext: Context, items: ArrayList<OnBoardItem>) :
    PagerAdapter() {
    private val mContext: Context = mContext
    var onBoardItems: ArrayList<OnBoardItem> = ArrayList()
    override fun getCount(): Int {
        return onBoardItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View =
            LayoutInflater.from(mContext).inflate(R.layout.onboard_item, container, false)

        val item: OnBoardItem = onBoardItems[position]

        val imageView: ImageView = itemView.findViewById(R.id.iv_onboard) as ImageView

        imageView.setImageResource(item.imageID)

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    init {
        onBoardItems = items
    }
}